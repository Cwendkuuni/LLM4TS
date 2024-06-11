package com.netflix.conductor.core.execution;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock
    ExecutionDAOFacade executionDAOFacade;
    
    @Mock
    QueueDAO queueDAO;
    
    @Mock
    MetadataDAO metadataDAO;
    
    @Mock
    WorkflowExecutor workflowExecutor;
    
    @Mock
    WorkflowSystemTask systemTask;
    
    @Mock
    TaskModel taskModel;
    
    @Mock
    WorkflowModel workflowModel;

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(systemTask, "123");
        
        verify(executionDAOFacade, times(1)).getTaskModel("123");
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor, systemTask);
    }
  
    @Test
    public void testExecuteTaskIsTerminal() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        
        asyncSystemTaskExecutor.execute(systemTask, "123");
        
        verify(executionDAOFacade, times(1)).getTaskModel("123");
        verify(taskModel, times(1)).getStatus();
        verify(queueDAO, times(1)).remove(anyString(), eq("123"));
    }
  
    // Add more test cases here for different scenarios
}