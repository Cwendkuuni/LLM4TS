package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    ConductorProperties conductorProperties;
    @Mock
    WorkflowExecutor workflowExecutor;
    @Mock
    WorkflowSystemTask systemTask;
    @Mock
    TaskModel task;
    @Mock
    WorkflowModel workflow;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
  
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        
        asyncSystemTaskExecutor.execute(systemTask, "task1");

        verify(task).setStartTime(anyLong());
        verify(systemTask).start(any(), any(), any());
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        
        final TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("task1");

        assertNull(result);
    }

    @Test
    public void testLoadTaskQuietlyWithException() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        
        final TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("task1");

        assertNull(result);
    }

    @Test
    public void testPostponeQuietly() {
        doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly("queue", task);

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietlyWithException() {
        doThrow().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly("queue", task);

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}