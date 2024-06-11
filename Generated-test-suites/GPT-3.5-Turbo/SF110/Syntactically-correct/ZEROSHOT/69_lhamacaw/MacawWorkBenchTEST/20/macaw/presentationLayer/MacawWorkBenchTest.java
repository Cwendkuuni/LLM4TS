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

    @Test
    public void testEditConstants() {
        macawWorkBench.editConstants();
        // Add assertion statements
    }

    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables();
        // Add assertion statements
    }

    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData();
        // Add assertion statements
    }

    @Test
    public void testExit() {
        macawWorkBench.exit();
        // Add assertion statements
    }

    @Test
    public void testActionPerformed_EditConstantsButton() {
        // Create ActionEvent object for editConstants button click
        // Call actionPerformed method with the created ActionEvent object
        // Add assertion statements
    }

    @Test
    public void testActionPerformed_EditVariablesButton() {
        // Create ActionEvent object for editVariables button click
        // Call actionPerformed method with the created ActionEvent object
        // Add assertion statements
    }

    @Test
    public void testActionPerformed_ExportVariableDataButton() {
        // Create ActionEvent object for exportVariableData button click
        // Call actionPerformed method with the created ActionEvent object
        // Add assertion statements
    }

    @Test
    public void testActionPerformed_ExitButton() {
        // Create ActionEvent object for exit button click
        // Call actionPerformed method with the created ActionEvent object
        // Add assertion statements
    }
}