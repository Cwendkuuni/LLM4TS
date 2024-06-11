import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() {
        String workflowId = "123";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.join();
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "456";
        when(workflowExecutor.decide(workflowId)).thenReturn(null);
        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }
  
    // Add more test cases for other methods in WorkflowSweeper class

}