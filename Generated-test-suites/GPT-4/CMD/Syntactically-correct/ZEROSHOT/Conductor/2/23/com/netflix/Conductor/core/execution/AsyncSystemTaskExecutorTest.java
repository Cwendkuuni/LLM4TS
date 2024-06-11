
package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.time.Duration;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));
    }

    @Test
    public void testExecute() {
        String taskId = "task1";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testExecute_NullTask() {
        String taskId = "task1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue1";
        TaskModel task = new TaskModel();
        task.setTaskId("task1");
        task.setWorkflowPriority(1);
        
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(task.getTaskId()), eq(task.getWorkflowPriority()), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task1";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assert result.getTaskId().equals(task.getTaskId());
    }
}
