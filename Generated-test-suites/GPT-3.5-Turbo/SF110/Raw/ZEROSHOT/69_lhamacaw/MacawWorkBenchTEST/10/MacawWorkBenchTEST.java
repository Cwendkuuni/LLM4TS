package macaw.presentationLayer;

import org.junit.*;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {
    
    @Test
    public void testEditConstants() {
        // Test editConstants method
        MacawWorkBench workBench = new MacawWorkBench(null);
        workBench.editConstants();
        // Add assertions
    }
    
    @Test
    public void testEditVariables() {
        // Test editVariables method
        MacawWorkBench workBench = new MacawWorkBench(null);
        workBench.editVariables();
        // Add assertions
    }
    
    @Test
    public void testExportVariableData() {
        // Test exportVariableData method
        MacawWorkBench workBench = new MacawWorkBench(null);
        workBench.exportVariableData();
        // Add assertions
    }
    
    @Test
    public void testExit() {
        // Test exit method
        MacawWorkBench workBench = new MacawWorkBench(null);
        workBench.exit();
        // Add assertions
    }
    
    @Test
    public void testActionPerformedEditConstants() {
        // Test actionPerformed method with editConstants button
        MacawWorkBench workBench = new MacawWorkBench(null);
        workBench.actionPerformed(null);
        // Add assertions
    }
    
    @Test
    public void testActionPerformedEditVariables() {
        // Test actionPerformed method with editVariables button
        MacawWorkBench workBench = new MacawWorkBench(null);
        workBench.actionPerformed(null);
        // Add assertions
    }
    
    @Test
    public void testActionPerformedExportVariableData() {
        // Test actionPerformed method with exportVariableData button
        MacawWorkBench workBench = new MacawWorkBench(null);
        workBench.actionPerformed(null);
        // Add assertions
    }
    
    @Test
    public void testActionPerformedExit() {
        // Test actionPerformed method with exit button
        MacawWorkBench workBench = new MacawWorkBench(null);
        workBench.actionPerformed(null);
        // Add assertions
    }
}