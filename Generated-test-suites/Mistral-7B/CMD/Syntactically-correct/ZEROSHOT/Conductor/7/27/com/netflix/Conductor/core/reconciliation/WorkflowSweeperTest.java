package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;

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
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.test.testcontainers.Testcontainers;

@Testcontainers
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

    @Mock
    private TaskModel taskModel;

    @Mock
    private TaskDef taskDef;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync_WorkflowFoundAndTerminal_RemovedFromQueue() {
        // Given
        String workflowId = "workflow-1";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setStatus(Status.FAILED);

        when(properties.getAppId()).thenReturn("test-app-id");
        when(queueDAO.get(DECIDIDER_QUEUE, workflowId)).thenReturn(workflow);

        // When
        workflowSweeper.sweepAsync(workflowId);

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepAsync_WorkflowNotFound_RemovedFromQueue() {
        // Given
        String workflowId = "workflow-1";

        when(properties.getAppId()).thenReturn("test-app-id");
        when(queueDAO.get(DECIDIDER_QUEUE, workflowId)).thenThrow(new NotFoundException());

        // When
        workflowSweeper.sweepAsync(workflowId);

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepAsync_WorkflowNotTerminal_UnackWithTimeout() {
        // Given
        String workflowId = "workflow-1";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setStatus(Status.SCHEDULED);

        when(properties.getAppId()).thenReturn("test-app-id");
        when(queueDAO.get(DECIDIDER_QUEUE, workflowId)).thenReturn(workflow);

        // When
        workflowSweeper.sweepAsync(workflowId);

        // Then
        long expectedWorkflowOffsetTimeout = 1000L; // for testing purpose
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, expectedWorkflowOffsetTimeout * 1000);
    }

    @Test
    public void testUnack_WorkflowWithInProgressTask_UnackWithTimeout() {
        // Given
        String workflowId = "workflow-1";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);

        List<TaskModel> tasks = new ArrayList<>();
        TaskModel task = new TaskModel();
        task.setTaskId("task-1");
        task.setStatus(Status.IN_PROGRESS);
        task.setTaskType(TaskType.TASK_TYPE_EMAIL);
        task.setWorkflowId(workflowId);
        tasks.add(task);
        workflow.setTasks(tasks);

        when(properties.getAppId()).thenReturn("test-app-id");
        when(queueDAO.get(DECIDIDER_QUEUE, workflowId)).thenReturn(workflow);

        // When
        workflowSweeper.unack(workflow);

        // Then
        long expectedPostponeDurationSeconds = 11L; // for testing purpose
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, expectedPostponeDurationSeconds * 1000);
    }

    @Test
    public void testUnack_WorkflowWithScheduledTask_UnackWithTimeout() {
        // Given
        String workflowId = "workflow-1";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);

        List<TaskModel> tasks = new ArrayList<>();
        TaskModel task = new TaskModel();
        task.setTaskId("task-1");
        task.setStatus(Status.SCHEDULED);
        task.setTaskType(TaskType.TASK_TYPE_WAIT);
        task.setWorkflowId(workflowId);
        tasks.add(task);
        workflow.setTasks(tasks);

        when(properties.getAppId()).thenReturn("test-app-id");
        when(queueDAO.get(DECIDIDER_QUEUE, workflowId)).thenReturn(workflow);

        // When
        workflowSweeper.unack(workflow);

        // Then
        long expectedPostponeDurationSeconds = 11L; // for testing purpose
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, expectedPostponeDurationSeconds * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter_ValidWorkflowOffsetTimeout_ReturnsValidTimeoutWithJitter() {
        // Given
        long workflowOffsetTimeout = 45L;

        // When
        long actualWorkflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        long expectedMinJitter = 30L;
        long expectedMaxJitter = 60L;
        assertTrue(actualWorkflowOffsetWithJitter >= expectedMinJitter);
        assertTrue(actualWorkflowOffsetWithJitter <= expectedMaxJitter);
    }
}