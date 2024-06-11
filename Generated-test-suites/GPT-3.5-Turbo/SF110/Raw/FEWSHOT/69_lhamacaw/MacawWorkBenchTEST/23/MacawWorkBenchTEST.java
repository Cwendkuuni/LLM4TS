
package macaw.presentationLayer;

import static org.junit.Assert.*;
import org.junit.Test;

public class MacawWorkBenchTest {

    @Test
    public void testConstructor() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workbench = new MacawWorkBench(sessionProperties);
        assertNotNull(workbench);
    }

    @Test
    public void testShow() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workbench = new MacawWorkBench(sessionProperties);
        workbench.show();
        // Ensure the dialog is visible
        assertTrue(workbench.dialog.isVisible());
    }

    @Test
    public void testEditConstants() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workbench = new MacawWorkBench(sessionProperties);
        workbench.editConstants();
        // Provide assertions for the behavior after editConstants() method is executed
    }

    @Test
    public void testEditVariables() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workbench = new MacawWorkBench(sessionProperties);
        workbench.editVariables();
        // Provide assertions for the behavior after editVariables() method is executed
    }

    @Test
    public void testExportVariableData() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workbench = new MacawWorkBench(sessionProperties);
        workbench.exportVariableData();
        // Provide assertions for the behavior after exportVariableData() method is executed
    }

    @Test
    public void testExit() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workbench = new MacawWorkBench(sessionProperties);
        workbench.exit();
        // Provide assertions for the behavior after exit() method is executed
    }

    @Test
    public void testActionPerformed() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workbench = new MacawWorkBench(sessionProperties);
        // Simulate action events for each button and test the corresponding method calls
        // Call workbench.actionPerformed(event) for each button and provide assertions for the behavior
    }

}
