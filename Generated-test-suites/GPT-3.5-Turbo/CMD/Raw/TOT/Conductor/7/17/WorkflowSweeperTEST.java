import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@EnableAsync
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
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        Mockito.when(properties.getAppId()).thenReturn("testApp");
    }

    @Test
    public void testWorkflowSweeperSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = sweeper.sweepAsync(workflowId);
        assertEquals(AsyncResult.class, future.getClass());
    }

    @Test
    public void testWorkflowSweeperSweep() {
        String workflowId = "testWorkflowId";
        sweeper.sweep(workflowId);
        // Assert any necessary behavior here
    }

    @Test
    public void testWorkflowSweeperUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 60;
        sweeper.unack(workflowModel, workflowOffsetTimeout);
        // Assert any necessary behavior here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        // Assert any necessary behavior here
    }
}