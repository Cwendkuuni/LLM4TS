package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;

@SuppressWarnings("deprecation")
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private Monitors monitors;

    private String taskId = "taskId";
    private String queueName = "queueName";
    private String workflowId = "workflowId";
    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        systemTask = new WorkflowSystemTaskImpl();
        when(workflowExecutor.decide(workflowId)).thenReturn(null);
    }

    @After
    public void tearDown() {
        if (task != null) {
            when(executionDAOFacade.updateTask(task)).thenCallRealMethod();
        }
    }

    @Test
    public void testExecute_taskNotFound() {
        assertNull(asyncSystemTaskExecutor.execute(systemTask, taskId));
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        task.setStatus(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertFalse(queueDAO.contains(queueName, taskId));
    }

    @Test
    public void testExecute_taskScheduled_exceedsInProgressLimit() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertFalse(queueDAO.contains(queueName, taskId));
        assertSame(ExecutionStatus.CONCURRENT_LIMIT_EXCEEDED, task.getReasonForIncompletion());
    }

    @Test
    public void testExecute_taskScheduled_exceedsRateLimitPerFrequency() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertFalse(queueDAO.contains(queueName, taskId));
        assertSame(ExecutionStatus.RATE_LIMIT_EXCEEDED, task.getReasonForIncompletion());
    }

    @Test
    public void testExecute_taskInProgress() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertSame(TaskModel.Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    public void testExecute_taskCompleted() {
        task.setStatus(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertSame(TaskModel.Status.COMPLETED, task.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_exceptionDuringExecution() {
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.RUNNING);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        throw new RuntimeException("Exception during task execution");
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        assertSame(queueName, queueDAO.getQueueName(task.getTaskId()));
        assertFalse(queueDAO.contains(queueName, task.getTaskId()));
        assertSame(queueTaskMessagePostponeSecs, task.getCallbackAfterSeconds());
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        assertSame(task, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
}