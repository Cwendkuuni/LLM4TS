import org.junit.Test;
import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {

    @Test
    public void testSweepAsync() {
        // Test functionality of sweepAsync() method
    }

    @Test
    public void testSweep() {
        // Edge case: Workflow is null
        // Test NotFoundException handling in the sweep() method
        // Test Exception handling in the sweep() method
    }

    @Test
    public void testUnack() {
        // Edge case: TaskModel status is IN_PROGRESS or SCHEDULED
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Test functionality of workflowOffsetWithJitter() method
    }

}