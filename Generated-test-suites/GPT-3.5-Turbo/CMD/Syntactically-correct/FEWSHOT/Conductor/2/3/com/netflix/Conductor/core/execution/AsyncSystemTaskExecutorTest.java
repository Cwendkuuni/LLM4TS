package com.netflix.conductor.core.execution;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Test
    public void testExecute_TaskFound() {
        // Setup
        ExecutionDAOFacade executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAOMock = mock(QueueDAO.class);
        MetadataDAO metadataDAOMock = mock(MetadataDAO.class);
        ConductorProperties conductorPropertiesMock = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutorMock = mock(WorkflowExecutor.class);
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock, queueDAOMock, metadataDAOMock, conductorPropertiesMock, workflowExecutorMock);

        // Mock Data
        TaskModel taskModelMock = mock(TaskModel.class);
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        String taskId = "testTaskId";
        String queueName = "testQueue";

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModelMock);
        when(systemTaskMock.isTaskRetrievalRequired()).thenReturn(true);
        when(workflowModelMock.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModelMock.getTaskType()).thenReturn("testTaskType");
        when(taskModelMock.getTaskId()).thenReturn(taskId);
        when(taskModelMock.getWorkflowPriority()).thenReturn(1);

        // Execution
        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

        // Verification
        verify(executionDAOFacadeMock).getTaskModel(taskId);
        verify(executionDAOFacadeMock).getWorkflowModel(anyString(), anyBoolean());
        verify(taskModelMock).incrementPollCount();
        verify(taskModelMock).setStartTime(anyLong());
        verify(taskModelMock).setCallbackAfterSeconds(anyLong());
        verify(queueDAOMock).postpone(eq(queueName), eq(taskId), anyInt(), anyLong());
        verify(executionDAOFacadeMock).updateTask(taskModelMock);
        verify(queueDAOMock).remove(eq(queueName), eq(taskId));
        verify(workflowExecutorMock).decide(anyString());
    }

    @Test
    public void testExecute_TaskNotFound() {
        // Setup
        ExecutionDAOFacade executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAOMock = mock(QueueDAO.class);
        MetadataDAO metadataDAOMock = mock(MetadataDAO.class);
        ConductorProperties conductorPropertiesMock = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutorMock = mock(WorkflowExecutor.class);
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock, queueDAOMock, metadataDAOMock, conductorPropertiesMock, workflowExecutorMock);

        // Mock Data
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        String taskId = "testTaskId";

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

        // Execution
        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

        // Verification
        verify(executionDAOFacadeMock).getTaskModel(taskId);
    }

}