package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.workflow.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecuteTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setWorkflowInstanceId("456");
        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("456", false)).thenReturn(new WorkflowModel());

        // Test execution for non-terminal task
        task.setStatus(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, "123");
        assertEquals(TaskModel.Status.IN_PROGRESS, task.getStatus());

        // Test execution for terminal task
        task.setStatus(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "123");
        verify(queueDAO, times(1)).remove(anyString(), anyString());

        // Test execution for null task
        when(executionDAOFacade.getTaskModel("123")).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "123");
        assertNull(executionDAOFacade.getTaskModel("123"));
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        String queueName = "testQueue";
        doNothing().when(queueDAO).postpone(eq(queueName), eq("123"), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(queueName, "123", task.getWorkflowPriority(), asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs());
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("123");
        assertEquals("123", loadedTask.getTaskId());

        TaskModel nullTask = asyncSystemTaskExecutor.loadTaskQuietly("456");
        assertNull(nullTask);
    }
}