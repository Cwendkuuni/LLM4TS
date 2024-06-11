import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";

        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
        when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenReturn(null);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertNotNull(result);
        assertEquals(future, result);
    }

    @Test
    public void testSweepAsync_withNullWorkflowId() {
        String workflowId = null;

        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

        when(workflowExecutor.decide(anyString())).thenReturn(null);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenReturn(null);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertNotNull(result);
        assertEquals(future, result);
    }

    @Test
    public void testSweepAsync_withNullWorkflowRepairService() {
        String workflowId = "testWorkflowId";

        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
        when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenReturn(null);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.empty(), properties, queueDAO);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertNotNull(result);
        assertEquals(future, result);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.getTasks().add(new TaskModel());

        workflowSweeper.unack(workflowModel, 60);

        // Add assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= 30 && result <= 60);
    }
}