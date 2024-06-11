package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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
        String taskId = "12345";

        TaskModel task = new TaskModel();
        task.setTaskType("SAMPLE_TASK_TYPE");
        task.setTaskId("12345");
        task.setWorkflowInstanceId("54321");
        
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("54321", false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertNotNull(task.getStartTime());
        assertNotNull(task.getCallbackAfterSeconds());
        verify(systemTask, times(1)).start(eq(workflow), eq(task), eq(workflowExecutor));
    }

    @Test
    public void testExecuteNotFoundTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that executionDAOFacade.getTaskModel was called
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        // Verify that systemTask.start was not called
        verify(systemTask, never()).start(any(), any(), any());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel task = new TaskModel();
        task.setTaskId("12345");

        String queueName = QueueUtils.getQueueName(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        
        verify(queueDAO, times(1)).postpone(eq(queueName), eq("12345"), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "12345";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(loadedTask);
        assertEquals("12345", loadedTask.getTaskId());
    }

    @Test
    public void testLoadTaskQuietlyNotFound() {
        String taskId = "12345";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
    }
}