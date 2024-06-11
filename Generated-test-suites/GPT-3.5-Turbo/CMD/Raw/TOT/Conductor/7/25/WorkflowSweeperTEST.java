import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws InterruptedException, ExecutionException, TimeoutException {
        String workflowId = "123";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        assertNull(future.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testSweep() {
        String workflowId = "123";
        workflowSweeper.sweep(workflowId);

        // Add your assertion here
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 60;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        // Add your assertion here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Add your assertion here
    }
}