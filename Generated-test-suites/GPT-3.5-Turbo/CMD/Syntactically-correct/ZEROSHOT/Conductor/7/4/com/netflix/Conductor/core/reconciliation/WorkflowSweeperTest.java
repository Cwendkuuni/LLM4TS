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

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "12345";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        String workflowId = "12345";
        workflowSweeper.sweep(workflowId);

        // Verify certain methods were called
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 60;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        // Verify queueDAO method was called
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), (workflowModel.getWorkflowDefinition().getTimeoutSeconds() + 1) * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Check if result is within expected range
        assertTrue(result >= 30 && result <= 60);
    }

}