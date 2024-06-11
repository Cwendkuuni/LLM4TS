
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "test-workflow-id";
        doNothing().when(workflowSweeper).sweep(any(String.class));
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.join();
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "test-workflow-id";
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, atLeastOnce()).remove(any(String.class), any(String.class));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        long workflowOffsetTimeout = 1000L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        verify(queueDAO, times(1)).setUnackTimeout(any(String.class), any(String.class), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 1000L;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assert(result >= (2.0/3)*workflowOffsetTimeout && result <= (4.0/3)*workflowOffsetTimeout);
    }
}
