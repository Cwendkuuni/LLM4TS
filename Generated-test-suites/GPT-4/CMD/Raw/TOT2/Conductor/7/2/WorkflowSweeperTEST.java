import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock 
    private WorkflowExecutor workflowExecutor;
    
    @Mock 
    private WorkflowRepairService workflowRepairService;
    
    @Mock 
    private ConductorProperties properties;

    @Mock 
    private QueueDAO queueDAO;

    @InjectMocks 
    private WorkflowSweeper workflowSweeper;

    private String workflowId = "sample_workflow_id";

    @Before 
    public void setUp() {
        // ...
    }

    @Test 
    public void sweepAsyncTest() throws ExecutionException, InterruptedException {
        doNothing().when(workflowSweeper).sweep(any(String.class));
        CompletableFuture<Void> resultFuture = workflowSweeper.sweepAsync(workflowId);

        // Assert that the CompletableFuture completes
        assertEquals(null, resultFuture.get());
        
        // verify if sweep method was called
        verify(workflowSweeper).sweep(any(String.class));
    }

    // Further testing of the other methods should follow here...
}