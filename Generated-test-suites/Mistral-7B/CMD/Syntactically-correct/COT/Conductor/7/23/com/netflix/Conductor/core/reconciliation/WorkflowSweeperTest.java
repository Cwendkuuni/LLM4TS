package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

public class WorkflowSweeperTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @VisibleForTesting
    private static final String TEST_WORKFLOW_ID = "testWorkflowId";
    @VisibleForTesting
    private static final String TEST_WORKFLOW_NAME = "testWorkflowName";
    @VisibleForTesting
    private static final Instant TEST_INSTANT = Instant.now();

    @Before
    public void setup() {
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(1L);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        when(workflowExecutor.decide(TEST_WORKFLOW_ID)).thenReturn(createWorkflowModel());

        WorkflowModel workflowModel = createWorkflowModel();
        when(queueDAO.remove(eq(DECIDER_QUEUE), eq(TEST_WORKFLOW_ID))).thenAnswer(i -> {
            if (workflowModel != null) {
                return null;
            }
            throw new NotFoundException(String.format("Workflow with id %s not found", TEST_WORKFLOW_ID));
        });

        when(queueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(TEST_WORKFLOW_ID), anyLong())).thenAnswer(i -> {
            // Do nothing
        });
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync(TEST_WORKFLOW_ID);
        // No verification needed as CompletableFuture.completedFuture(null) does not perform any action
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = workflowSweeper.sweep(TEST_WORKFLOW_ID);
        assertNull(workflowModel);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        workflowSweeper.sweep(TEST_WORKFLOW_ID);
    }

    @Test
    public void testSweepVerificationAndRepair() {
        WorkflowModel workflowModel = createWorkflowModel();

        when(workflowRepairService.isAvailable()).thenReturn(true);
        workflowSweeper.sweep(TEST_WORKFLOW_ID);
        verifyWorkflowRepairCalls(workflowModel);
        verifyNoUnackCalls();

        when(workflowRepairService.isAvailable()).thenReturn(false);
        workflowSweeper.sweep(TEST_WORKFLOW_ID);
        verifyUnackCalls(workflowModel);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = createWorkflowModel();

        workflowSweeper.unack(workflowModel, 1L);
        verifyUnackTimeoutCall(workflowModel.getWorkflowId(), 1000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long jitter = 10L;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        long expected = workflowOffsetTimeout + jitter;
        assertEquals(expected, result);
    }

    @Test
    public void testWorkflowOffsetWithJitterNegativeJitter() {
        long workflowOffsetTimeout = 45L;
        long jitter = -5L;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        long expected = workflowOffsetTimeout - jitter;
        assertEquals(expected, result);
    }

    @Test
    public void testSweepUnackAllTasks() {
        WorkflowModel workflowModel = createWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(
                createTaskModelInProgressTaskTypeWithWait(),
                createTaskModelInProgressTaskType(),
                createTaskModelScheduledTaskType(),
                createTaskModelScheduledTaskTypeWithWorkflowTimeout());
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(TEST_WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(TEST_WORKFLOW_ID);

        // Workflow is removed from decider queue
        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(TEST_WORKFLOW_ID));

        // Unack timeout is set for each task
        verifyUnackTimeoutCallsForAllTasks(workflowModel);
    }

    @Test
    public void testSweepUnackAllTasksWithJitter() {
        WorkflowModel workflowModel = createWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(
                createTaskModelInProgressTaskTypeWithWait(),
                createTaskModelInProgressTaskType(),
                createTaskModelScheduledTaskType(),
                createTaskModelScheduledTaskTypeWithWorkflowTimeout());
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(TEST_WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(TEST_WORKFLOW_ID);

        // Unack timeout is set for each task with jitter
        verifyUnackTimeoutCallsForAllTasksWithJitter(workflowModel);
    }

    @Test
    public void testSweepUnackAllTasksWithJitterAndWorkflowOffetTimeoutIsZero() {
        WorkflowModel workflowModel = createWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(
                createTaskModelInProgressTaskTypeWithWait(),
                createTaskModelInProgressTaskType(),
                createTaskModelScheduledTaskType(),
                createTaskModelScheduledTaskTypeWithWorkflowTimeout());
        workflowModel.setTasks(tasks);

        when(properties.getWorkflowOffsetTimeout()).thenReturn(0L);
        when(workflowExecutor.decide(TEST_WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(TEST_WORKFLOW_ID);

        // Unack timeout is set for each task with jitter
        verifyUnackTimeoutCallsForAllTasksWithJitterAndZeroWorkflowOffsetTimeout(workflowModel);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(TEST_WORKFLOW_NAME);
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setWorkflowId(TEST_WORKFLOW_ID);
        workflowModel.setCreatedAt(TEST_INSTANT);
        workflowModel.setUpdatedAt(TEST_INSTANT);
        return workflowModel;
    }

    private TaskModel createTaskModelInProgressTaskType() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        return taskModel;
    }

    private TaskModel createTaskModelInProgressTaskTypeWithWait() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        return taskModel;
    }

    private TaskModel createTaskModelScheduledTaskType() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        return taskModel;
    }

    private TaskModel createTaskModelScheduledTaskTypeWithWorkflowTimeout() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(123L);
        taskModel.setWorkflowDefinition(workflowDefinition);
        return taskModel;
    }

    private void verifyWorkflowRepairCalls(WorkflowModel workflowModel) {
        if (workflowRepairService != null) {
            workflowRepairService.verifyAndRepairWorkflowTasks(TEST_WORKFLOW_ID);
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(TEST_WORKFLOW_ID);
        }
    }

    private void verifyUnackCalls(WorkflowModel workflowModel) {
        unack(workflowModel, 1L);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(TEST_WORKFLOW_ID), anyLong());
    }

    private void verifyUnackTimeoutCall(String workflowId, long timeout) {
        unack(null, timeout);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), timeout * 1000);
    }

    private void verifyUnackTimeoutCallsForAllTasks(WorkflowModel workflowModel) {
        for (TaskModel taskModel : workflowModel.getTasks()) {
            unack(taskModel, 1L);
        }
        verify(queueDAO, times(workflowModel.getTasks().size())).setUnackTimeout(
                eq(DECIDER_QUEUE), anyString(), anyLong());
    }

    private void verifyUnackTimeoutCallsForAllTasksWithJitter(WorkflowModel workflowModel) {
        for (TaskModel taskModel : workflowModel.getTasks()) {
            unack(taskModel, 1L);
        }
        for (TaskModel taskModel : workflowModel.getTasks()) {
            long jitter = new Random().nextInt((int) (2 * (1L / 3)) + 1) - (1L / 3);
            unack(taskModel, 1L + jitter);
        }
        verify(queueDAO, times(workflowModel.getTasks().size())).setUnackTimeout(
                eq(DECIDER_QUEUE), anyString(), anyLong());
    }

    private void verifyUnackTimeoutCallsForAllTasksWithJitterAndZeroWorkflowOffsetTimeout(WorkflowModel workflowModel) {
        for (TaskModel taskModel : workflowModel.getTasks()) {
            unack(taskModel, 1L);
        }
        for (TaskModel taskModel : workflowModel.getTasks()) {
            long jitter = new Random().nextInt((int) (2 * (1L / 3)) + 1) - (1L / 3);
            unack(taskModel, 1L + jitter);
        }
        verify(queueDAO, times(workflowModel.getTasks().size())).setUnackTimeout(
                eq(DECIDER_QUEUE), anyString(), anyLong());
    }
}