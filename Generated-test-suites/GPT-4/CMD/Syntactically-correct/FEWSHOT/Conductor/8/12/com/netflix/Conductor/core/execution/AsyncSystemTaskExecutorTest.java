package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
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
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

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
    private WorkflowSystemTask systemTask;
    @Mock
    private TaskModel taskModel;
    @Mock
    private WorkflowModel workflowModel;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO,
                conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute(){
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(systemTask.isAsyncComplete(any())).thenReturn(false);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(systemTask, "1");

        verify(systemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
    }

    @Test
    public void testPostponeQuietly(){
        doNothing().when(queueDAO).postpone(any(), any(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);
        verify(queueDAO, times(1)).postpone(eq("queueName"), any(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly(){
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        TaskModel response = asyncSystemTaskExecutor.loadTaskQuietly("1");
        assertEquals(response, taskModel);
    }

    @Test
    public void testLoadTaskQuietlyWhenException(){
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(RuntimeException.class);
        TaskModel response = asyncSystemTaskExecutor.loadTaskQuietly("1");
        assertEquals(response, null);
    }
}