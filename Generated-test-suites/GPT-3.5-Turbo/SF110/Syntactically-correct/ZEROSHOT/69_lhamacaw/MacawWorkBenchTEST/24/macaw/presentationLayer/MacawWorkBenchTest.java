import org.junit.*;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBench() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        assertNotNull(workBench);
    }

    @Test
    public void testEditConstants() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.editConstants();
        // Add assertions here
    }

    @Test
    public void testEditVariables() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.editVariables();
        // Add assertions here
    }

    @Test
    public void testExportVariableData() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.exportVariableData();
        // Add assertions here
    }

    @Test
    public void testExit() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.exit();
        // Add assertions here
    }

    // Additional tests for actionPerformed() can be added if needed

}