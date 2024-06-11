import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private ConductorProperties properties;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(new Random(1L));
        when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(45);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflow-1";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper).sweep(workflowId);
        result.join();
        verifyZeroInteractions(workflowExecutor, workflowRepairService, queueDAO, monitors);
    }

    @Test
    public void testSweep() {
        String workflowId = "workflow-1";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setStatus(Status.RUNNING);

        doReturn(workflow).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyZeroInteractions(workflowExecutor, workflowRepairService, monitors);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        String workflowId = "workflow-1";
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verifyZeroInteractions(workflowExecutor, workflowRepairService);
    }

    @Test
    public void testSweepWithNullWorkflow() {
        String workflowId = "workflow-1";
        doReturn(null).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, 46L * 1000);
        verifyZeroInteractions(workflowExecutor, workflowRepairService, monitors);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflow-1");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setTaskId("task-1").setStatus(Status.IN_PROGRESS),
                new TaskModel().setTaskId("task-2").setStatus(Status.SCHEDULED),
                new TaskModel().setTaskId("task-3").setStatus(Status.TERMINAL));
        workflowModel.setTasks(tasks);

        workflowSweeper.unack(workflowModel, 45L);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), 46L * 1000);
        verifyZeroInteractions(workflowExecutor, workflowRepairService, monitors);
    }

    @Test
    public void testUnackWithNullWorkflowModel() {
        workflowSweeper.unack(null, 45L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, anyString(), 46L * 1000);
        verifyZeroInteractions(workflowExecutor, workflowRepairService, monitors);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= (workflowOffsetTimeout - 15) && result <= (workflowOffsetTimeout + 15));
    }
}