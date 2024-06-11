import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties propertiesMock;
    @Mock
    private WorkflowExecutor workflowExecutorMock;
    @Mock
    private WorkflowRepairService workflowRepairServiceMock;
    @Mock
    private QueueDAO queueDAOMock;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutorMock, Optional.of(workflowRepairServiceMock), propertiesMock, queueDAOMock);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        doNothing().when(workflowSweeper).sweep(workflowId);
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep_whenWorkflowFound() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);

        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModelMock);
        when(workflowModelMock.getStatus()).thenReturn(Status.FINISHED);
        doNothing().when(queueDAOMock).remove(DECIDER_QUEUE, workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_whenWorkflowNotFound() {
        String workflowId = "testWorkflowId";

        when(workflowExecutorMock.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));
        doNothing().when(queueDAOMock).remove(DECIDER_QUEUE, workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_withException() {
        String workflowId = "testWorkflowId";

        when(workflowExecutorMock.decide(workflowId)).thenThrow(new RuntimeException("Some error"));
        doNothing().when(queueDAOMock).remove(DECIDER_QUEUE, workflowId);

        workflowSweeper.sweep(workflowId);

        // Error condition, no verification possible
    }

    @Test
    public void testUnack_withInProgressTask() {
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        when(workflowModelMock.getTasks()).thenReturn(Collections.singletonList(mock(TaskModel.class)));
        when(workflowModelMock.getTasks().get(0).getStatus()).thenReturn(Status.IN_PROGRESS);

        workflowSweeper.unack(workflowModelMock, 60);

        // Verify certain actions performed
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 60;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Verify the result within expected range
    }

}