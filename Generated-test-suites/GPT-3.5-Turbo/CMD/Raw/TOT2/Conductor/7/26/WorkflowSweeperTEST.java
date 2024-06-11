import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties properties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertTrue(result.isDone());
    }

    @Test
    public void testSweep() {
        String workflowId = "workflowId";
        workflowSweeper.sweep(workflowId);
        // Assert based on the expected behavior of the sweep method
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        long workflowOffsetTimeout = 60;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        // Assert based on the expected behavior of the unack method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        // Assert based on the expected behavior of the workflowOffsetWithJitter method
        assertTrue(result >= 30 && result <= 60);
    }
}