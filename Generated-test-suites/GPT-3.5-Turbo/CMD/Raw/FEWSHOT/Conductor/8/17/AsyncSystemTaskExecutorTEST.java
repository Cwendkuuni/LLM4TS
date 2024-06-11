package com.netflix.conductor.core.execution;

import static org.mockito.Mockito.*;

import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.ExecutionDAOFacade;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
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
    public void setup() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskType("TEST_TASK");
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow123");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(eq("workflow123"), anyBoolean())).thenReturn(workflowModel);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(systemTask, times(1)).start(any(WorkflowModel.class), eq(taskModel), eq(workflowExecutor));
        verify(executionDAOFacade, times(1)).updateTask(eq(taskModel));
        verify(workflowExecutor, times(1)).decide(eq("workflow123"));
    }

    @Test
    public void testExecute_TaskNull() {
        String taskId = "12345";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        
        // No interactions with other mocks as task is null
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskType("TEST_TASK");
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow123");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(eq("workflow123"), anyBoolean())).thenReturn(workflowModel);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(taskModel, times(1)).setStatus(eq(TaskModel.Status.CANCELED));
        verify(executionDAOFacade, times(1)).updateTask(eq(taskModel));
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("123");
        String queueName = "testQueue";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq("123"), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(taskModel);
    }
}