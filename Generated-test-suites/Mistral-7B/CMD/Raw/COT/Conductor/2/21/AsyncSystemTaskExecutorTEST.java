package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.Metadata;
import com.netflix.conductor.core.model.Queue;
import com.netflix.conductor.core.model.Task;
import com.netflix.conductor.core.model.TaskDefinition;
import com.netflix.conductor.core.model.Workflow;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.TestUtils;

@SuppressWarnings("deprecation")
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Monitors monitors;

    @Mock
    private ConductorProperties conductorProperties;

    @Value("${systemTaskWorkerCallbackDuration}")
    private Duration systemTaskCallbackDuration;

    @Value("${taskExecutionPostponeDuration}")
    private Duration taskExecutionPostponeDuration;

    private Task task;
    private WorkflowSystemTask systemTask;
    private Workflow workflow;

    @Before
    public void initMocks() {
        task = new Task();
        systemTask = mock(WorkflowSystemTask.class);
        workflow = new Workflow();

        when(metadataDAO.getTaskDef(any()))
                .thenReturn(new TaskDefinition());
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(anyString()))
                .thenReturn(Optional.of(task));
        when(queueDAO.remove(anyString(), anyString()))
                .thenReturn(true);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(true);
        when(queueDAO.getQueue(anyString()))
                .thenReturn(new Queue(QueueUtils.getQueueName(task), 0));
        when(workflowExecutor.decide(anyString()))
                .thenReturn(Optional.empty());
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "123";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
        assertNull(task.getTaskId());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        task.setStatus(Task.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        assertFalse(workflowExecutor.decide(workflow.getWorkflowId()).isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecute_NullTask() {
        asyncSystemTaskExecutor.execute(systemTask, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecute_NullSystemTask() {
        asyncSystemTaskExecutor.execute(null, task.getTaskId());
    }

    @Test
    public void testExecute_TaskIsTerminal() {
        task.setStatus(Task.Status.FAILED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        assertFalse(workflowExecutor.decide(workflow.getWorkflowId()).isPresent());
    }

    @Test
    public void testExecute_TaskIsScheduled() {
        task.setStatus(Task.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO, times(2)).postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                anyLong());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_TaskIsInProgress() {
        task.setStatus(Task.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                systemTaskCallbackDuration.getSeconds());
        verify(systemTask).execute(workflow, task, workflowExecutor);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ExceptionDuringExecution() {
        task.setStatus(Task.Status.SCHEDULED);
        when(systemTask.execute(any(), any(), any()))
                .thenThrow(new RuntimeException("Test error"));
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        assertFalse(workflowExecutor.decide(workflow.getWorkflowId()).isPresent());
    }

    @Test
    public void testExecute_TaskIsTerminalAfterExecution() {
        task.setStatus(Task.Status.SCHEDULED);
        when(systemTask.execute(any(), any(), any()))
                .thenReturn(new Object());
        task.setStatus(Task.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        assertFalse(workflowExecutor.decide(workflow.getWorkflowId()).isPresent());
    }

    @Test
    public void testPostponeQuietly_QueueNotFound() {
        String queueName = "queueName";
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, never()).postpone(queueName, task.getTaskId(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly_TaskNotFound() {
        String queueName = "queueName";
        asyncSystemTaskExecutor.postponeQuietly(queueName, null);
        verify(queueDAO, never()).postpone(queueName, null, anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_NullTaskId() {
        asyncSystemTaskExecutor.loadTaskQuietly(null);
        assertNull(task.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly_TaskLoaded() {
        task.setId("taskId");
        asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertNotNull(task.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        asyncSystemTaskExecutor.loadTaskQuietly("nonExistentTaskId");
        assertNull(task.getTaskId());
    }

    @Test
    public void testExecute_ConcurrentExecutionLimit() {
        String workflowId = "workflowId";
        when(executionDAOFacade.exceedsInProgressLimit(task))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());
    }

    @Test
    public void testExecute_RateLimitPerFrequency() {
        String workflowId = "workflowId";
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());
    }

    @Test
    public void testExecute_WorkflowTerminalState() {
        task.setStatus(Task.Status.SCHEDULED);
        workflow.setStatus(ExecutionStatus.FAILED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        assertFalse(workflowExecutor.decide(workflow.getWorkflowId()).isPresent());
    }

    @Test
    public void testExecute_TaskPostponedAfterExecution() {
        task.setStatus(Task.Status.SCHEDULED);
        when(systemTask.execute(any(), any(), any()))
                .thenReturn(new Object());
        task.setStatus(Task.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                taskExecutionPostponeDuration.getSeconds());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_TaskPostponedBeforeExecution() {
        task.setStatus(Task.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                systemTaskCallbackDuration.getSeconds());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_WorkflowPendingState() {
        workflow.setStatus(ExecutionStatus.PENDING);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        verify(workflowExecutor).decide(workflow.getWorkflowId());
    }

    @Test
    public void testExecute_WorkflowRunningState() {
        workflow.setStatus(ExecutionStatus.RUNNING);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        verify(workflowExecutor).decide(workflow.getWorkflowId());
    }

    @Test
    public void testExecute_WorkflowCompletedState() {
        workflow.setStatus(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        assertFalse(workflowExecutor.decide(workflow.getWorkflowId()).isPresent());
    }

    @Test
    public void testExecute_WorkflowErrorState() {
        workflow.setStatus(ExecutionStatus.ERROR);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        assertFalse(workflowExecutor.decide(workflow.getWorkflowId()).isPresent());
    }

    @Test
    public void testMonitors_QueueWaitTimeRecorded() {
        long queueWaitTime = 500L;
        when(task.getQueueWaitTime())
                .thenReturn(Optional.of(Duration.ofMillis(queueWaitTime).getSeconds()));
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(monitors).recordQueueWaitTime(task.getTaskType(), queueWaitTime);
    }

    @Test
    public void testMonitors_ErrorRecorded() {
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(monitors).error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
    }
}