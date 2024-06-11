import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        when(workflowRepairService.verifyAndRepairWorkflowTasks("testWorkflowId")).thenReturn(CompletableFuture.completedFuture(null));
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");
        future.join();
        // assert completion of async method
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("testWorkflowId");
        // assert functionality of sweep method
    }

    @Test
    public void testUnack() {
        workflowSweeper.unack(new WorkflowModel(), 60);
        // assert functionality of unack method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(45);
        // assert functionality of workflowOffsetWithJitter method
        assertEquals(30, jitter, 15); // Checking the range of jitter +- (1/3)
    }
}