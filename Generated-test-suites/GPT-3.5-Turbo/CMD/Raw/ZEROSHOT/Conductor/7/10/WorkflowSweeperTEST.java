import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAO = mock(QueueDAO.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), new ConductorProperties(), queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30 && result <= 60);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);
        workflowModel.getTasks().add(taskModel);

        workflowSweeper.unack(workflowModel, 45);

        verify(queueDAO, times(1)).setUnackTimeout(eq("decider_queue"), eq("testWorkflowId"), eq(11*1000L));
    }

    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "notFoundWorkflowId";
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(eq("decider_queue"), eq(workflowId));
    }

    @Test
    public void testSweepWorkflowTerminalStatus() {
        String workflowId = "terminalStatusWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(eq("decider_queue"), eq(workflowId));
    }
}