import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(new WorkflowExecutor(), Optional.empty(), new ConductorProperties(), new QueueDAO());
    }

    @Test
    public void testSweepAsync() {
        // Test typical use case
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
        // Test typical use case with workflow found
        workflowSweeper.sweep("workflowId");

        // Test case when workflow is not found
        workflowSweeper.sweep("workflowIdNotFound");
    }

    @Test
    public void testUnack() {
        // Create a sample workflow model
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(30);
        workflowModel.getTasks().add(taskModel);

        // Test typical use case
        workflowSweeper.unack(workflowModel, 45);

        // Test case when task status is SCHEDULED
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        workflowSweeper.unack(workflowModel, 45);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Test for a given workflowOffsetTimeout
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        assertTrue(result >= 30 && result <= 60);
    }
}