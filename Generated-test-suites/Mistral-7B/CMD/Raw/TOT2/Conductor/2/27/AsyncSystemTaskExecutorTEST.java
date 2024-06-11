package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.metrics.Monitors;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(mock(Duration.class));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(mock(Duration.class));
    }

    @Test
    public void testExecute_TaskDoesNotExist_RemovesMessageFromQueue() {
        String taskId = "taskId1";
        String queueName = QueueUtils.getQueueName(new TaskModel());
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted_DoesNotUpdateTaskOrRemoveMessageFromQueue() {
        String taskId = "taskId1";
        String queueName = QueueUtils.getQueueName(new TaskModel());
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(queueDAO.remove(queueName, taskId)).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoInteractions(executionDAOFacade, metadataDAO, queueDAO);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_NullTaskId_ThrowsException() {
        String nullTaskId = null;
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, nullTaskId);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_NullSystemTask_ThrowsException() {
        String taskId = "taskId1";

        asyncSystemTaskExecutor.execute(null, taskId);
    }

    @Test
    public void testExecute_TaskIsTerminal_RemovesMessageFromQueue() {
        String taskId = "taskId1";
        String queueName = QueueUtils.getQueueName(new TaskModel());
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskInProgress_UpdatesTaskStatusAndQueueMessage() {
        String taskId = "taskId1";
        String queueName = QueueUtils.getQueueName(new TaskModel());
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade)
                .updateTask(argThat(t -> t.getId().equals(taskId) && t.getStatus() == TaskModel.Status.IN_PROGRESS));
        verify(queueDAO).postpone(
                queueName,
                taskId,
                workflow.getWorkflowPriority(),
                asyncSystemTaskExecutor.systemTaskCallbackTime.getSeconds());
    }

    @Test
    public void testExecute_TaskInProgress_CallbackAfterSecondsDoesNotChangeIfRateLimitIsExceeded() {
        String taskId = "taskId1";
        String queueName = QueueUtils.getQueueName(new TaskModel());
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade)
                .updateTask(argThat(t -> t.getId().equals(taskId) && t.getStatus() == TaskModel.Status.IN_PROGRESS));
        verify(queueDAO).postpone(
                queueName,
                taskId,
                workflow.getWorkflowPriority(),
                asyncSystemTaskExecutor.systemTaskCallbackTime.getSeconds());
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testExecute_TaskInProgress_CallbackAfterSecondsChangesIfRateLimitIsNotExceeded() {
        String taskId = "taskId1";
        String queueName = QueueUtils.getQueueName(new TaskModel());
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade)
                .updateTask(argThat(t -> t.getId().equals(taskId) && t.getStatus() == TaskModel.Status.IN_PROGRESS));
        verify(queueDAO)
                .postpone(queueName, taskId, workflow.getWorkflowPriority(), asyncSystemTaskExecutor.systemTaskCallbackTime.getSeconds());
        verify(queueDAO)
                .updateQueueMessage(queueName, taskId, Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Test(expected = UndeclaredThrowableException.class)
    public void testExecute_TaskExecutionThrowsException_RemovesMessageFromQueue() {
        String taskId = "taskId1";
        String queueName = QueueUtils.getQueueName(new TaskModel());
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doThrow(new RuntimeException("Exception in SystemTaskExecutor")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testPostponeQuietly_QueueMessageUpdated() {
        String queueName = QueueUtils.getQueueName(new TaskModel());
        TaskModel task = new TaskModel();
        QueueMessage queueMessage = new QueueMessage(task.getTaskId(), 5000L, 1L);

        when(queueDAO.getQueueMessage(queueName, task.getTaskId())).thenReturn(queueMessage);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).updateQueueMessage(queueName, task.getTaskId(), Optional.empty(), Optional.empty(), Optional.of(6000L));
    }

    @Test
    public void testLoadTaskQuietly_TaskIsLoaded() {
        String taskId = "taskId1";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_TaskIsNull() {
        String taskId = "taskId1";

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
    }
}