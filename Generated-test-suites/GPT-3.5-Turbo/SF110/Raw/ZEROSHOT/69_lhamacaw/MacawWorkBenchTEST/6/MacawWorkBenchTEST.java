package macaw.presentationLayer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;

    @Before
    public void setUp() {
        macawWorkBench = new MacawWorkBench(null);
    }

    @Test
    public void testEditConstants() {
        macawWorkBench.editConstants();
        // TODO: Add assertions
    }

    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables();
        // TODO: Add assertions
    }
    
    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData();
        // TODO: Add assertions
    }

    @Test
    public void testExit() {
        macawWorkBench.exit();
        // TODO: Add assertions
    }
}