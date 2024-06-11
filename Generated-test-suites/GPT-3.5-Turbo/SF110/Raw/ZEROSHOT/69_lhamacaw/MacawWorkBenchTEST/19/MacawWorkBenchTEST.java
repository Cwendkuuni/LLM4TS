package macaw.presentationLayer;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.awt.event.ActionEvent;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.system.*;
import macaw.businessLayer.*;

public class MacawWorkBenchTest {
    
    private MacawWorkBench workBench;
    
    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        workBench = new MacawWorkBench(sessionProperties);
    }
    
    @Test
    public void testEditConstants() {
        workBench.editConstants();
        // Test edit constants functionality here
    }
    
    @Test
    public void testEditVariables() {
        workBench.editVariables();
        // Test edit variables functionality here
    }
    
    @Test
    public void testExportVariableData() {
        workBench.exportVariableData();
        // Test export variable data functionality here
    }
    
    @Test
    public void testExit() {
        workBench.exit();
        // Test exit functionality here
    }
    
    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(this, 0, "editConstants");
        workBench.actionPerformed(event);
        // Test actionPerformed method for editConstants button
        
        event = new ActionEvent(this, 0, "editVariables");
        workBench.actionPerformed(event);
        // Test actionPerformed method for editVariables button
        
        event = new ActionEvent(this, 0, "exportVariableData");
        workBench.actionPerformed(event);
        // Test actionPerformed method for exportVariableData button
        
        event = new ActionEvent(this, 0, "exit");
        workBench.actionPerformed(event);
        // Test actionPerformed method for exit button
    }
}