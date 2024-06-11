package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    @Test
    public void testSweep() {
        // Given
        ConductorProperties properties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        String workflowId = "workflow-123";
        Instant now = Instant.now();
        Instant past = now.minusSeconds(10);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setStatus(Status.RUNNING);
        workflow.setCreatedAt(past);
        workflow.setUpdatedAt(now);

        when(properties.getAppId()).thenReturn("app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(10L);

        when(workflowExecutor.decide(anyString())).thenReturn(workflow);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);

        // When
        sweeper.sweep(workflowId);

        // Then
        ArgumentCaptor<WorkflowModel> workflowCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, timeoutCaptor.capture());
        Long timeout = timeoutCaptor.getValue();
        assertEquals(11L, timeout.longValue()); // 10 seconds + 1
    }

    @Test
    public void testSweepNotFound() {
        // Given
        ConductorProperties properties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        String workflowId = "workflow-123";

        when(properties.getAppId()).thenReturn("app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(10L);

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);

        // When
        sweeper.sweep(workflowId);

        // Then
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(queueDAO).remove(DECIDER_QUEUE, workflowIdCaptor.capture());
        assertEquals(workflowId, workflowIdCaptor.getValue());
    }

    @Test
    public void testSweepException() {
        // Given
        ConductorProperties properties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        String workflowId = "workflow-123";

        when(properties.getAppId()).thenReturn("app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(10L);

        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException("An error occurred"));
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);

        // When
        sweeper.sweep(workflowId);

        // Then
        Monitors monitors = mock(Monitors.class);
        when(properties.getMonitors()).thenReturn(monitors);
        Mockito.verify(monitors).error(WorkflowSweeper.class.getName(), "sweep");
    }

    @Test
    public void testUnack() {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel(),
                new TaskModel(),
                new TaskModel());
        workflowModel.setTasks(tasks);

        WorkflowSweeper sweeper = new WorkflowSweeper();

        // When
        sweeper.unack(workflowModel, 11L);

        // Then
        for (TaskModel taskModel : workflowModel.getTasks()) {
            if (taskModel.getStatus() == Status.IN_PROGRESS) {
                assertEquals(12L, taskModel.getUnackTimeout());
            }
        }
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Given
        WorkflowSweeper sweeper = new WorkflowSweeper();

        // When
        long workflowOffsetTimeout = 45;
        long jitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        assertEquals(46, jitter);
    }
}