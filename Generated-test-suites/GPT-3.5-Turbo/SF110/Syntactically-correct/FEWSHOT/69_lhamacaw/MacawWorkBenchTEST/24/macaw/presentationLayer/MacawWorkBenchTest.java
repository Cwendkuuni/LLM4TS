package macaw.presentationLayer;

import static org.junit.Assert.*;
import org.junit.Test;

public class MacawWorkBenchTest {
    @Test
    public void testMacawWorkBench() {
        // Test the initialization of MacawWorkBench object
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench);
    }

    @Test
    public void testShow() {
        // Test the show method of MacawWorkBench
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.show();
        assertTrue(macawWorkBench.isVisible());
    }

    @Test
    public void testEditConstants() {
        // Test the editConstants method of MacawWorkBench
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.editConstants();
        // Add assertion here
    }

    @Test
    public void testEditVariables() {
        // Test the editVariables method of MacawWorkBench
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.editVariables();
        // Add assertion here
    }

    @Test
    public void testExportVariableData() {
        // Test the exportVariableData method of MacawWorkBench
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.exportVariableData();
        // Add assertion here
    }

    @Test
    public void testExit() {
        // Test the exit method of MacawWorkBench
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.exit();
        // Add assertion here
    }

    @Test
    public void testActionPerformed() {
        // Test the actionPerformed method of MacawWorkBench
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.actionPerformed(null);
        // Add assertion here
    }
}