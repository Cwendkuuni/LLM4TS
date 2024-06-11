import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        ConductorProperties properties = mock(ConductorProperties.class);
        QueueDAO queueDAO = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("testWorkflowId");
        // Add assertions here
    }

    @Test
    public void testUnack() {
        // Mocking WorkflowModel
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn("testWorkflowId");

        workflowSweeper.unack(workflowModel, 60L);
        // Add assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45L);
        // Add assertions here
    }

}