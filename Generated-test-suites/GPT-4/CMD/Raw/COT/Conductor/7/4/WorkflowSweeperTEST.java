import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.mockito.Mockito;

public class WorkflowSweeperTest {

    @Test
    public void testConstructor() {
        WorkflowExecutor executor = Mockito.mock(WorkflowExecutor.class);
        Optional<WorkflowRepairService> repairService = Optional.empty();
        ConductorProperties properties = new ConductorProperties();
        QueueDAO queueDAO = Mockito.mock(QueueDAO.class);

        WorkflowSweeper sweeper = new WorkflowSweeper(executor, repairService, properties, queueDAO);
        assertNotNull(sweeper);
    }

    @Test
    public void testSweepAsync() {
        // ... similar setup to above ...
        String workflowId = "testId";
        // Need to ensure we allow for async processing in our testing here
        CompletableFuture<Void> future = sweeper.sweepAsync(workflowId);
        future.join();
        // Test successful completion, error cases would depend on method specifics 
        assertTrue(future.isDone());
    }

    @Test
    public void testSweep() {
        // ... similar setup to above ...
        String workflowId = "testId";
        sweeper.sweep(workflowId);
        // Perform checks here based on what the sweep method should achieve
    }

    @Test
    public void testUnack() {
        // Requires a complete WorkflowModel object to be created/mocked, methods called on it checked
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Basic functionality check, edge cases will be developed separately
        long offset = 60;
        long result = sweeper.workflowOffsetWithJitter(offset);
        assertTrue("Result should be within range", result >= 40 && result <= 80);
    }

    @Test
    public void testWorkflowOffsetWithJitterAtMinimum() {
        long offset = 1;
        long result = sweeper.workflowOffsetWithJitter(offset);
        assertTrue("Result should not be negative", result >= 0);
    }
}

//