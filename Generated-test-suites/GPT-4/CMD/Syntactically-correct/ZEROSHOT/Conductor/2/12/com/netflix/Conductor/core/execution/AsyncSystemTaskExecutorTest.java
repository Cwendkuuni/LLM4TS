
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, 
                queueDAO, 
                metadataDAO, 
                conductorProperties, 
                workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        // Run test method
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        // Verify interactions
        Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(taskId);
        Mockito.verify(systemTask, Mockito.times(1)).getTaskType();
        Mockito.verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testPostponeQuietly() {
        // Setup
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        String queueName = "queueName";
        
        // Run method under test
        asyncSystemTaskExecutor.getClass().getDeclaredMethod("postponeQuietly", String.class, TaskModel.class)
                    .invoke(asyncSystemTaskExecutor, queueName, task);

        // Verify interactions
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyLong()
        );
    }

    @Test
    public void testLoadTaskQuietly() {
        // Setup
        String taskId = "taskId";
        
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        // Run method under test
        TaskModel result = asyncSystemTaskExecutor.getClass().getDeclaredMethod("loadTaskQuietly", String.class)
                    .invoke(asyncSystemTaskExecutor, taskId);
      
        // Assertions
        Assert.assertNull(result);
    }
}
