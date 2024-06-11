package com.netflix.conductor.core.execution;

import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {

    AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    ExecutionDAOFacade executionDAOFacadeMock;
    QueueDAO queueDAOMock;
    MetadataDAO metadataDAOMock;
    ConductorProperties conductorPropertiesMock;
    WorkflowExecutor workflowExecutorMock;
    WorkflowSystemTask systemTaskMock;
    TaskModel taskMock;
    WorkflowModel workflowMock;

    private void setUpMocks(String taskId, TaskModel.Status status) {
	    asyncSystemTaskExecutor = Mockito.mock(AsyncSystemTaskExecutor.class);
            executionDAOFacadeMock = Mockito.mock(ExecutionDAOFacade.class); 
	    queueDAOMock = Mockito.mock(QueueDAO.class);
            metadataDAOMock = Mockito.mock(MetadataDAO.class);
            conductorPropertiesMock = Mockito.mock(ConductorProperties.class);
            workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);
            systemTaskMock = Mockito.mock(WorkflowSystemTask.class);
	    taskMock = Mockito.mock(TaskModel.class);
            workflowMock = Mockito.mock(WorkflowModel.class);

	    when(taskMock.getTaskId()).thenReturn(taskId);
            when(taskMock.getStatus()).thenReturn(status);
            when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(taskMock);
            when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowMock);
    }
	
    @Test
    public void testExecuteTaskNotFound() {
        setUpMocks("testID", TaskModel.Status.SCHEDULED);
	when(asyncSystemTaskExecutor.loadTaskQuietly(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTaskMock, "testID");
	
	verify(asyncSystemTaskExecutor.getLogger()).error(anyString());
    }

    @Test
    public void testExecuteAlreadyCompletedTask() {
        setUpMocks("testID", TaskModel.Status.COMPLETED);
	asyncSystemTaskExecutor.execute(systemTaskMock, "testID");
	
	verify(asyncSystemTaskExecutor.queueDAO).remove(anyString());
    }

    @Test
    public void testExecuteScheduledTask() {
        setUpMocks("testID", TaskModel.Status.SCHEDULED);
        TaskModel.Status executeStatus = asyncSystemTaskExecutor.execute(systemTaskMock, "testID");
    
	verify(asyncSystemTaskExecutor.systemTask).start(any(), any(), any());
    }

    @Test
    public void testExecuteInProgressTask() {
        setUpMocks("testID", TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTaskMock, "testID");
	
	verify(asyncSystemTaskExecutor.systemTask).execute(any(), any(), any());
    }

    @Test
    public void testExecuteWithSystemTaskError() {
        setUpMocks("testID", TaskModel.Status.SCHEDULED);
        doThrow(RuntimeException.class).when(asyncSystemTaskExecutor.systemTask).start(any(), any(), any());
        asyncSystemTaskExecutor.execute(systemTaskMock, "testID");
	
	verify(asyncSystemTaskExecutor.getLogger()).error(anyString());
    }
}