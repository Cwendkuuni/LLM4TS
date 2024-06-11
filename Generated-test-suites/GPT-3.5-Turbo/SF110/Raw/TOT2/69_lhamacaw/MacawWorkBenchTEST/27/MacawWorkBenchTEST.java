package macaw.presentationLayer;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {
    
    private MacawWorkBench macawWorkBench;
    
    @Before
    public void setUp() {
        macawWorkBench = new MacawWorkBench(new SessionProperties());
    }
    
    @After
    public void tearDown() {
        macawWorkBench = null;
    }
    
    @Test
    public void testEditConstants() {
        // Typical use case
        macawWorkBench.editConstants();
        // Edge case: Constants editor is null
        assertNull("Constants editor should be null", macawWorkBench.getVariableConstantsEditor());
    }
    
    @Test
    public void testEditVariables() {
        // Typical use case
        macawWorkBench.editVariables();
        // Edge case: Variable browser is null
        assertNull("Variable browser should be null", macawWorkBench.getMacawVariableBrowser());
    }
    
    @Test
    public void testExportVariableData() {
        // Typical use case
        macawWorkBench.exportVariableData();
        // Edge case: Export format is incorrect
        assertFalse("Export format should be incorrect", macawWorkBench.isExportFormatCorrect());
    }
    
    @Test
    public void testExit() {
        // Typical use case
        macawWorkBench.exit();
        // Edge case: System should not exit when exit button is pressed
        assertFalse("System should not have exited", macawWorkBench.isSystemExited());
    }
    
    @Test
    public void testActionPerformed() {
        // Typical use case: Edit Constants button pressed
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getEditConstantsButton(), ActionEvent.ACTION_PERFORMED, ""));
        // Edge case: Edit Variables button pressed
        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.getEditVariablesButton(), ActionEvent.ACTION_PERFORMED, ""));
        // Exception handling scenario
        try {
            macawWorkBench.actionPerformed(new ActionEvent(null, ActionEvent.ACTION_PERFORMED, ""));
        } catch (Exception e) {
            assertTrue("Exception should be caught", e instanceof NullPointerException);
        }
    }
}