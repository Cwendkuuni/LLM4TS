import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {
    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    WorkflowRepairService workflowRepairService;

    @Mock
    ConductorProperties properties;

    @Mock
    QueueDAO queueDAO;
    
    WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }
    
    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        when(workflowSweeper.sweepAsync(workflowId)).thenReturn(CompletableFuture.completedFuture(null));

        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        long timeout = 5000;
        workflowSweeper.unack(workflowModel, timeout);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), timeout * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long timeout = 5000;  // 5 seconds
        long jitterOffset = workflowSweeper.workflowOffsetWithJitter(timeout);
        assertTrue("Jitter offset must be within +- range of the original timeout", jitterOffset >= (timeout - timeout/3) && jitterOffset <= (timeout + timeout/3));
    }
}