package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskMock;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.core.execution.error.TaskException;
import com.netflix.conductor.core.execution.error.WorkflowException;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "task123";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskMock();
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = "task123";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskMock();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "task123";
        TaskModel expectedTask = new TaskModel();
        expectedTask.setTaskId(taskId);
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(expectedTask);
        
        TaskModel actualTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(actualTask);
        assertEquals(expectedTask.getTaskId(), actualTask.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "task123";
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        TaskModel actualTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(actualTask);
    }
}