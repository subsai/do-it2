/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calimeroex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.DatapointMap;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListenerEx; 
import tuwien.auto.calimero.xml.def.DefaultXMLReader;

/**
 *
 * @author davidbertalan
 */
public class CalimeroEX extends ProcessListenerEx{
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        int i=0;
        String hostname = "shk-b003561-knxip.aat.technikum-wien.at";
        String group1="0/0/1";
        String XMLFILE="/Users/davidbertalan/KNX.xml";
        BufferedReader BReader = null;
        
        try {
            BReader = new BufferedReader(new FileReader(new File(XMLFILE)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CalimeroEX.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultXMLReader XMLread=new DefaultXMLReader();
        DatapointMap dpm=new DatapointMap();

        try {
          KNXNetworkLink Linker = new KNXNetworkLinkIP(hostname, TPSettings.TP1);
          ProcessCommunicator pc = new ProcessCommunicatorImpl(Linker);
          pc.addProcessListener(new CalimeroEX());  
          
            XMLread.setInput(BReader, false);
            dpm.load(XMLread);
            XMLread.close();
            
            ArrayList<Datapoint> da = new ArrayList<Datapoint>(dpm.getDatapoints());
            
            for(Datapoint dp:da){
                if(Double.parseDouble(dp.getDPT())==1.001){
                pc.write(dp.getMainAddress(), true);
                Thread.sleep(2000);
                }
            }
            
            for(Datapoint dp:da){
                if(Double.parseDouble(dp.getDPT())==1.001){
                pc.write(dp.getMainAddress(), false);
                Thread.sleep(2000);
                }
            }  
            
//            Iterator<Datapoint> itr = dpm.getDatapoints().iterator();   // brauche noch einen iterator
//            while(itr.hasNext()){
//                Datapoint d = itr.next();
//                d.getDPT()....
//            }
         

            
           while(i<5){
            Thread.sleep(1000);
            pc.write(new GroupAddress(group1), true);
            
            Thread.sleep(1000);
            pc.write(new GroupAddress(group1), false);
            i++;
            }
           
            Linker.close();
          
        } catch (KNXException ex) {
            Logger.getLogger(CalimeroEX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CalimeroEX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void groupReadRequest(ProcessEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println(e.toString());
    }

    @Override
    public void groupReadResponse(ProcessEvent e) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    System.out.println(e.toString());
    }

    @Override
    public void groupWrite(ProcessEvent e) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   System.out.println(e.toString());
    }

    @Override
    public void detached(DetachEvent e) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    System.out.println(e.toString());
    }
    
}