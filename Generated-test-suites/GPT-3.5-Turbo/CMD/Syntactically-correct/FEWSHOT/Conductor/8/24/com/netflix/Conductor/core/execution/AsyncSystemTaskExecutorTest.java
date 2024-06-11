package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskType("testTask");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflow123");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.getQueueName(task)).thenReturn("testQueue");

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide("workflow123");
    }

    @Test
    public void testExecuteTaskNotFound() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verifyZeroInteractions(queueDAO);
        verifyZeroInteractions(workflowExecutor);
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel task = new TaskModel();
        task.setTaskId("task123");
        
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);

        asyncSystemTaskExecutor.postponeQuietly("testQueue", task);

        verify(queueDAO, times(1)).postpone("testQueue", "task123", 0, 60L);
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel("task123")).thenReturn(new TaskModel());

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly("task123");

        assertNotNull(task);
    }

    @Test
    public void testLoadTaskQuietlyTaskNotFound() {
        when(executionDAOFacade.getTaskModel("task123")).thenReturn(null);

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly("task123");

        assertNull(task);
    }
}