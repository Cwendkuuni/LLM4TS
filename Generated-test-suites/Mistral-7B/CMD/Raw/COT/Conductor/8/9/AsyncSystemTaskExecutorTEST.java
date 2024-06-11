package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowInstance;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskDefinition;
import com.netflix.conductor.utils.SystemTaskCallbackListener;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;

    @Before
    public void init() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);

        when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Mockito.duration(5, "seconds"));
        when(conductorPropertiesMock.getTaskExecutionPostponeDuration()).thenReturn(
                Mockito.duration(10, "seconds"));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "task-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // Given
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.empty());

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacadeMock, never()).updateTask(any(TaskModel.class));
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.COMPLETED);

        // Given
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacadeMock, never()).updateTask(any(TaskModel.class));
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        // Given
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacadeMock, times(1)).updateTask(task);
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(queueDAOMock, times(1)).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(taskId),
                anyInt(),
                eq(longValue(5 * 1000)));
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecute_taskPostponed() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        // Given
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacadeMock, times(1)).updateTask(task);
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(queueDAOMock, times(1)).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(taskId),
                anyInt(),
                eq(longValue(10 * 1000)));
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_executionLimitExceeded() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        // Given
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacadeMock.exceedsInProgressLimit(task)).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        // No verification is needed in this case as an IllegalStateException should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_rateLimitExceeded() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setRateLimitPerFrequency(1);

        // Given
        when(executionDAOFacadeMock.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(metadataDAOMock.getTaskDef(task.getTaskDefName())).thenReturn(new TaskDefinition());
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(task, new TaskDefinition())).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        // No verification is needed in this case as an IllegalStateException should be thrown
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue-1";
        String taskId = "task-1";
        TaskModel task = new TaskModel();

        // Given
        when(queueDAOMock.postpone(queueName, taskId, anyInt(), anyLong())).thenReturn(true);

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Then
        verify(queueDAOMock, times(1)).postpone(queueName, taskId, anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();

        // Given
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(task));

        // When
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "task-1";

        // Given
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.empty());

        // When
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertNull(loadedTask);
    }
}