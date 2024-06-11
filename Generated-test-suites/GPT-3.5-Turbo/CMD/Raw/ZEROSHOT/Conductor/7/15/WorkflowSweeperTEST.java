import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        ConductorProperties properties = new ConductorProperties();
        QueueDAO queueDAO = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService),
                properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("testWorkflowId");
        // Add test assertions here based on the functionality of the sweep method
    }

    @Test
    public void testUnack() {
        // Add test assertions here based on the functionality of the unack method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        // Add test assertions here based on the functionality of the workflowOffsetWithJitter method
    }

}