
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {
    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowModel workflowModel;

    private static final String WORKFLOW_ID = "testWorkflowId";
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService),
                properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        doNothing().when(workflowSweeper).sweep(WORKFLOW_ID);
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(WORKFLOW_ID);
        assertNull(result.get());
        Mockito.verify(workflowSweeper, times(1)).sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweep() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        doNothing().when(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        workflowSweeper.sweep(WORKFLOW_ID);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(1);
        TaskModel[] taskModels = {taskModel};
        when(workflowModel.getTasks()).thenReturn(new ArrayList<>(Arrays.asList(taskModels)));
        workflowSweeper.unack(workflowModel, 0L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= (workflowOffsetTimeout - workflowOffsetTimeout / 3) && 
                result <= (workflowOffsetTimeout + workflowOffsetTimeout / 3));
    }
}
