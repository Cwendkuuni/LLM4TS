package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.workflow.WorkflowExecutor;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
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
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        String taskId = "task1";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowInstanceId("workflow1");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowModel.getWorkflowInstanceId(), true)).thenReturn(workflowModel);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(200L);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(100L);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
    }

    @Test
    public void testExecuteTaskNotFound() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        String taskId = "task1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // No exception should be thrown
        assertTrue(true);
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task1");

        String queueName = "testQueue";

        when(queueDAO.postpone(queueName, taskModel.getTaskId(), taskModel.getWorkflowPriority(), 100L)).thenReturn(true);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // No exception should be thrown
        assertTrue(true);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(taskModel);
    }

    @Test
    public void testLoadTaskQuietlyTaskNotFound() {
        String taskId = "task1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(taskModel);
    }
}