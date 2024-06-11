import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private WorkflowModel workflowModel;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertEquals(CompletableFuture.completedFuture(null), result);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenReturn(null);
        workflowSweeper.sweep(workflowId);
        // Additional assertions can be added as needed
    }

    @Test
    public void testUnack() {
        long workflowOffsetTimeout = 30;
        when(workflowModel.getTasks()).thenReturn(new ArrayList<>());
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        // Additional assertions can be added as needed
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        // Assert that the result is within the expected range [30-60]
        assertTrue(result >= 30 && result <= 60);
    }
}