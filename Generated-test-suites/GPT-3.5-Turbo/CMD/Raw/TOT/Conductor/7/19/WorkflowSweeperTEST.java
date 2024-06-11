import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.CompletableFuture;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove("deciderQueue", workflowId);
    }

    @Test
    public void testSweepAsyncNotFound() {
        String workflowId = "nonExistingWorkflowId";
        doThrow(new NotFoundException()).when(workflowExecutor).decide(workflowId);
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
        verify(queueDAO).remove("deciderQueue", workflowId);
    }

    @Test
    public void testSweepAsyncException() {
        String workflowId = "exceptionWorkflowId";
        doThrow(new RuntimeException()).when(workflowExecutor).decide(workflowId);
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
        verify(queueDAO, never()).remove("deciderQueue", workflowId);
    }

    // Add more test cases for sweep method covering typical use cases, edge cases, and error scenarios

}