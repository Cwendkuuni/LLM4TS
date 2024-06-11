package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    ExecutionDAOFacade executionDAOFacade;

    @Mock
    WorkflowSystemTask systemTask;

    @Mock
    TaskModel taskModel;

    @Mock
    WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_whenTaskIsNull() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_whenTaskIsNotNull() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    @Test
    public void testPostponeQuietly() {
        doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);
		verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_whenExceptionIsThrown() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertNull(taskModel);
    }
    
    @Test
    public void testLoadTaskQuietly_whenNoException() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertEquals(taskModel, result);
    }
}