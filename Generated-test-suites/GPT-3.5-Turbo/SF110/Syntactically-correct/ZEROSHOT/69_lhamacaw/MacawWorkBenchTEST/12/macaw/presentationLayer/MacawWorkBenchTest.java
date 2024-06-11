package macaw.presentationLayer;

import org.junit.Test;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {
    @Test
    public void testConstructor() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        assertNotNull(macawWorkBench);
    }
    
    @Test
    public void testShow() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.show();
        // Assert that the dialog is visible
    }
    
    @Test
    public void testEditConstants() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.editConstants();
        // Assert that the variable constants editor is opened
    }
    
    @Test
    public void testEditVariables() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.editVariables();
        // Assert that the variable browser is opened
    }
    
    @Test
    public void testExportVariableData() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.exportVariableData();
        // Assert that the variables are exported to MacawXML format
    }
    
    @Test
    public void testExit() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.exit();
        // Assert that the system exits
    }
}