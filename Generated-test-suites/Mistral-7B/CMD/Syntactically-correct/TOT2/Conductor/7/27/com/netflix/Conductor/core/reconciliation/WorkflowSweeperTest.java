package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowExecutionStatus;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings("unchecked")
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
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("test-app");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(10L);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
    }

    @Test
    public void testSweepAsync_WorkflowFoundAndTerminal() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.TERMINAL);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(workflow);

        CompletableFuture<Void> result = sweeper.sweepAsync("test-workflow-id");
        verify(queueDAO).remove(DECIDER_QUEUE, "test-workflow-id");
        result.join();
        verify(queueDAO).remove(DECIDER_QUEUE, "test-workflow-id");
    }

    @Test
    public void testSweepAsync_WorkflowNotFound() {
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(false);
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Test NotFoundException"));

        CompletableFuture<Void> result = sweeper.sweepAsync("test-workflow-id");
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "test-workflow-id");
        result.join();
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "test-workflow-id");
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"), any());
    }

    @Test
    public void testSweepAsync_ExceptionDuringSweep() {
        WorkflowModel workflow = new WorkflowModel();
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(workflow);

        doThrow(new RuntimeException("Test RuntimeException"))
                .when(sweeper)
                .sweep(anyString());

        CompletableFuture<Void> result = sweeper.sweepAsync("test-workflow-id");
        verify(queueDAO).remove(DECIDER_QUEUE, "test-workflow-id");
        result.join();
        verify(queueDAO).remove(DECIDER_QUEUE, "test-workflow-id");
        verify(monitors, times(1)).error(
                eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"), any());
    }

    @Test
    public void testUnack_InProgressTaskWithWaitType() {
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskDef(new TaskDef());
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(20L);
        workflowModel.setTasks(Arrays.asList(taskModel));

        long postponedDuration = sweeper.unack(workflowModel, 10L);

        assertEquals(31L, postponedDuration);
    }

    @Test
    public void testUnack_InProgressTaskWithHumanType() {
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskDef(new TaskDef());
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_HUMAN);
        workflowModel.setTasks(Arrays.asList(taskModel));

        long postponedDuration = sweeper.unack(workflowModel, 10L);

        assertEquals(11L, postponedDuration);
    }

    @Test
    public void testUnack_ScheduledTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskDef(new TaskDef());
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskType(TaskType.TASK_TYPE_EXEC);
        workflowModel.setTasks(Arrays.asList(taskModel));

        long postponedDuration = sweeper.unack(workflowModel, 10L);

        assertEquals(21L, postponedDuration);
    }

    @Test
    public void testUnack_TerminalWorkflow() {
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskDef(new TaskDef());
        taskModel.setStatus(Status.TERMINAL);
        workflowModel.setTasks(Arrays.asList(taskModel));

        long postponedDuration = sweeper.unack(workflowModel, 10L);

        assertEquals(0L, postponedDuration);
    }

    @Test
    public void testWorkflowOffsetWithJitter_PositiveValue() {
        long workflowOffsetTimeout = 10L;

        long workflowOffset = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(workflowOffset >= 7L && workflowOffset <= 13L);
    }

    @Test
    public void testWorkflowOffsetWithJitter_ZeroValue() {
        long workflowOffsetTimeout = 0L;

        long workflowOffset = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(0L, workflowOffset);
    }
}