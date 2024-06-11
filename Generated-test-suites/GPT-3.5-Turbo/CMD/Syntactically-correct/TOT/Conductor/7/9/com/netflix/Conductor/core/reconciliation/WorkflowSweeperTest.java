import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

public class WorkflowSweeperTest {

    @Test
    public void testSweepAsync() throws InterruptedException, ExecutionException {
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        ConductorProperties properties = mock(ConductorProperties.class);
        QueueDAO queueDAO = mock(QueueDAO.class);

        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        
        CompletableFuture<Void> future = sweeper.sweepAsync("test_workflow_id");
        future.get();
        
        // Verify that the sweepAsync method calls the sweep method
        verify(workflowExecutor, times(1)).decide("test_workflow_id");
    }

    @Test
    public void testSweep() {
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        ConductorProperties properties = mock(ConductorProperties.class);
        QueueDAO queueDAO = mock(QueueDAO.class);

        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        // Test case for workflow not found
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException());
        sweeper.sweep("non_existent_workflow_id");
        
        // Verify that the queueDAO.remove method is called when workflow is not found
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "non_existent_workflow_id");
    }

    @Test
    public void testUnack() {
        WorkflowSweeper sweeper = new WorkflowSweeper();

        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel1.setWaitTimeout(10);
        taskModel1.setTaskType(TaskType.TASK_TYPE_WAIT);
        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(TaskModel.Status.SCHEDULED);

        workflowModel.getTasks().add(taskModel1);
        workflowModel.getTasks().add(taskModel2);

        sweeper.unack(workflowModel, 20);

        // Add more assertions as needed for different scenarios
        assertNotNull(workflowModel);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper sweeper = new WorkflowSweeper();

        long jitter = sweeper.workflowOffsetWithJitter(45);

        // Verify jitter values fall within the expected range [30-60]
        assertTrue(jitter >= 30 && jitter <= 60);
    }
}