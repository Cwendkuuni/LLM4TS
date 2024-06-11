
package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
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
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }
    
    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";
        TaskModel expected = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(expected);
        TaskModel actual = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLoadTaskQuietlyWithException() {
        String taskId = "123";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(RuntimeException.class);
        TaskModel actual = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(actual);
    }
    
}
