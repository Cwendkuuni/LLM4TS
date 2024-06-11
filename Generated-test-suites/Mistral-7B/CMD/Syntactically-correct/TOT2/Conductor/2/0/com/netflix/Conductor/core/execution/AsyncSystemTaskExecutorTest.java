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
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;

    @Before
    public void setup() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);

        when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Mockito.Duration.ofSeconds(100));
        when(conductorPropertiesMock.getTaskExecutionPostponeDuration()).thenReturn(
                Mockito.Duration.ofSeconds(10));

        executor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
    }

    @Test
    public void testExecute_TaskFoundInExecutionDAO_TaskStatusIsNotTerminal() {
        // Given
        String taskId = "task123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setQueueName(QueueUtils.getQueueName(task));
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacadeMock).updateTask(task);
        verify(queueDAOMock, never()).remove(task.getQueueName(), taskId);
    }

    @Test
    public void testExecute_TaskFoundInExecutionDAO_TaskStatusIsTerminal() {
        // Given
        String taskId = "task123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacadeMock).updateTask(task);
        verify(queueDAOMock).remove(task.getQueueName(), taskId);
    }

    @Test
    public void testExecute_TaskNotFoundInExecutionDAO() {
        // Given
        String taskId = "task123";

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(queueDAOMock).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_TaskStatusIsInProgress() {
        // Given
        String taskId = "task123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskDefName("testTaskDef");
        task.setQueueName(QueueUtils.getQueueName(task));
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacadeMock).updateTask(task);
        verify(queueDAOMock, never()).remove(task.getQueueName(), taskId);
    }

    @Test
    public void testExecute_ExecutionDAOExceedsInProgressLimit() {
        // Given
        String taskId = "task123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setQueueName(QueueUtils.getQueueName(task));
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacadeMock.exceedsInProgressLimit(task)).thenReturn(true);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(queueDAOMock).postpone(eq(task.getQueueName()), eq(taskId), anyInt(), eq(10));
    }

    @Test
    public void testExecute_ExecutionDAOExceedsRateLimitPerFrequency() {
        // Given
        String taskId = "task123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setQueueName(QueueUtils.getQueueName(task));
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAOMock.getTaskDef(task.getTaskDefName()))
                .thenReturn(new WorkflowModel());
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(queueDAOMock).postpone(eq(task.getQueueName()), eq(taskId), anyInt(), eq(10));
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        // Given
        String taskId = "task123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setQueueName(QueueUtils.getQueueName(task));
        task.setWorkflowInstanceId("workflowId");
        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel(workflow.getWorkflowInstanceId(), true))
                .thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacadeMock).updateTask(task);
        verify(workflowExecutorMock).decide(workflow.getWorkflowInstanceId());
        verify(queueDAOMock).remove(task.getQueueName(), taskId);
    }

    @Test
    public void testPostponeQuietly_TaskFoundInQueue() {
        // Given
        String queueName = "queueName";
        String taskId = "task123";
        when(queueDAOMock.getAllTasksInQueue(queueName)).thenReturn(Collections.singletonList(taskId));

        // When
        executor.postponeQuietly(queueName, new TaskModel());

        // Then
        verify(queueDAOMock).postpone(eq(queueName), eq(taskId), anyInt(), eq(10));
    }

    @Test
    public void testPostponeQuietly_TaskNotFoundInQueue() {
        // Given
        String queueName = "queueName";

        // When
        executor.postponeQuietly(queueName, new TaskModel());

        // Then
        verify(queueDAOMock, never()).postpone(eq(queueName), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        // Given
        String taskId = "task123";
        TaskModel task = new TaskModel();
        task.setId(taskId);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

        // When
        TaskModel loadedTask = executor.loadTaskQuietly(taskId);

        // Then
        assertSame(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        // Given
        String taskId = "task123";

        // When
        TaskModel loadedTask = executor.loadTaskQuietly(taskId);

        // Then
        assertNull(loadedTask);
    }
}