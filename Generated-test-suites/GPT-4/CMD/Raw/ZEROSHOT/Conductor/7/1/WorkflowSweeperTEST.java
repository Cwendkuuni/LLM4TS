
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    ConductorProperties properties;

    @Mock
    QueueDAO queueDAO;

    @InjectMocks
    WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWorkflowSweeper() throws Exception {
        when(workflowRepairService.isPresent()).thenReturn(true);
        when(workflowRepairService.get().verifyAndRepairWorkflowTasks(any())).thenReturn(Optional.empty());

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);
        when(workflowExecutor.decide(any())).thenReturn(workflowModel);
        when(properties.getWorkflowOffsetTimeout()).thenReturn(java.time.Duration.ofSeconds(60));

        workflowSweeper.sweep("workflowId");

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowId", anyLong());
        verify(workflowRepairService.get()).verifyAndRepairWorkflowTasks("workflowId");
    }

    @Test
    public void testWorkflowSweeperAsync() throws Exception {
        doNothing().when(workflowSweeper).sweep(any());
        workflowSweeper.sweepAsync("workflowId");

        verify(workflowSweeper).sweep("workflowId");
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn("workflowId");

        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));

        workflowSweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowId", 10000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long result = workflowSweeper.workflowOffsetWithJitter(10L);
        assertTrue(result >= 7L);
        assertTrue(result <= 13L);
    }
}
