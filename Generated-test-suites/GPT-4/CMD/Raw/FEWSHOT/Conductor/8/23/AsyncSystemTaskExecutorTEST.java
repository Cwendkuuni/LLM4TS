package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * The Testing class that tests the behavior of the AsyncSystemTaskExecutor class.
 */
public class AsyncSystemTaskExecutorTest {
    
    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowSystemTask workflowSystemTask;
    @Mock
    private WorkflowModel workflowModel;
    @Mock
    private TaskModel taskModel;
    
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(QueueUtils.getQueueName(any())).thenReturn("Test Queue");
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }
    
    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
        verify(queueDAO, times(0)).remove(anyString(), anyString());
    }
    
    @Test
    public void testPostponeQuietly() {
        doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly("Test Queue", taskModel);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
    
    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("1");
        assertEquals(taskModel, result);
    }
    
    @Test
    public void testLoadTaskQuietlyException() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("1");
        assertNull(result);
    }
    
    @Test
    public void testPostponeQuietlyException() {
        doThrow(new RuntimeException()).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly("Test Queue", taskModel);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}