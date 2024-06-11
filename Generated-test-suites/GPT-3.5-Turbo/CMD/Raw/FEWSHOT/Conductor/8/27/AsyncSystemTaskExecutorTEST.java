package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private final ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
    private final QueueDAO queueDAO = mock(QueueDAO.class);
    private final MetadataDAO metadataDAO = mock(MetadataDAO.class);
    private final ConductorProperties conductorProperties = mock(ConductorProperties.class);
    private final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

    private final AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

    @Test
    public void testExecute() {
        // Setup
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskType("taskType");
        taskModel.setWorkflowInstanceId("workflow123");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Workflow.WorkflowStatus.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflow123", false)).thenReturn(workflowModel);

        // Test
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify
        assertNotNull(taskModel.getEndTime());
        assertNull(taskModel.getReasonForIncompletion());
        assertNotNull(taskModel.getCallbackAfterSeconds());
    }

    @Test
    public void testExecute_withNullTask() {
        // Setup
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        // Test
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify
        // Verify that nothing happens
    }

    @Test
    public void testPostponeQuietly() {
        // Setup
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task123");
        String queueName = "queue1";

        // Test
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // Verify
        // Verify that no exception is thrown
    }

    @Test
    public void testLoadTaskQuietly() {
        // Setup
        String taskId = "task123";

        // Test
        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Verify
        assertNull(taskModel);
    }
}