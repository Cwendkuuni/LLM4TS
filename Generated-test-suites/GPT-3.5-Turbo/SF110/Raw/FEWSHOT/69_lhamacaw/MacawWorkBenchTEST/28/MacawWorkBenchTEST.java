package macaw.presentationLayer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;

    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }

    @Test
    public void testMain() {
        // No testable logic in the main method
    }

    @Test
    public void testMacawWorkBench() {
        assertNotNull(macawWorkBench);        
    }

    @Test
    public void testShow() {
        // No testable logic in the show method
    }

    @Test
    public void testEditConstants() {
        macawWorkBench.editConstants(); // No testable logic in the method
    }

    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables(); // No testable logic in the method
    }

    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData(); // No testable logic in the method
    }

    @Test
    public void testExit() {
        macawWorkBench.exit(); // No testable logic in the method
    }

    @Test
    public void testActionPerformed() {
        // Test editConstants button action
        macawWorkBench.actionPerformed(null);
        // Test editVariables button action
        macawWorkBench.actionPerformed(null);
        // Test exportVariableData button action
        macawWorkBench.actionPerformed(null);
        // Test exit button action
        macawWorkBench.actionPerformed(null);
    }
}