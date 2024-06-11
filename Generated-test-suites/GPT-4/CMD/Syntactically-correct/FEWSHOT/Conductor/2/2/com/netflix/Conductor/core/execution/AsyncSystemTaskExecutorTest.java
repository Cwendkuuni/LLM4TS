package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import com.netflix.conductor.core.config.ConductorConfig;
import com.netflix.conductor.dao.MockQueueDAO;
import com.netflix.conductor.core.utils.IDGenerator;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private ConductorConfig conductorProperties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowSystemTask workflowSystemTask;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private TaskModel task;
    private String taskId;

    @Before
    public void setUp() {
        
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade,queueDAO,metadataDAO,conductorProperties,workflowExecutor);

        this.taskId = UUID.randomUUID().toString();
        this.task = new TaskModel();
        this.task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
    }

    @Test
    public void testExecute() {
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = UUID.randomUUID().toString();
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(returnedTask, task);
    }

    @Test
    public void testLoadTaskQuietlyThrowsException() {
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
}