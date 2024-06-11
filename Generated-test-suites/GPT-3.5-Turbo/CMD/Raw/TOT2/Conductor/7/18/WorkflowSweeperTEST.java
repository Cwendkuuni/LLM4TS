import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sweepAsyncTest() {
        String workflowId = "123";
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

        when(workflowSweeper.sweepAsync(any(String.class))).thenReturn(future);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertNotNull(result);
    }

    @Test
    public void sweepTest() {
        String workflowId = "123";

        doNothing().when(workflowSweeper).sweep(workflowId);

        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void unackTest() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("123");
        workflowModel.setStatus(Status.IN_PROGRESS);

        long workflowOffsetTimeout = 60;

        doNothing().when(workflowSweeper).unack(workflowModel, workflowOffsetTimeout);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long workflowOffsetTimeout = 45;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= 30 && result <= 60);
    }
}