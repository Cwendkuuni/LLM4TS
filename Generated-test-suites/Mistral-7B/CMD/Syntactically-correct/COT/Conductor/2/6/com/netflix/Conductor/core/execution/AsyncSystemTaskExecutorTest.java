package com.netflix.conductor.core.execution.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;

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

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Autowired
    private QueueUtils queueUtils;

    private WorkflowSystemTask systemTask;
    private TaskModel task;
    private WorkflowModel workflow;

    @Before
    public void setUp() {
        systemTask = mock(WorkflowSystemTask.class);
        task = mock(TaskModel.class);
        workflow = mock(WorkflowModel.class);
        when(workflowExecutor.decide(any())).thenReturn(Duration.ofMillis(1));
        when(executionDAOFacade.getTaskModel(any())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.remove(any(), any())).thenReturn(true);
        when(queueDAO.postpone(any(), any(), anyInt(), anyLong())).thenReturn(true);
        when(metadataDAO.getTaskDef(any())).thenReturn(mock(TaskDef.class));
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
        when(monitors.recordQueueWaitTime(anyString(), anyLong())).thenReturn(null);
    }

    @Test
    public void testExecute_taskNull() {
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
    }

    @Test
    public void testExecute_taskTerminal() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_taskScheduled_exceedsInProgressLimit() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskScheduled_exceedsRateLimitPerFrequency() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskInProgress() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(systemTask).execute(workflow, task, workflowExecutor);
    }

    @Test(expected = Exception.class)
    public void testExecute_workflowTerminal() {
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
    }

    @Test
    public void testExecute_taskAsyncComplete() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_taskStatusChange() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        verify(task).setStatus(TaskModel.Status.IN_PROGRESS);
        verify(task).setCallbackAfterSeconds(systemTaskCallbackTime);
    }

    @Test
    public void testLoadTaskQuietly_taskNull() {
        asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testLoadTaskQuietly_taskPresent() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        verify(executionDAOFacade).getTaskModel("taskId");
    }
}