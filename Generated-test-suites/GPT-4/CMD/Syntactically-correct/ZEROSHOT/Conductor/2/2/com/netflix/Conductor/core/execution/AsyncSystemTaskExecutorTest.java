package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
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
    @Mock
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecute() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(300));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(600));
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(new TaskModel());
        when(systemTask.isAsyncComplete(any())).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly("testTaskId"));
    }

    @Test
    public void testPostponeQuietly() throws Exception {
        doThrow(new RuntimeException()).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly("queueName", new TaskModel());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}
