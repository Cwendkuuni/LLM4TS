package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;import org.mockito.Mock;import static org.mockito.Mockito.verify;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;

@SuppressWarnings("unchecked")
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

    @Before
    public void init() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(new TaskModel());
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(new WorkflowModel());
        when(queueDAO.remove(QueueUtils.getQueueName("task"), "taskId")).thenReturn(true);
        when(queueDAO.postpone(QueueUtils.getQueueName("task"), "taskId", 0, 5000)).thenReturn(true);
        when(queueDAO.getWorkflowPriority("workflowId")).thenReturn(0);
        when(metadataDAO.getTaskDef("taskDefName")).thenReturn(new TaskModel());
        when(workflowExecutor.decide("workflowId")).thenReturn(true);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.removeCalled);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName("task"), taskId);
    }

    @Test
    public void testExecute_concurrencyLimitExceeded() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertSame(task, executionDAOFacade.getTaskModel(taskId));
        verify(queueDAO).postpone(QueueUtils.getQueueName("task"), taskId, 0, 5000);
    }

    @Test
    public void testExecute_rateLimitExceeded() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertSame(task, executionDAOFacade.getTaskModel(taskId));
        verify(queueDAO).postpone(QueueUtils.getQueueName("task"), taskId, 0, 5000);
    }

    @Test
    public void testExecute_workflowNotFound() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_workflowCompleted() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName("task"), taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        String taskId = "taskId";

        asyncSystemTaskExecutor.postponeQuietly(queueName, new TaskModel());

        verify(queueDAO).postpone(queueName, taskId, 0, 5000);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "taskId";

        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        assertSame(task, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
}