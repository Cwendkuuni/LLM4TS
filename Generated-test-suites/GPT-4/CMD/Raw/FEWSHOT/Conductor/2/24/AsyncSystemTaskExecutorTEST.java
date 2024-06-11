package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Duration;
import static org.junit.Assert.*;

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

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
     
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = new WorkflowSystemTask("Test");
        String taskId = "testTaskId";
        
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade).getTaskModel(taskId);
    }
    
    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task1");
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        verify(queueDAO).postpone(queueName, taskModel.getTaskId(), taskModel.getWorkflowPriority(), conductorProperties.getTaskExecutionPostponeDuration().getSeconds());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        
        assertEquals(taskId, result.getTaskId());
    }
}