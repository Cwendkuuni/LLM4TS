package com.netflix.conductor.core.reconciliation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel.Status;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModelImpl;

@Component
public class WorkflowSweeperTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);

    @Autowired
    private WorkflowSweeper workflowSweeper;

    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = new QueueDAOImpl();
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(120L);
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);
        when(workflowExecutor.decide(Mockito.anyString())).thenReturn(new WorkflowModelImpl());
    }

    @Test
    public void testSweepSuccess() {
        // Given
        String workflowId = "testWorkflowId";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId)).thenReturn(new WorkflowModelImpl());

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
    }

    @Test
    public void testSweepNotFound() {
        // Given
        String workflowId = "testWorkflowId";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepException() {
        // Given
        String workflowId = "testWorkflowId";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId))
                .thenThrow(new RuntimeException("Test exception"));

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifyError();
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, Utils.WORKFLOW_OFFSET_WITH_JITTER(120L) * 1000);
    }

    @Test
    public void testSweepRepairTasks() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        WorkflowRepairService repairService = mock(WorkflowRepairService.class);
        when(workflowSweeper.workflowRepairService).thenReturn(repairService);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(repairService).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testSweepWorkflowNotFound() {
        // Given
        String workflowId = "testWorkflowId";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepTaskInProgress() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        TaskModel taskModel = new TaskModel();
        workflowModel.setTasks(Arrays.asList(taskModel));
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, Utils.WORKFLOW_OFFSET_WITH_JITTER(120L) * 1000);
    }

    @Test
    public void testSweepTaskScheduled() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        workflowModel.setTasks(Arrays.asList(taskModel));
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, Utils.WORKFLOW_OFFSET_WITH_JITTER(120L) * 1000);
    }

    @Test
    public void testSweepTaskFailed() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.FAILED);
        workflowModel.setTasks(Arrays.asList(taskModel));
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepTaskTerminated() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.TERMINATED);
        workflowModel.setTasks(Arrays.asList(taskModel));
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepTaskBlocked() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.BLOCKED);
        workflowModel.setTasks(Arrays.asList(taskModel));
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, Utils.WORKFLOW_OFFSET_WITH_JITTER(120L) * 1000);
    }

    @Test
    public void testSweepTaskTimedOut() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.FAILED);
        taskModel.setTaskType(new TaskType("taskType", null, null, null, null));
        workflowModel.setTasks(Arrays.asList(taskModel));
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(workflowId));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verifySuccess();
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, Utils.WORKFLOW_OFFSET_WITH_JITTER(120L) * 1000);
    }

    private void verifySuccess() {
        Mockito.verifyNoMoreInteractions(LOGGER);
    }

    private void verifyError() {
        Mockito.verify(Monitors).error(Mockito.anyString(), Mockito.any(Throwable.class));
        Mockito.verify(LOGGER).error(Mockito.anyString(), Mockito.any(Throwable.class));
    }
}