package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowInstanceId("workflow123");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflow123", true)).thenReturn(workflowModel);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_NullTask() {
        String taskId = "task123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        // Verify no interactions with other objects
        verifyNoInteractions(systemTask);
        verifyNoInteractions(executionDAOFacade);
        verifyNoInteractions(queueDAO);
        verifyNoInteractions(metadataDAO);
        verifyNoInteractions(conductorProperties);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_ServiceTaskIncomplete() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowInstanceId("workflow123");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflow123", true)).thenReturn(workflowModel);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_NullWorkflow() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowInstanceId("workflow123");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflow123", true)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel task = new TaskModel();
        task.setTaskId("task123");
        String queueName = "testQueue";
        
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1))
            .postpone(eq(queueName), eq("task123"), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task123";
        
        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(eq("task123"));
    }
}