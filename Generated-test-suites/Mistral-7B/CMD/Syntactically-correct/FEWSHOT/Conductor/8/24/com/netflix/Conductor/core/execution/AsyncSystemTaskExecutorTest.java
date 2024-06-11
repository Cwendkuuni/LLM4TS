package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceModel;
import com.netflix.conductor.model.WorkflowModel.Status;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private WorkflowExecutor workflowExecutorMock;
    private ConductorProperties conductorPropertiesMock;

    @Before
    public void setup() {
        executor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
    }

    @Test
    public void testExecute_taskIsNotFound() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(null);

        executor.execute(systemTaskMock, taskId);

        verify(executionDAOFacadeMock).updateTask(taskModelMock);
        verifyNoMoreInteractions(queueDAOMock, metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.NOT_FOUND, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsTerminal() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        executor.execute(systemTaskMock, taskId);

        verify(executionDAOFacadeMock).updateTask(taskModelMock);
        verifyNoMoreInteractions(queueDAOMock, metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.COMPLETED, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsScheduled_noConcurrentLimitReached() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(Status.RUNNING);
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflowModelMock);

        executor.execute(systemTaskMock, taskId);

        verify(executionDAOFacadeMock).updateTask(taskModelMock);
        verifyNoMoreInteractions(queueDAOMock, metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.IN_PROGRESS, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsScheduled_concurrentLimitReached() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(Status.RUNNING);
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflowModelMock);
        when(executionDAOFacadeMock.exceedsInProgressLimit(taskModelMock)).thenReturn(true);

        executor.execute(systemTaskMock, taskId);

        verify(queueDAOMock).postpone(eq("queueName"), eq(taskId), anyInt(), anyLong());
        verifyNoMoreInteractions(metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.POSTPONED, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsScheduled_rateLimitReached() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(Status.RUNNING);
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, false)).thenReturn(workflowModelMock);
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(taskModelMock, any())).thenReturn(true);

        executor.execute(systemTaskMock, taskId);

        verify(queueDAOMock).postpone(eq("queueName"), eq(taskId), anyInt(), anyLong());
        verifyNoMoreInteractions(metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.POSTPONED, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsRunning() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(Status.RUNNING);
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.RUNNING);

        executor.execute(systemTaskMock, taskId);

        verify(executionDAOFacadeMock).updateTask(taskModelMock);
        verifyNoMoreInteractions(queueDAOMock, metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.IN_PROGRESS, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsRunning_asyncComplete() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(Status.RUNNING);
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.RUNNING);

        System.setProperty("task.async_complete", "true");
        boolean isTaskAsyncComplete = systemTaskMock.isAsyncComplete(taskModelMock);

        executor.execute(systemTaskMock, taskId);

        verify(queueDAOMock).remove(eq("queueName"), eq(taskId));
        verifyNoMoreInteractions(metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.COMPLETED, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsRunning_noAsyncComplete() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(Status.RUNNING);
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.RUNNING);

        System.setProperty("task.async_complete", "false");
        boolean isTaskAsyncComplete = systemTaskMock.isAsyncComplete(taskModelMock);

        executor.execute(systemTaskMock, taskId);

        verify(queueDAOMock).postpone(eq("queueName"), eq(taskId), anyInt(), anyLong());
        verifyNoMoreInteractions(metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.IN_PROGRESS, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsTerminal_asyncComplete() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(Status.COMPLETED);
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        System.setProperty("task.async_complete", "true");
        boolean isTaskAsyncComplete = systemTaskMock.isAsyncComplete(taskModelMock);

        executor.execute(systemTaskMock, taskId);

        verify(queueDAOMock).remove(eq("queueName"), eq(taskId));
        verifyNoMoreInteractions(metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.COMPLETED, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsTerminal_noAsyncComplete() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel workflowModelMock = Mockito.mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(Status.COMPLETED);
        WorkflowSystemTask systemTaskMock = Mockito.mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        System.setProperty("task.async_complete", "false");
        boolean isTaskAsyncComplete = systemTaskMock.isAsyncComplete(taskModelMock);

        executor.execute(systemTaskMock, taskId);

        verify(queueDAOMock).remove(eq("queueName"), eq(taskId));
        verifyNoMoreInteractions(metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.COMPLETED, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_taskIsPostponed() {
        String taskId = UUID.randomUUID().toString();
        String queueName = "queueName";

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.POSTPONED);

        executor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verifyNoInteractions(queueDAOMock, metadataDAOMock, workflowExecutorMock);
        assertEquals(TaskModel.Status.COMPLETED, taskModelMock.getStatus());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel taskModelMock = Mockito.mock(TaskModel.class);

        long queueTaskMessagePostponeSecs = 60L;

        executor.postponeQuietly(queueName, taskModelMock);

        verify(queueDAOMock).postpone(eq(queueName), eq(taskModelMock.getTaskId()), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound() {
        String taskId = UUID.randomUUID().toString();

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

        TaskModel taskModelResult = executor.loadTaskQuietly(taskId);

        verifyZeroInteractions(queueDAOMock, metadataDAOMock, workflowExecutorMock);
        assertEquals(null, taskModelResult);
    }

    @Test
    public void testLoadTaskQuietly_taskIsFound() {
        String taskId = UUID.randomUUID().toString();

        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);

        TaskModel taskModelResult = executor.loadTaskQuietly(taskId);

        verifyZeroInteractions(queueDAOMock, metadataDAOMock, workflowExecutorMock);
        assertEquals(taskModelMock, taskModelResult);
    }

}