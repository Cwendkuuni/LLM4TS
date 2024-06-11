package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.metadata.test.TestTaskDef;
import com.netflix.conductor.utils.QueueUtils;

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
        queueDAO = new QueueDAOImpl();
        metadataDAO = new MetadataDAOImpl();
        conductorProperties = Mockito.spy(new ConductorProperties());
        workflowExecutor = mock(WorkflowExecutor.class);
        AsyncSystemTaskExecutor asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);
        this.asyncSystemTaskExecutor = asyncSystemTaskExecutor;
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "123";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove("taskQueue", taskId);
    }

    @Test
    public void testExecute_terminalTask() {
        String taskId = "123";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new TestWorkflowModel(workflowId, "completed");
        TaskModel taskModel = new TestTaskModel(taskId, workflowId, "completed");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove("queueName", taskId);
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        String taskId = "123";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new TestWorkflowModel(workflowId, "SCHEDULED");
        TaskModel taskModel = new TestTaskModel(taskId, workflowId, "SCHEDULED");
        taskModel.setMaxConcurrentExecutions(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        doReturn(true).when(executionDAOFacade).exceedsInProgressLimit(taskModel);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone("queueName", taskId, 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        String taskId = "123";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new TestWorkflowModel(workflowId, "SCHEDULED");
        TaskModel taskModel = new TestTaskModel(taskId, workflowId, "SCHEDULED");
        taskModel.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        doReturn(true).when(executionDAOFacade).exceedsRateLimitPerFrequency(taskModel, mock(TaskDef.class));

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone("queueName", taskId, 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_workflowCompleted() {
        String taskId = "123";
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new TestWorkflowModel(workflowId, "completed");
        TaskModel taskModel = new TestTaskModel(taskId, workflowId, "SCHEDULED");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testPostponeQuietly_taskExists() {
        String queueName = "queueName";
        String taskId = "taskId";

        asyncSystemTaskExecutor.postponeQuietly(queueName, new TestTaskModel(taskId, null, "SCHEDULED"));

        verify(queueDAO, times(1)).postpone(queueName, taskId, 0, queueTaskMessagePostponeSecs);
    }

    @Test(expected = Exception.class)
    public void testLoadTaskQuietly_exception() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("Failed to load task"));

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);
    }

    private static class TestTaskModel extends TaskModel {
        public TestTaskModel(String id, String workflowInstanceId, String status) {
            super(id, workflowInstanceId, status, null, null, null, null, null, null, null, null);
        }
    }

    private static class TestWorkflowModel extends WorkflowModel {
        public TestWorkflowModel(String id, String status) {
            super(id, status, null, null, null, null, null, null, null, null, null);
        }
    }
}