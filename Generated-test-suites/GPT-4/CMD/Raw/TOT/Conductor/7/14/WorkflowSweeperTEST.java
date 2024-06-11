
import static org.junit.Assert.*;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;
    
    @Mock
    private WorkflowRepairService workflowRepairService;
    
    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");
        assertNotNull(future);
    }
    
    @Test
    public void testSweep_nullWorkflow() {
        String workflowId = "workflowId";
        when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());
        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(2)).remove(anyString(), anyString());
        // One for removing the DECIDER_QUEUE with the workflowId and once when the NotFoundException is caught
    }
    
    @Test
    public void testSweep_workflowFound() {
        String workflowId = "workflowId";
        when(workflowExecutor.decide(workflowId)).thenReturn(Optional.of(new WorkflowModel()));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void workflowOffsetWithJitter() {
        long jitteredValue = workflowSweeper.workflowOffsetWithJitter(30);
        assertTrue(jitteredValue >= 10 && jitteredValue <= 50);
    }

}
