import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.*;

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
        // Add assertion here based on the functionality of editConstants method
    }

    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables();
        // Add assertion here based on the functionality of editVariables method
    }

    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData();
        // Add assertion here based on the functionality of exportVariableData method
    }

    @Test
    public void testExit() {
        // Mock System.exit(0) to prevent actual exit
        try (MockedStatic<System> systemMock = Mockito.mockStatic(System.class)) {
            macawWorkBench.exit();
        }
    }

    @Test
    public void testActionPerfomed_EditConstants() {
        ActionEvent event = new ActionEvent(macawWorkBench.editConstants, ActionEvent.ACTION_PERFORMED, "Edit Constants");
        macawWorkBench.actionPerformed(event);
        // Add assertion here based on the expected behavior of actionPerformed method
    }

    @Test
    public void testActionPerfomed_EditVariables() {
        ActionEvent event = new ActionEvent(macawWorkBench.editVariables, ActionEvent.ACTION_PERFORMED, "Edit Variables");
        macawWorkBench.actionPerformed(event);
        // Add assertion here based on the expected behavior of actionPerformed method
    }

    @Test
    public void testActionPerfomed_ExportVariableData() {
        ActionEvent event = new ActionEvent(macawWorkBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "Export Variable Data");
        macawWorkBench.actionPerformed(event);
        // Add assertion here based on the expected behavior of actionPerformed method
    }

    @Test
    public void testActionPerfomed_Exit() {
        ActionEvent event = new ActionEvent(macawWorkBench.exit, ActionEvent.ACTION_PERFORMED, "Exit");
        macawWorkBench.actionPerformed(event);
        // Add assertion here based on the expected behavior of actionPerformed method
    }
}