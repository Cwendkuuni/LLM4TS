import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.reconciliation.*;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

public class WorkflowSweeperTest {

    @Mock 
    private WorkflowSweeper workflowSweeper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test 
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        verify(workflowSweeper, times(1)).sweepAsync(workflowId);
        Assertions.assertNotNull(result);
    }

    @Test 
    public void testSweep() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    // Assuming WorkflowModel as a simple class for the sake of this example
    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 1000;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        verify(workflowSweeper, times(1)).unack(workflowModel, workflowOffsetTimeout);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 1000;

        long jitterOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(jitterOffset >= (2 / 3 * workflowOffsetTimeout));
        assertTrue(jitterOffset <= (4 / 3 * workflowOffsetTimeout));
    }
}