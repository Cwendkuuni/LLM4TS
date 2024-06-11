
import static org.junit.Assert.*;
import org.junit.Test;

public class WorkflowSweeperTest {

    @Test
    public void testSweepAsync() {
        WorkflowSweeper sweeper = new WorkflowSweeper();
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = sweeper.sweepAsync(workflowId);
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
        WorkflowSweeper sweeper = new WorkflowSweeper();
        String workflowId = "testWorkflowId";
        sweeper.sweep(workflowId);
        // Add assertions here to validate the sweep method
    }

    @Test
    public void testUnack() {
        WorkflowSweeper sweeper = new WorkflowSweeper();
        WorkflowModel workflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 60; // Example value
        sweeper.unack(workflowModel, workflowOffsetTimeout);
        // Add assertions here to validate the unack method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper sweeper = new WorkflowSweeper();
        long workflowOffsetTimeout = 45; // Example value
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30 && result <= 60);
    }

    // Add more test cases for edge cases and exception handling scenarios if needed

}
