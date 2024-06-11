import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowSweeper workflowSweeper;

    private final String workflowId = "workflowId";

    @Before
    public void setup() {
        Mockito.when(workflowSweeper.workflowOffsetWithJitter(Mockito.anyLong())).thenCallRealMethod();
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        Mockito.verify(workflowSweeper, Mockito.times(1)).sweep(workflowId);
        assertEquals(null, result.join());
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep(workflowId);

        Mockito.verify(workflowSweeper, Mockito.times(1)).unack(Mockito.any(), Mockito.anyLong());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        long workflowOffsetTimeout = 60;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        // Test implementations of unack method here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Instant now = Instant.now();
        long currentTime = now.toEpochMilli() / 1000;
        assertEquals(workflowOffsetTimeout, result, 15);

    }

}