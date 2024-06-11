package macaw.presentationLayer;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MacawWorkBenchTest {

    @Test
    public void testEditConstants() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.editConstants();
        // Add assertions here to verify the behavior of the method
    }

    @Test
    public void testEditVariables() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.editVariables();
        // Add assertions here to verify the behavior of the method
    }

    @Test
    public void testExportVariableData() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.exportVariableData();
        // Add assertions here to verify the behavior of the method
    }

    @Test
    public void testExit() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.exit();
        // Add assertions here to verify the behavior of the method
    }

}