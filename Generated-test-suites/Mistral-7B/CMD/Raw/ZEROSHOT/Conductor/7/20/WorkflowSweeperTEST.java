package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final String APPLICATION_ID = "applicationId";
    private static final String DECIDER_QUEUE = "deciderQueue";

    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSweeper workflowSweeper;
    private QueueDAO queueDAO;
    private Monitors monitors;

    @Before
    public void setup() {
        conductorProperties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        monitors = Mockito.mock(Monitors.class);

        WorkflowContext workflowContext = new WorkflowContext(APPLICATION_ID);
        when(conductorProperties.getAppId()).thenReturn(APPLICATION_ID);
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(10L);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel());

        WorkflowSweeper workflowSweeper =
                new WorkflowSweeper(workflowExecutor, Mockito.mock(WorkflowRepairService.class), conductorProperties, queueDAO);
        this.workflowSweeper = workflowSweeper;
    }

    @Test
    public void givenWorkflowNotFound_whenSweeping_thenWorkflowIsRemoved() {
        doThrow(new NotFoundException(WORKFLOW_ID)).when(workflowExecutor).decide(WORKFLOW_ID);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void givenWorkflowIsTerminal_whenSweeping_thenWorkflowIsRemoved() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.TERMINATED);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void givenWorkflowIsNotTerminal_whenSweeping_thenWorkflowIsNotRemoved() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void givenWorkflowIsNotTerminal_whenSweepingAsync_thenTaskIsNotRemoved() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(WORKFLOW_ID);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
        assertFalse(result.join());
    }

    @Test(expected = IllegalStateException.class)
    public void givenWorkflowExecutorIsNull_whenInitializing_thenExceptionIsThrown() {
        WorkflowSweeper workflowSweeper =
                new WorkflowSweeper(null, Mockito.mock(WorkflowRepairService.class), conductorProperties, queueDAO);
    }

    @Test
    public void givenVerifyAndRepairServiceIsNotNull_whenInitializing_thenServiceIsUsed() {
        WorkflowRepairService workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        WorkflowSweeper workflowSweeper =
                new WorkflowSweeper(workflowExecutor, workflowRepairService, conductorProperties, queueDAO);

        verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void givenVerifyAndRepairServiceIsNull_whenInitializing_thenServiceIsNotUsed() {
        WorkflowSweeper workflowSweeper =
                new WorkflowSweeper(workflowExecutor, null, conductorProperties, queueDAO);

        verify(workflowRepairService, never()).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void givenWorkflowWithInProgressTask_whenUnack_thenTimeoutIsSet() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_SCRIPT);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel().tasks(taskModel));

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void givenWorkflowWithScheduledTask_whenUnack_thenTimeoutIsSet() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefinition(Mockito.mock(TaskDef.class));

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel().tasks(taskModel));

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void givenWorkflowWithTimeoutTask_whenUnack_thenTimeoutIsSet() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel().tasks(taskModel));

        long timeout = 5000L;
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, timeout + 1L);
    }

    @Test
    public void givenWorkflowWithLongTimeoutTask_whenUnack_thenTimeoutIsSet() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_SCRIPT);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel().tasks(taskModel));

        long timeout = 60000L;
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, timeout + 1L);
    }

    @Test
    public void givenWorkflowWithNoTasks_whenUnack_thenNothingHappens() {
        WorkflowModel workflowModel = new WorkflowModel();

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void givenWorkflowWithNullTasks_whenUnack_thenNothingHappens() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(null);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void givenWorkflowWithNullWorkflowModel_whenUnack_thenNothingHappens() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(null);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void givenWorkflowWithWorkflowName_whenSweeping_thenWorkflowNameIsLogged() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel());

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(LOGGER, times(1)).info("Running sweeper for workflow {}", WORKFLOW_ID);
    }

    @Test
    public void givenWorkflowWithNoLogging_whenSweeping_thenWorkflowNameIsNotLogged() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel());
        Logger logger = Mockito.mock(Logger.class);
        when(LOGGER).thenReturn(logger);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, null, conductorProperties, queueDAO) {
            @Override
            private static final Logger LOGGER = logger;
        };

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(logger, never()).info("Running sweeper for workflow {}", WORKFLOW_ID);
    }

    @Test
    public void givenWorkflowWithNullWorkflowId_whenSweeping_thenExceptionIsThrown() {
        when(workflowExecutor.decide(null)).thenCallRealMethod();

        assertThrows(IllegalArgumentException.class, () -> workflowSweeper.sweep(null));
    }

    @Test
    public void givenWorkflowWithNullWorkflowId_whenSweepingAsync_thenExceptionIsThrown() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(null);

        assertThrows(IllegalArgumentException.class, () -> result.join());
    }

    @Test
    public void givenWorkflowWithNullWorkflowId_whenSweepingWithAsyncResult_thenExceptionIsThrown() {
        CompletableFuture<AsyncResult<Void>> result = new CompletableFuture<>();
        result.complete(new AsyncResult<>(null));

        assertThrows(IllegalArgumentException.class, () -> workflowSweeper.sweepAsync(null, result));
    }

    @Test
    public void givenWorkflowWithNullWorkflowId_whenSweepingWithAsyncResultAndError_thenExceptionIsThrown() {
        CompletableFuture<AsyncResult<Void>> result = new CompletableFuture<>();
        result.completeExceptionally(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> workflowSweeper.sweepAsync(null, result));
    }
}