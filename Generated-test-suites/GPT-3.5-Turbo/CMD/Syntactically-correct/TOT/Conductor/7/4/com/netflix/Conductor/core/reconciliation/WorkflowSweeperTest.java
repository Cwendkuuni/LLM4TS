import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties properties;

    @Test
    public void testSweepAsync() throws InterruptedException, ExecutionException {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        
        CompletableFuture<Void> completableFuture = workflowSweeper.sweepAsync("test_workflow_id");
        completableFuture.get();
        
        verify(workflowExecutor, times(1)).decide("test_workflow_id");
        verify(queueDAO, times(1)).remove("DECIDER_QUEUE", "test_workflow_id");
    }

    @Test
    public void testSweepAsyncWithException() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        
        when(workflowExecutor.decide(anyString())).thenThrow(new Exception());
        
        workflowSweeper.sweepAsync("test_workflow_id");
        
        verify(queueDAO, times(1)).remove("DECIDER_QUEUE", "test_workflow_id");
    }

    @Test
    public void testSweep() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        workflowSweeper.sweep("test_workflow_id");

        verify(workflowExecutor, times(1)).decide("test_workflow_id");
    }

    @Test
    public void testSweepWithNotFoundException() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        doThrow(new NotFoundException()).when(queueDAO).remove("DECIDER_QUEUE", "test_workflow_id");

        workflowSweeper.sweep("test_workflow_id");

        verify(queueDAO, times(1)).remove("DECIDER_QUEUE", "test_workflow_id");
    }

    @Test
    public void testUnack() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn("test_workflow_id");

        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1)).setUnackTimeout("DECIDER_QUEUE", "test_workflow_id", 60000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        long result = workflowSweeper.workflowOffsetWithJitter(60);

        assertTrue(result >= 45 && result <= 75);
    }
}