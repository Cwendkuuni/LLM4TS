package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

import static org.mockito.Mockito.mock;

public class AsyncSystemTaskExecutorTest {
    
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacade,
            queueDAO,
            metadataDAO,
            conductorProperties,
            workflowExecutor
        );
    }
    
    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("testTask");
        
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowInstanceId("workflow123");
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(eq("workflow123"), eq(false))).thenReturn(workflowModel);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(systemTask.isAsyncComplete(eq(taskModel))).thenReturn(true);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verify(executionDAOFacade, times(1)).updateTask(eq(taskModel));
        verify(workflowExecutor, times(1)).decide(eq("workflow123"));
    }
    
    @Test
    public void testExecuteTaskNotFound() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        // Verify that the task is not updated or removed from the queue
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }
    
    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setWorkflowPriority(1);
        
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        
        verify(queueDAO, times(1)).postpone(eq(queueName), eq("123"), eq(1), anyLong());
    }
    
    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";
        
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(taskModel);
        
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        
        assertEquals(taskModel, loadedTask);
    }
    
    @Test
    public void testLoadTaskQuietlyTaskNotFound() {
        String taskId = "123";
        
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenThrow(new RuntimeException("Task not found"));
        
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        
        assertNull(loadedTask);
    }
}