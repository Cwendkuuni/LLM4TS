import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Test
    public void testSweepAsync() {
        // typical use case
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        CompletableFuture<Void> completableFuture = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(completableFuture);
    }

    @Test
    public void testSweep() {
        // typical use case
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        workflowSweeper.sweep("testWorkflowId");
        // add assertions for the expected behavior
    }

    @Test
    public void testUnack() {
        // typical use case
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(Status.IN_PROGRESS);
        
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10); // edge case
        workflowModel.getTasks().add(taskModel);

        QueueDAO queueDAO = mock(QueueDAO.class);
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        workflowSweeper.unack(workflowModel, 60);
        // add assertions for the expected behavior
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper();
        long jitterValue = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(jitterValue >= 30 && jitterValue <= 60); // edge case
    }
}