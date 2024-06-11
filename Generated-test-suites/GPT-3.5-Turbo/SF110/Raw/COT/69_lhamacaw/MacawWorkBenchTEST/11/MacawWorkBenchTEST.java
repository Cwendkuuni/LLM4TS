
package macaw.presentationLayer;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.border.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;
import java.awt.event.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {
    
    private MacawWorkBench macawWorkBench;
    
    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }
    
    @Test
    public void testShow() {
        macawWorkBench.show();
        assertTrue(macawWorkBench.getDialog().isVisible());
    }
    
    @Test
    public void testActionPerformed_editConstants() {
        ActionEvent event = new ActionEvent(macawWorkBench.getEditConstants(), ActionEvent.ACTION_PERFORMED, "editConstants");
        macawWorkBench.actionPerformed(event);
        //TODO: Add assertion to check if editConstants method was called
    }
    
    @Test
    public void testActionPerformed_editVariables() {
        ActionEvent event = new ActionEvent(macawWorkBench.getEditVariables(), ActionEvent.ACTION_PERFORMED, "editVariables");
        macawWorkBench.actionPerformed(event);
        //TODO: Add assertion to check if editVariables method was called
    }
    
    @Test
    public void testActionPerformed_exportVariableData() {
        ActionEvent event = new ActionEvent(macawWorkBench.getExportVariableData(), ActionEvent.ACTION_PERFORMED, "exportVariableData");
        macawWorkBench.actionPerformed(event);
        //TODO: Add assertion to check if exportVariableData method was called
    }
    
    @Test
    public void testActionPerformed_exit() {
        ActionEvent event = new ActionEvent(macawWorkBench.getExit(), ActionEvent.ACTION_PERFORMED, "exit");
        macawWorkBench.actionPerformed(event);
        //TODO: Add assertion to check if exit method was called
    }
}
