package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
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
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.testutils.TestWorkflowModel;
import com.netflix.conductor.testutils.TestWorkflowModel.TestTaskModel;

public class WorkflowSweeperTest {

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("test-app-id");
        when(workflowExecutor.decide(anyString())).thenReturn(new TestWorkflowModel());
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyInt()))
                .thenReturn(true);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(workflowRepairService);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "test-workflow-id";
        sweeper.sweepAsync(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = new TestWorkflowModel();
        sweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), anyInt());

        if (workflowRepairService != null) {
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        }

        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testSweepNotFoundException() {
        String workflowId = "test-workflow-id";
        doThrow(new NotFoundException("Test NotFoundException"))
                .when(workflowExecutor).decide(workflowId);

        sweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, anyInt());
        verify(monitors).error(WorkflowSweeper.CLASS_NAME, "sweep");
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new TestWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(new TestTaskModel(), new TestTaskModel());
        workflowModel.setTasks(tasks);

        sweeper.unack(workflowModel, 1000);

        for (TaskModel taskModel : tasks) {
            if (taskModel.getTaskType().equals(TaskType.TASK_TYPE_WAIT)
                    || taskModel.getTaskType().equals(TaskType.TASK_TYPE_HUMAN)) {
                verify(queueDAO, atLeastOnce())
                        .setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(),
                                eq(1001L));
                return;
            }
        }

        fail("Should have found a task model with TaskType.TASK_TYPE_WAIT or TaskType.TASK_TYPE_HUMAN.");
    }

    @Test
    public void testUnackTaskDefinition() {
        WorkflowModel workflowModel = new TestWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(new TestTaskModel(), new TestTaskModel());
        workflowModel.setTasks(tasks);

        TaskDef taskDef = new TaskDef();
        taskDef.setPollTimeoutSeconds(5L);

        doReturn(taskDef).when(tasks.get(0)).getTaskDefinition();

        sweeper.unack(workflowModel, 1000);

        verify(queueDAO, atLeastOnce())
                .setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), eq(5101L));
    }

    @Test
    public void testUnackWorkflowDefinition() {
        WorkflowModel workflowModel = new TestWorkflowModel();
        List<TaskModel> tasks = Collections.emptyList();
        workflowModel.setTasks(tasks);

        sweeper.unack(workflowModel, 1000);

        verify(queueDAO, atLeastOnce())
                .setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), eq(1001L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 1000;

        long actual = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long min = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
        long max = workflowOffsetTimeout + (workflowOffsetTimeout / 3);

        assertTrue(actual >= min);
        assertTrue(actual <= max);
    }
}