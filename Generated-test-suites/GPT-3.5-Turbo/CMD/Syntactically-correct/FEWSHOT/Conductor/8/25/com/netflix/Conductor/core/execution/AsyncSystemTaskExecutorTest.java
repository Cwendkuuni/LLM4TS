package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSystemTask systemTask;
    private String taskId;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        
        systemTask = mock(WorkflowSystemTask.class);
        taskId = "123";
    }

    @Test
    public void testExecuteTask() {
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        assertNotNull(task.getStartTime());
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecuteTaskTaskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testHandleTaskPostponement() {
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel task = new TaskModel();
        String queueName = "testQueue";
        when(queueDAO.postpone(eq(queueName), eq(taskId), anyInt(), anyLong())).thenThrow(new RuntimeException());
        
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        
        verify(logger, times(1)).error(anyString(), anyString(), anyString());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());
        
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        
        assertNotNull(result);
    }

    @Test
    public void testLoadTaskQuietlyTaskNotFound() {
        String taskId = "456";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        
        assertNull(result);
    }
}