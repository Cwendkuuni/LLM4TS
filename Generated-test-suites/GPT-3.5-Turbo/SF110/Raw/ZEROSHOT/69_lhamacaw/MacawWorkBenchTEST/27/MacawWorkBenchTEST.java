package macaw.presentationLayer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {
    
    private MacawWorkBench macawWorkBench;
    
    @Before
    public void setUp() {
        macawWorkBench = new MacawWorkBench(null);
    }
    
    @Test
    public void testEditConstants() {
        macawWorkBench.editConstants();
        // Add assertions here
    }
    
    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables();
        // Add assertions here
    }
    
    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData();
        // Add assertions here
    }
    
    @Test
    public void testExit() {
        macawWorkBench.exit();
        // Add assertions here
    }
    
    @Test
    public void testActionPerformed_EditConstants() {
        macawWorkBench.actionPerformed(null);
        // Add assertions here
    }
    
    @Test
    public void testActionPerformed_EditVariables() {
        macawWorkBench.actionPerformed(null);
        // Add assertions here
    }
    
    @Test
    public void testActionPerformed_ExportVariableData() {
        macawWorkBench.actionPerformed(null);
        // Add assertions here
    }
    
    @Test
    public void testActionPerformed_Exit() {
        macawWorkBench.actionPerformed(null);
        // Add assertions here
    }
}