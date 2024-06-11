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

    // Test case for the main method when startupOptions.useDemo() is true
    @Test
    public void testMain_useDemo() {
        String[] arguments = {"-demo"};
        MacawWorkBench.main(arguments);

        // Validate that sessionProperties has the correct properties set
        SessionProperties sessionProperties = macawWorkBench.getSessionProperties();
        assertTrue(sessionProperties.getProperty("startupOptions") instanceof StartupOptions);
        assertTrue(sessionProperties.getProperty("currentUser") instanceof User);
        assertTrue(sessionProperties.getProperty("retrievalService") instanceof DemonstrationRetrievalService);
        assertTrue(sessionProperties.getProperty("database") instanceof DemonstrationCurationService);

        // Validate that the login dialog is displayed
        assertNotNull(macawWorkBench.getLoginDialog());
        assertFalse(macawWorkBench.getLoginDialog().isLoginSuccessful());
    }

    // Test case for the main method when startupOptions.useDemo() is false
    @Test
    public void testMain_notUseDemo() {
        String[] arguments = {"-notDemo"};
        MacawWorkBench.main(arguments);

        // Validate that sessionProperties has the correct properties set
        SessionProperties sessionProperties = macawWorkBench.getSessionProperties();
        assertTrue(sessionProperties.getProperty("startupOptions") instanceof StartupOptions);
        assertTrue(sessionProperties.getProperty("currentUser") instanceof User);
        assertTrue(sessionProperties.getProperty("retrievalService") instanceof ProductionRetrievalService);
        assertTrue(sessionProperties.getProperty("database") instanceof ProductionCurationService);

        // Validate that the login dialog is displayed
        assertNotNull(macawWorkBench.getLoginDialog());
        assertFalse(macawWorkBench.getLoginDialog().isLoginSuccessful());
    }

    // Test case for the actionPerformed method when editConstants button is clicked
    @Test
    public void testActionPerformed_editConstants() {
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getEditConstants(), ActionEvent.ACTION_PERFORMED, null));

        // Validate that the editConstants method is called
        // Implementation details of VariableConstantsEditor are not accessible for verification
    }

    // Test case for the actionPerformed method when editVariables button is clicked
    @Test
    public void testActionPerformed_editVariables() {
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getEditVariables(), ActionEvent.ACTION_PERFORMED, null));

        // Validate that the editVariables method is called
        // Implementation details of MacawVariableBrowser are not accessible for verification
    }

    // Test case for the actionPerformed method when exportVariableData button is clicked
    @Test
    public void testActionPerformed_exportVariableData() {
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getExportVariableData(), ActionEvent.ACTION_PERFORMED, null));

        // Validate that the exportVariableData method is called
        // Implementation details of ExportVariablesToMacawXML are not accessible for verification
    }

    // Test case for the actionPerformed method when exit button is clicked
    @Test
    public void testActionPerformed_exit() {
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getExit(), ActionEvent.ACTION_PERFORMED, null));

        // Validate that the exit method is called
        // System.exit() cannot be verified through unit testing
    }

}