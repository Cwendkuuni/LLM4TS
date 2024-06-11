package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.workflow.WorkflowExecutor;

@SuppressWarnings("unchecked")
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
    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(new WorkflowModel());
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(new TaskModel());
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new Object());
        when(queueDAO.getQueueMessage(anyString(), anyString())).thenReturn(null);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "taskId123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doNothing().when(systemTask).start(any(), any(), any());
        doNothing().when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade, never()).getTaskModel(taskId);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        String taskId = "taskId123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, never()).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_ExceedsInProgressLimit() {
        String taskId = "taskId123";
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).exceedsInProgressLimit(task);
        verify(queueDAO, never()).remove(anyString(), taskId);
        verifyZeroInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_ExceedsRateLimitPerFrequency() {
        String taskId = "taskId123";
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).exceedsRateLimitPerFrequency(task, any());
        verify(queueDAO, never()).remove(anyString(), taskId);
        verifyZeroInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_TaskScheduledAndNotAsyncComplete() {
        String taskId = "taskId123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doNothing().when(systemTask).start(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(task).incrementPollCount();
        verify(queueDAO).postpone(
                anyString(),
                taskId,
                anyInt(),
                eq(AsyncSystemTaskExecutor.this.systemTaskCallbackTime));
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = "taskId123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doNothing().when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask).execute(any(), any(), any());
    }

    @Test
    public void testExecute_TaskAsyncComplete() {
        String taskId = "taskId123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doNothing().when(systemTask).start(any(), any(), any());
        doNothing().when(systemTask).execute(any(), any(), any());
        doReturn(true).when(systemTask).isAsyncComplete(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_TaskTerminal() {
        String taskId = "taskId123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.TERMINAL);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor).decide(eq("workflowId"));
    }

    @Test
    public void testPostponeQuietly_QueueNameNotNull() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                eq(AsyncSystemTaskExecutor.this.queueTaskMessagePostponeSecs));
    }

    @Test(expected = NullPointerException.class)
    public void testPostponeQuietly_QueueNameNull() {
        AsyncSystemTaskExecutor.this.postponeQuietly(null, new TaskModel());
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "taskId123";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel taskLoaded = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(task, taskLoaded);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "taskId123";

        TaskModel taskLoaded = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(taskLoaded);
    }
}