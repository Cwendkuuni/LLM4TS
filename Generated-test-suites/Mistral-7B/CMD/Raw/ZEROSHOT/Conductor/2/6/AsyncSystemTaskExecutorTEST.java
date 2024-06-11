package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;

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
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNotFound() {
        // Given
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(eq(QueueUtils.getQueueName("")), eq(taskId));
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        // Given
        String taskId = "task-123";
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(eq(QueueUtils.getQueueName("")), eq(taskId));
    }

    @Test
    public void testExecute_taskNotInQueue() {
        // Given
        String taskId = "task-123";
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testExecute_taskInProgress() {
        // Given
        String taskId = "task-123";
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testExecute_taskRateLimitExceeded() {
        // Given
        String taskId = "task-123";
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getRateLimitPerFrequency()).thenReturn(1);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(mock(WorkflowModel.class));

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecute_taskInQueue() {
        // Given
        String taskId = "task-123";
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).postpone(
                eq(QueueUtils.getQueueName("")), eq(taskId), anyInt(), anyInt());
    }

    @Test
    public void testExecute_taskError() {
        // Given
        String taskId = "task-123";
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doThrow(new RuntimeException("System task error")).when(systemTask).execute(any(), any(), any());

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecute_workflowCompleted() {
        // Given
        String taskId = "task-123";
        String workflowId = "workflow-123";
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn(workflowId);

        WorkflowModel workflow = createWorkflowModel();
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(eq(QueueUtils.getQueueName("")), eq(taskId));
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    private TaskModel createTaskModel() {
        return new TaskModel()
                .withTaskId("task-123")
                .withTaskType("task-type")
                .withTaskDefName("task-def-name")
                .withStatus(ExecutionStatus.SCHEDULED)
                .withStartTime(0L)
                .withEndTime(0L)
                .withQueueWaitTime(0L)
                .withWorkflowPriority(1)
                .withWorkflowInstanceId("workflow-123");
    }

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel()
                .withId("workflow-123")
                .withName("workflow-name")
                .withDefinition("workflow-definition")
                .withStatus(ExecutionStatus.COMPLETED);
    }
}