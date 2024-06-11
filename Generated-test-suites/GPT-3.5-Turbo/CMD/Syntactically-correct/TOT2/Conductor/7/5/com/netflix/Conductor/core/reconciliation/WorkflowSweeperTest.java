import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(mock(WorkflowExecutor.class), Optional.empty(), mock(ConductorProperties.class), mock(QueueDAO.class));
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("testWorkflowId");
        // Verify appropriate actions are taken when sweeping a workflow
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getTasks()).thenReturn(new ArrayList<>());

        workflowSweeper.unack(workflowModel, 60L);
        // Verify the unack method handles scenarios appropriately
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45L);
        assertTrue(result >= 30 && result <= 60);
    }

    // Add more test cases for edge cases and error scenarios

}