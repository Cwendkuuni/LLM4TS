import org.junit.*;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {

    @Test
    public void testEditConstants() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.editConstants();
        // add assertions
    }

    @Test
    public void testEditVariables() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.editVariables();
        // add assertions
    }

    @Test
    public void testExportVariableData() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.exportVariableData();
        // add assertions
    }

    @Test
    public void testExit() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.exit();
        // add assertions
    }
}