
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        workflowSweeper = new WorkflowSweeper(mock(WorkflowExecutor.class), 
            mock(WorkflowRepairService.class), new ConductorProperties(), mock(QueueDAO.class));
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("test_workflow_id");
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("test_workflow_id");
        // Add more assertions based on the expected behavior of the sweep method
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(new ArrayList<>());
        workflowSweeper.unack(workflowModel, 60);
        // Add more assertions based on the expected behavior of the unack method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(result >= 30 && result <= 60);
    }
}
