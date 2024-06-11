import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    // Test for sweep method when WorkflowModel is not found
    @Test
    public void testSweepWorkflowModelNotFound() {
        String workflowId = "123";
        WorkflowModel workflowModel = null;
        doReturn(null).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    // Test for sweep method to verify and repair tasks
    @Test
    public void testSweepVerifyAndRepairTasks() {
        String workflowId = "123";
        WorkflowModel workflowModel = new WorkflowModel();
        doReturn(workflowModel).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
    }

    // Test for sweep method when workflow is in terminal status
    @Test
    public void testSweepWorkflowTerminalStatus() {
        String workflowId = "123";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.COMPLETED);
        doReturn(workflowModel).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    // Test for sweep method with NotFoundException
    @Test
    public void testSweepWithNotFoundException() {
        String workflowId = "123";
        doThrow(new NotFoundException()).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    // Test for Workflow offset with jitter
    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(jitter, workflowOffsetTimeout + 15, 20); // Checking if jitter is within range [30-60]
    }

    // Test for unack method
    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setWaitTimeout(30);
        workflowModel.setTasks(List.of(taskModel));

        workflowSweeper.unack(workflowModel, 45);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), eq(31 * 1000));
    }

    // Test for async sweep method
    @Test
    public void testSweepAsync() {
        String workflowId = "123";
        CompletableFuture<Void> completableFuture = workflowSweeper.sweepAsync(workflowId);
        try {
            completableFuture.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            fail("Execution timed out");
        }
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

}