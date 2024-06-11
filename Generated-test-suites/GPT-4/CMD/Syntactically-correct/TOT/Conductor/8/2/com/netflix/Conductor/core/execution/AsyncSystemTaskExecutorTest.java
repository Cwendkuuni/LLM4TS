package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.junit.Assert.assertNull;

public class AsyncSystemTaskExecutorTest {
    
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private Logger logger;

    @Test
    public void postponeQuietly_whenExceptionThrown_errorShouldBeLogged() {
        // Add your mock setup here. i.e., make queueDAO.postpone() throw an exception
        asyncSystemTaskExecutor.postponeQuietly("queueName", new TaskModel());

        // Verify that an error was logged
        verify(logger, times(1)).error(anyString(), any(), any());
    }

    @Test
    public void loadTaskQuietly_whenExceptionThrown_nullShouldBeReturned() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        assertNull(result);
    }

    @Test
    public void execute_givenWorkflowSystemTaskAndTaskId_shouldLoadTaskAndExecuteIt() {
        TaskModel task = new TaskModel();
        task.setTaskId("taskId");
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        // Verify that the task was loaded and execute was called
        verify(executionDAOFacade, times(1)).getTaskModel("taskId");
        verify(workflowSystemTask, times(1)).execute(any(WorkflowModel.class), eq(task), any(WorkflowExecutor.class));
    }
}
