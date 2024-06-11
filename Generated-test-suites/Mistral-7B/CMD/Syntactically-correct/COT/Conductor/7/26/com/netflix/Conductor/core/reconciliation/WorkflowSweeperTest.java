package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.TimeoutStrategy;
import com.netflix.conductor.test.TestUtils;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@Component
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflowId";
        sweeper.sweepAsync(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "workflowId";
        WorkflowModel workflow = TestUtils.createWorkflowModel();

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflow));
        when(properties.getWorkflowOffsetTimeout()).thenReturn(TimeoutStrategy.of(10L));

        sweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        if (workflowRepairService != null) {
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        }
        verify(workflowExecutor).decide(workflowId);

        if (workflow != null) {
            verify(monitors).recordUnackTime(eq(workflow.getWorkflowName()), anyLong());
        } else {
            verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
        }
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFoundException() {
        String workflowId = "workflowId";

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        sweeper.sweep(workflowId);
    }

    @Test(expected = Exception.class)
    public void testSweepException() {
        String workflowId = "workflowId";
        WorkflowModel workflow = TestUtils.createWorkflowModel();

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflow));

        doThrow(new RuntimeException("Test Exception")).when(workflowExecutor).decide(workflowId);

        sweeper.sweep(workflowId);

        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testUnack() {
        String workflowId = "workflowId";
        WorkflowModel workflow = TestUtils.createWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(TestUtils.createTaskModel(), TestUtils.createTaskModel());
        workflow.setTasks(tasks);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflow));
        when(properties.getWorkflowOffsetTimeout()).thenReturn(TimeoutStrategy.of(10L));

        sweeper.unack(workflow);

        for (TaskModel taskModel : tasks) {
            if (taskModel.getStatus() == Status.IN_PROGRESS) {
                long postponeDurationSeconds =
                        taskModel.getTaskType().equals(TaskType.TASK_TYPE_WAIT)
                                || taskModel.getTaskType().equals(TaskType.TASK_TYPE_HUMAN)
                                ? taskModel.getWaitTimeout() + 1
                                : taskModel.getResponseTimeoutSeconds() + 1;
                verify(queueDAO).setUnackTimeout(
                        DECIDER_QUEUE, workflow.getWorkflowId(), postponeDurationSeconds * 1000);
                break;
            }
        }
    }

    @Test
    public void testUnackWorkflowNotFound() {
        String workflowId = "workflowId";

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        sweeper.unack(workflow);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        long jitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= (workflowOffsetTimeout / 3));
        assertTrue(jitter <= (workflowOffsetTimeout * 1.5));
    }
}