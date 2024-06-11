import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class WorkflowSweeperTest {

    @Test
    public void testSweepAsync() {
        WorkflowExecutor workflowExecutorMock = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairServiceMock = mock(WorkflowRepairService.class);
        QueueDAO queueDAOMock = mock(QueueDAO.class);
        ConductorProperties properties = new ConductorProperties();

        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutorMock, Optional.of(workflowRepairServiceMock), properties, queueDAOMock);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");

        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        WorkflowExecutor workflowExecutorMock = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairServiceMock = mock(WorkflowRepairService.class);
        QueueDAO queueDAOMock = mock(QueueDAO.class);
        ConductorProperties properties = new ConductorProperties();

        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutorMock, Optional.of(workflowRepairServiceMock), properties, queueDAOMock);

        workflowSweeper.sweep("workflowId");

        verify(workflowExecutorMock, times(1)).decide("workflowId");
        verify(queueDAOMock, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testUnack() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(null, null, null, null);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");

        workflowSweeper.unack(workflowModel, 60L);

        // Test unack functionality based on workflow model status
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(null, null, null, null);

        long workflowOffsetTimeout = 45L;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Test workflow offset with jitter functionality
    }

}