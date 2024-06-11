package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.Tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskConsolidated;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskWithCallback;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskWithInput;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.testutils.TestExecutionDAOFacade;
import com.netflix.conductor.testutils.TestMetadataDAO;
import com.netflix.conductor.testutils.TestQueueDAO;
import com.netflix.conductor.testutils.TestWorkflowExecutor;

@SuppressWarnings("deprecation")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask workflowSystemTask;
    private WorkflowSystemTaskConsolidated workflowSystemTaskConsolidated;
    private WorkflowSystemTaskWithInput workflowSystemTaskWithInput;
    private WorkflowSystemTaskWithCallback workflowSystemTaskWithCallback;

    @Before
    public void initializeMocks() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(Duration.ofSeconds(5));
        this.workflow = new TestWorkflowModel("workflowId");
        this.task = new TestTaskModel("taskId");
        this.workflowSystemTask = new TestWorkflowSystemTask();
        this.workflowSystemTaskConsolidated = new TestWorkflowSystemTaskConsolidated();
        this.workflowSystemTaskWithInput = new TestWorkflowSystemTaskWithInput();
        this.workflowSystemTaskWithCallback = new TestWorkflowSystemTaskWithCallback();
    }

    @Test
    public void testExecuteNullTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteCompletedTask() {
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTerminalTask() {
        task.setStatus(TaskModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).remove(anyString(), eq("taskId"));
    }

    @Test
    public void testExecuteScheduledTask() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(anyString(), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testExecuteInProgressTask() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        // no assertions here as execute method does not throw any exception for IN_PROGRESS tasks
    }

    @Test
    public void testExecuteWithWorkflow() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflowExecutor.decide(anyString())).thenReturn(Optional.empty());
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(anyString(), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testExecuteWithWorkflowCompleted() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        workflow.setStatus(WorkflowModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflowExecutor.decide(anyString())).thenReturn(Optional.of(workflow));
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).remove(anyString(), eq("taskId"));
    }

    @Test
    public void testExecuteWithWorkflowFailed() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        workflow.setStatus(WorkflowModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflowExecutor.decide(anyString())).thenReturn(Optional.of(workflow));
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).remove(anyString(), eq("taskId"));
    }

    @Test
    public void testExecuteWithWorkflowRunning() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflowExecutor.decide(anyString())).thenReturn(Optional.of(workflow));
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(anyString(), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testExecuteWithWorkflowPostponed() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        workflow.setStatus(WorkflowModel.Status.POSTPONED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflowExecutor.decide(anyString())).thenReturn(Optional.of(workflow));
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(anyString(), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testExecuteWithConcurrentLimitExceeded() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(anyString(), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testExecuteWithRateLimitExceeded() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        TestMetadataDAO metadataDAO = new TestMetadataDAO();
        when(metadataDAO.getTaskDef(anyString())).thenReturn(metadataDAO.getTaskDef());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(anyString(), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testPostponeNullTask() {
        asyncSystemTaskExecutor.postponeQuietly(anyString(), null);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeTerminalTask() {
        task.setStatus(TaskModel.Status.FAILED);
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO, never()).postpone(anyString(), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testPostponeInProgressTask() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        ArgumentCaptor<Long> callbackAfterSecondsArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO, times(1)).postpone(eq("queueName"), eq("taskId"), eq(1), callbackAfterSecondsArgumentCaptor.capture());
        Long callbackAfterSeconds = callbackAfterSecondsArgumentCaptor.getValue();
        assertEquals(asyncSystemTaskExecutor.getSystemTaskCallbackTime().getSeconds(), callbackAfterSeconds.intValue());
    }

    @Test
    public void testLoadTaskNull() {
        asyncSystemTaskExecutor.loadTaskQuietly(null);
        assertNull(task);
    }

    @Test
    public void testLoadTaskCompleted() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertSame(task, loadedTask);
    }

    @Test
    public void testLoadTaskTerminal() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(null);
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertNull(loadedTask);
    }

    private static class TestWorkflowModel extends WorkflowModel {
        TestWorkflowModel(String workflowId) {
            super(workflowId, Instant.now());
        }
    }

    private static class TestTaskModel extends TaskModel {
        TestTaskModel(String taskId) {
            super(taskId, null, null, null, null, null, null, TaskModel.Status.SCHEDULED, null, null, null);
        }
    }

    private static class TestWorkflowSystemTask extends WorkflowSystemTask {
        @Override
        public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

        @Override
        public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}
    }

    private static class TestWorkflowSystemTaskConsolidated extends WorkflowSystemTaskConsolidated {}

    private static class TestWorkflowSystemTaskWithInput extends WorkflowSystemTaskWithInput {}

    private static class TestWorkflowSystemTaskWithCallback extends WorkflowSystemTaskWithCallback {}

}