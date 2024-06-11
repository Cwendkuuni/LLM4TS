package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor);
    }

    @Test
    public void testExecute_withExistingTask() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskDefName("test_task");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getWorkflowModel("workflow_1", true)).thenReturn(workflowModel);
        when(executionDAOFacade.getTaskModel("1")).thenReturn(taskModel);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "1");

        // Assert statements for the expected behavior
    }

    @Test
    public void testExecute_withNonExistingTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel("2")).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, "2");

        // Assert statements for the expected behavior
    }

    @Test
    public void testExecute_withTerminalWorkflow() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("3");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow_2");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getWorkflowModel("workflow_2", true)).thenReturn(workflowModel);
        when(executionDAOFacade.getTaskModel("3")).thenReturn(taskModel);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "3");

        // Assert statements for the expected behavior
    }

    @Test
    public void testExecute_withException() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel("4")).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, "4");

        // Assert statements for the expected behavior
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("5");

        String queueName = "test_queue";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // Assert statements for the expected behavior
    }

    @Test
    public void testLoadTaskQuietly_withExistingTask() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("6");

        when(executionDAOFacade.getTaskModel("6")).thenReturn(taskModel);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("6");

        assertEquals(taskModel, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_withNonExistingTask() {
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("7");

        assertNull(loadedTask);
    }

}