
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    private WorkflowExecutor workflowExecutorMock = mock(WorkflowExecutor.class);
    private WorkflowRepairService workflowRepairServiceMock = mock(WorkflowRepairService.class);
    private ConductorProperties conductorPropertiesMock = mock(ConductorProperties.class);
    private QueueDAO queueDAOMock = mock(QueueDAO.class);
    private WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutorMock, Optional.of(workflowRepairServiceMock), conductorPropertiesMock, queueDAOMock);

    @Test
    public void testSweepAsync() {
        final String workflowId = "testWorkflow";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        final String workflowId = "testWorkflow";
        when(workflowExecutorMock.decide(anyString())).thenReturn(new WorkflowModel());
        workflowSweeper.sweep(workflowId);
        verify(workflowRepairServiceMock, times(1)).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepWithNotFoundException() {
        final String workflowId = "testWorkflow";
        when(workflowExecutorMock.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));
        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        TaskModel taskModelMock = mock(TaskModel.class);
        when(workflowModelMock.getTasks()).thenReturn(Arrays.asList(taskModelMock));
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        workflowSweeper.unack(workflowModelMock, 30L);
        verify(queueDAOMock, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30L && result <= 60L);
    }
}
