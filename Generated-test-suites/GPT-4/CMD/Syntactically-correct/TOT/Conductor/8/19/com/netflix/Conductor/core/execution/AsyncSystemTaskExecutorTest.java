package com.netflix.conductor.core.execution;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    @Mock 
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private TaskModel task;
    
    private AsyncSystemTaskExecutor executor;
    private final long systemTaskCallbackTime = 60;
    private final long queueTaskMessagePostponeSecs = 60; 
    String taskId = "1";
    
    @Before
    public void setup(){
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, systemTaskCallbackTime, queueTaskMessagePostponeSecs );
    }
    
    @Test
    public void testExecute_nullTask_returnWithoutThrowingException() {
        WorkflowSystemTask systemTask = new WorkflowSystemTask("test");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        executor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testExecute_normalExecution_successfulExecutionNoException() {
        WorkflowSystemTask systemTask = new WorkflowSystemTask("test");
        task = new TaskModel();
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        executor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly_noException_successfulPostpone() {
        task = new TaskModel();
        
        executor.postponeQuietly("queueName", task);
        
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
    
    @Test
    public void testLoadTaskQuietly_noException_successfulLoad() {
        task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel returnedTask = executor.loadTaskQuietly(taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertNotNull(returnedTask);
    }
    
    @Test
    public void testLoadTaskQuietly_exception_nullReturned() {
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        TaskModel returnedTask = executor.loadTaskQuietly(taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertNull(returnedTask);
    }
}