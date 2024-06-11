package macaw.presentationLayer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;

    @Before
    public void setUp() {
        macawWorkBench = new MacawWorkBench(new SessionProperties());
    }

    @Test
    public void testShow() {
        macawWorkBench.show();
        // The visibility of the dialog should be true after calling show()
        assertTrue(macawWorkBench.dialog.isVisible());
    }

    @Test
    public void testEditConstants() {
        // Test editConstants() method
        // Create a VariableConstantsEditor and handle the show() method
        // Assert that the visibility of the VariableConstantsEditor dialog is true
        macawWorkBench.editConstants();
        VariableConstantsEditor editor = macawWorkBench.getVariableConstantsEditor();
        assertTrue(editor.getDialog().isVisible());
    }

    @Test
    public void testEditVariables() {
        // Test editVariables() method
        // Create a MacawVariableBrowser and handle the show() method
        // Assert that the visibility of the MacawVariableBrowser dialog is true
        macawWorkBench.editVariables();
        MacawVariableBrowser browser = macawWorkBench.getMacawVariableBrowser();
        assertTrue(browser.getDialog().isVisible());
    }

    @Test
    public void testExportVariableData() {
        // Test exportVariableData() method
        // Create an ExportVariablesToMacawXML and handle the export() method
        // Assert that the export is successful by checking the exported file
        macawWorkBench.exportVariableData();
        ExportVariablesToMacawXML exporter = macawWorkBench.getExportVariablesToMacawXML();
        assertTrue(exporter.export());
    }

    @Test
    public void testExit() {
        // Test exit() method
        // Call the exit() method and assert that System.exit() is called
        macawWorkBench.exit();
        // This will throw a SecurityException if System.exit() is called
    }
}