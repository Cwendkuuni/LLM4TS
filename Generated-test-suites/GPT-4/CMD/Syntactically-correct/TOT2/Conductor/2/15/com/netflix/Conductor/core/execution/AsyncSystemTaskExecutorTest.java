package com.netflix.conductor.core.execution;
import org.junit.Test;
import org.junit.Before;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.netflix.conductor.core.execution.*;
import com.netflix.conductor.model.*;
import com.netflix.conductor.core.dal.*;
import com.netflix.conductor.dao.*;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteTaskDoesNotExist() {
        String taskId = "task1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(any(), eq(taskId));
    }

    @Test
    public void testExecuteWithWorkflow() {
        String taskId = "task1";
        String workflowId = "workflow1";
        TaskModel taskModel = new TaskModel();
        taskModel.setWorkflowInstanceId(workflowId);

        WorkflowModel workflowModel = new WorkflowModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(any());
        verify(workflowExecutor, times(1)).decide(any());
    }


    @Test(expected = RuntimeException.class)
    public void testExecuteWithException() {
        String taskId = "task1";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(RuntimeException.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }
 
    @Test
    public void testLoadTaskQuietlyWithException() {
        String taskId = "task1";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(RuntimeException.class);

        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task1";
        TaskModel taskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        assertEquals(taskModel, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
    
    @Test(expected = RuntimeException.class)
    public void testPostponeQuietlyWithException() {
        String queueName = "queue1";
        TaskModel taskModel = new TaskModel();
        
        doThrow(new RuntimeException()).when(queueDAO).postpone(queueName, taskModel.getTaskId(), 
            taskModel.getWorkflowPriority(), asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
        
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
    }
}