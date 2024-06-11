import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties conductorProperties;
    
    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
    }

    @Test
    public void sweepAsync_shouldSweepWorkflowAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> completableFuture = workflowSweeper.sweepAsync(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(eq(WorkflowSweeper.DECIDER_QUEUE), eq(workflowId));
        verify(queueDAO, times(1)).setUnackTimeout(eq(WorkflowSweeper.DECIDER_QUEUE), eq(workflowId), anyLong());
        // Add more verifications as needed
    }

    @Test
    public void sweep_shouldSweepWorkflow() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        // Add more verifications as needed
    }

    @Test
    public void unack_shouldPostponeTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 60;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        // Add assertions for postponed tasks
    }

    @Test
    public void workflowOffsetWithJitter_shouldReturnJitteredTime() {
        long workflowOffsetTimeout = 45;
        long jitteredTime = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        // Add assertions for jittered time
    }
}