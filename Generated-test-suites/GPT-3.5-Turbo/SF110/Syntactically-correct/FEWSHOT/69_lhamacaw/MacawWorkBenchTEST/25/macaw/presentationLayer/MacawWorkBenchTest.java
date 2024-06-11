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
    
    private MacawWorkBench workBench;
    
    @Before
    public void setup() {
        SessionProperties sessionProperties = new SessionProperties();
        workBench = new MacawWorkBench(sessionProperties);
    }
    
    @Test
    public void testEditConstants() {
        workBench.editConstants();
    }
    
    @Test
    public void testEditVariables() {
        workBench.editVariables();
    }
    
    @Test
    public void testExportVariableData() {
        workBench.exportVariableData();
    }
    
    @Test
    public void testExit() {
        workBench.exit();
    }
    
    @Test
    public void testActionPerformed_EditConstants() {
        ActionEvent event = new ActionEvent(workBench.editConstants, ActionEvent.ACTION_PERFORMED, null);
        workBench.actionPerformed(event);
    }
    
    @Test
    public void testActionPerformed_EditVariables() {
        ActionEvent event = new ActionEvent(workBench.editVariables, ActionEvent.ACTION_PERFORMED, null);
        workBench.actionPerformed(event);
    }
    
    @Test
    public void testActionPerformed_ExportVariableData() {
        ActionEvent event = new ActionEvent(workBench.exportVariableData, ActionEvent.ACTION_PERFORMED, null);
        workBench.actionPerformed(event);
    }
    
    @Test
    public void testActionPerformed_Exit() {
        ActionEvent event = new ActionEvent(workBench.exit, ActionEvent.ACTION_PERFORMED, null);
        workBench.actionPerformed(event);
    }
}