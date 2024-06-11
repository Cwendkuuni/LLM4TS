package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowSystemTask systemTaskMock;
    private TaskModel taskModelMock;
    private WorkflowModel workflowModelMock;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);
        systemTaskMock = mock(WorkflowSystemTask.class);
        taskModelMock = mock(TaskModel.class);
        workflowModelMock = mock(WorkflowModel.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock, 
                queueDAOMock, 
                metadataDAOMock, 
                conductorPropertiesMock, 
                workflowExecutorMock);
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTaskMock, "taskId");

        verify(executionDAOFacadeMock, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecuteTaskIsTerminal() {
        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTaskMock, "taskId");

        verify(queueDAOMock).remove(anyString(), anyString());
        verify(executionDAOFacadeMock).updateTask(any(TaskModel.class));
        verify(workflowExecutorMock, never()).decide(anyString());
    }

    @Test
    public void testExecuteTaskInProgress() {
        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(taskModelMock);
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModelMock);
        when(taskModelMock.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(workflowModelMock.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTaskMock, "taskId");

        verify(workflowExecutorMock).decide(anyString());
        verify(queueDAOMock).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly() {
        doThrow(new RuntimeException()).when(queueDAOMock).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModelMock);
    } 

    @Test
    public void testLoadTaskQuietlyFailure() {
        doThrow(new RuntimeException()).when(executionDAOFacadeMock).getTaskModel(anyString());

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        assertNull(task);
    } 
}