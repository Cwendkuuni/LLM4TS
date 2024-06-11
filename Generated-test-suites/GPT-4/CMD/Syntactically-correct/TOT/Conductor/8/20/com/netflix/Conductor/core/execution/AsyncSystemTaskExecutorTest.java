package com.netflix.conductor.core.execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*; 

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock; 
    private QueueDAO queueDAOMock; 
    private MetadataDAO metadataDAOMock; 
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowSystemTask workflowSystemTaskMock;

    @Before
    public void setUp() {
        executionDAOFacadeMock = Mockito.mock(ExecutionDAOFacade.class);
        queueDAOMock = Mockito.mock(QueueDAO.class);
        metadataDAOMock = Mockito.mock(MetadataDAO.class);
        ConductorProperties conductorProperties = new ConductorProperties();
        workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);
        workflowSystemTaskMock = Mockito.mock(WorkflowSystemTask.class);
        
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacadeMock, queueDAOMock, metadataDAOMock, conductorProperties, workflowExecutorMock);
    }

    @Test
    public void testExecute_noTaskId() {
        String taskId = "A1";
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, taskId);
        
        verify(workflowSystemTaskMock, never()).start(any(), any(), any());
        verify(workflowSystemTaskMock, never()).execute(any(), any(), any());
    }

    @Test
    public void testExecute_scheduledTask() {
        String taskId = "A1";
        TaskModel taskModel = getTaskModel(TaskModel.Status.SCHEDULED);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModel);
        
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, taskId);
        
        verify(workflowSystemTaskMock).start(any(), eq(taskModel), any());
        verify(workflowSystemTaskMock, never()).execute(any(), any(), any());
    }

    @Test
    public void testExecute_inProgressTask() {
        String taskId = "A1";
        TaskModel taskModel = getTaskModel(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModel);
        
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, taskId);
        
        verify(workflowSystemTaskMock, never()).start(any(), any(), any());
        verify(workflowSystemTaskMock).execute(any(), eq(taskModel), any());
    }
    
    private TaskModel getTaskModel(TaskModel.Status status) {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(status);
        return taskModel;
    }
}