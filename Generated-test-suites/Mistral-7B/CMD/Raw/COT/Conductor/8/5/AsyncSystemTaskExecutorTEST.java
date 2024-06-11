package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
import com.netflix.conductor.model.WorkflowInstanceStatus;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;
    private String taskId;

    @Before
    public void setup() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);

        taskId = "task_id";
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.empty());
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);
        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(executionDAOFacadeMock, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, never()).decide(anyString());
        verify(LOGGER, times(1)).error("TaskId: {} could not be found while executing {}", taskId,
                Mockito.any(WorkflowSystemTask.class));
    }

    @Test
    public void testExecute_TaskStatusIsTerminal() {
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);
        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).remove(anyString(), taskId);
        verify(executionDAOFacadeMock, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecute_TaskStatusIsScheduled_ConcurrentExecutionLimitExceeded() {
        // Given
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacadeMock.exceedsInProgressLimit(taskModel))
                .thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).remove(anyString(), taskId);
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacadeMock, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, never()).decide(anyString());
        verify(LOGGER, times(1)).warn("Concurrent Execution limited for {}:{}, limit:{}", taskId,
                taskModel.getTaskDefName(), taskModel.getConcurrentExecutionLimit());
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskStatusIsScheduled_RateLimitPerFrequencyExceeded() {
        // Given
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("taskDefName");
        taskModel.setRateLimitPerFrequency(1);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowInstanceStatus.RUNNING);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).remove(anyString(), taskId);
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacadeMock, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, never()).decide(anyString());
        verify(LOGGER, times(1)).warn("RateLimit Execution limited for {}, limit:{}", taskId,
                taskModel.getTaskDefName(), taskModel.getRateLimitPerFrequency());
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskStatusIsScheduled_WorkflowCompletion() {
        // Given
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId("workflowId");
        workflowModel.setStatus(WorkflowInstanceStatus.COMPLETED);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacadeMock.getWorkflowModel(workflowModel.getId(), anyBoolean()))
                .thenReturn(workflowModel);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, times(1)).remove(anyString(), taskId);
        verify(executionDAOFacadeMock, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, times(1)).decide(workflowModel.getId());
        verify(LOGGER, times(1)).info("Workflow {} has been completed for {}/{}", workflowModel.toShortString(),
                Mockito.any(WorkflowSystemTask.class), taskId);
    }

    @Test
    public void testExecute_TaskStatusIsInProgress() {
        // Given
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.IN_PROGRESS);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId("workflowId");
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        doNothing().when(workflowSystemTask).execute(any(), any(), any());

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, never()).remove(anyString(), taskId);
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacadeMock, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, times(1)).decide(workflowModel.getId());
        verify(workflowSystemTask, times(1)).execute(any(), any(), any());

        // Add edge case for null workflowModel
        TaskModel taskModel2 = new TaskModel();
        taskModel2.setId(taskId);
        taskModel2.setStatus(ExecutionStatus.IN_PROGRESS);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(Optional.of(taskModel2));
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(null);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        verify(queueDAOMock, never()).remove(anyString(), taskId);
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacadeMock, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, never()).decide(anyString());
        verify(workflowSystemTask, never()).execute(any(), any(), any());
        verify(LOGGER, times(1)).error("Error executing system task - {}, with id: {}", workflowSystemTask, taskId);
    }

    @Test
    public void testPostponeQuietly_QueueException() {
        String queueName = "queueName";
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        when(queueDAOMock.postpone(queueName, taskId, anyInt(), anyLong()))
                .thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAOMock, times(1)).postpone(queueName, taskId, anyInt(), anyLong());
        verify(LOGGER, times(1)).error("Error postponing task: {} in queue: {}", taskId, queueName);
    }

    @Test
    public void testLoadTaskQuietly_ExecutionException() {
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacadeMock, times(1)).getTaskModel(taskId);
        assertNull(taskModel);
    }

    @After
    public void cleanUp() {
        reset(executionDAOFacadeMock, queueDAOMock, metadataDAOMock, conductorPropertiesMock, workflowExecutorMock);
    }
}