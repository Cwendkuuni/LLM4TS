import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.awt.event.ActionEvent;

public class MacawWorkBenchTest {
    
    private MacawWorkBench macawWorkBench;
    
    @Before
    public void setUp() {
        macawWorkBench = new MacawWorkBench(new SessionProperties());
    }
    
    @Test
    public void testEditConstants() {
        macawWorkBench.editConstants();
        // Add assertions to test the functionality of editConstants method
    }
    
    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables();
        // Add assertions to test the functionality of editVariables method
    }
    
    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData();
        // Add assertions to test the functionality of exportVariableData method
    }
    
    @Test
    public void testExit() {
        // Not testing as it calls System.exit(0)
    }
    
    @Test
    public void testActionPerformedEditConstants() {
        ActionEvent event = new ActionEvent(macawWorkBench.editConstants, 0, "");
        macawWorkBench.actionPerformed(event);
        // Add assertions to test the actionPerformed method for editConstants button
    }
    
    @Test
    public void testActionPerformedEditVariables() {
        ActionEvent event = new ActionEvent(macawWorkBench.editVariables, 0, "");
        macawWorkBench.actionPerformed(event);
        // Add assertions to test the actionPerformed method for editVariables button
    }
    
    @Test
    public void testActionPerformedExportVariableData() {
        ActionEvent event = new ActionEvent(macawWorkBench.exportVariableData, 0, "");
        macawWorkBench.actionPerformed(event);
        // Add assertions to test the actionPerformed method for exportVariableData button
    }
    
    @Test
    public void testActionPerformedExit() {
        ActionEvent event = new ActionEvent(macawWorkBench.exit, 0, "");
        macawWorkBench.actionPerformed(event);
        // Add assertions to test the actionPerformed method for exit button
    }
}