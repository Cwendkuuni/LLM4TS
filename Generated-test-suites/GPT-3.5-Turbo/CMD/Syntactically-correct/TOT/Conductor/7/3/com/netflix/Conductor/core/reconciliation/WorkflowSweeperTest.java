import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("test_workflow_id");
        assertEquals(CompletableFuture.completedFuture(null), result);
    }

    @Test
    public void testSweep() {
        Mockito.when(workflowExecutor.decide(anyString())).thenReturn(null);
        workflowSweeper.sweep("test_workflow_id");
        Mockito.verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("test_workflow_id");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(30);
        workflowModel.addTask(taskModel);

        Mockito.when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(true);

        workflowSweeper.unack(workflowModel, 50);
        Mockito.verify(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(true, result >= 30 && result <= 60);
    }
}