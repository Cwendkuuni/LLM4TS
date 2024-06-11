
package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    
    @Mock
    ExecutionDAOFacade executionDAOFacade;
    
    @Mock
    QueueDAO queueDAO;
    
    @Mock
    MetadataDAO metadataDAO;
    
    @Mock
    ConductorProperties conductorProperties;
    
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPostponeQuietly() {
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.postponeQuietly("testQueue", new TaskModel());
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly("testTaskId"));
    }

    @Test
    public void testExecute() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(TimeUnit.SECONDS.toSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(TimeUnit.SECONDS.toSeconds(1));
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");
    }
}
