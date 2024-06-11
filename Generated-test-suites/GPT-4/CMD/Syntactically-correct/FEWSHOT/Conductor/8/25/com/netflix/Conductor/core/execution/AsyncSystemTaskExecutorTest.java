package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {
    
    private ExecutionDAOFacade executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
    private QueueDAO queueDAO = Mockito.mock(QueueDAO.class);
    private MetadataDAO metadataDAO = Mockito.mock(MetadataDAO.class);
    private WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
    private ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
    private WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties,workflowExecutor);

    @Test
    public void testExecute() {

        String taskId = "mockTaskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(any(), any());
    }

    @Test
    public void testPostponeQuietly() {

        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setTaskId("mockTaskId");

        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(30));
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(any(), any(), anyInt(), anyLong());
    } 
    
    @Test
    public void testLoadTaskQuietly() {
        String taskId = "mockTaskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(taskId, result.getTaskId());
    }

}