
import org.junit.Test;
import java.util.concurrent.CompletableFuture;
import static org.junit.Assert.assertEquals;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        assertEquals(null, result.get());
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("workflowId");
        // Additional assertions can be added based on the behavior of sweep method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(45);
        // Add assertions for jitter value within the expected range
    }
}
