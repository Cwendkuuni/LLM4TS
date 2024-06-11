
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;


public class WorkflowSweeperTest {
     
    @Mock
    WorkflowExecutor workflowExecutor;
    
    @Mock
    Optional<WorkflowRepairService> workflowRepairService;
    
    @Mock
    ConductorProperties properties;
    
    @Mock
    QueueDAO queueDAO;

    @InjectMocks
    WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {

        // Given a workflowId
        String workflowId = "workflow_1";

        // When sweepAsync function called
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        // Then It should return CompletableFuture with null value
        Assert.assertNull(result.get());
    }
}
