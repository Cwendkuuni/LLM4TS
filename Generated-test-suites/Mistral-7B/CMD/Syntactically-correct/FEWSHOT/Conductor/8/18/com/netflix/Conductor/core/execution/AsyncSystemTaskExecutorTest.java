package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowInstanceView;
import com.netflix.conductor.utils.concurrent.RateLimitPerFrequency;

public class AsyncSystemTaskExecutorTest {

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
    private RateLimitPerFrequency rateLimitPerFrequency;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private String queueName;
    private String taskId;
    private String workflowId;
    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        this.queueName = "queueName";
        this.taskId = "taskId";
        this.workflowId = "workflowId";
        this.task = mock(TaskModel.class);
        this.workflow = mock(WorkflowModel.class);
        this.systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(queueDAO).thenReturn(new QueueDAOImpl());
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(rateLimitPerFrequency);
        when(workflowExecutor.decide(workflowId)).thenReturn(new Object());
        when(monitors.recordQueueWaitTime(task.getTaskType(), Long.MAX_VALUE)).thenReturn(null);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertTrue(task.isTerminal());
        assertTrue(queueDAO.remove(queueName, taskId) > 0);
    }

    @Test
    public void testExecute_TaskInProgress() {
        this.task.setStatus(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.remove(queueName, taskId) > 0);
        assertSame(TaskModel.Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    public void testExecute_TaskCompleted() {
        this.task.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.remove(queueName, taskId) > 0);
        assertSame(TaskModel.Status.COMPLETED, task.getStatus());
    }

    @Test
    public void testExecute_TaskFailed() {
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.FAILED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertNull(queueDAO.remove(queueName, taskId));
        assertSame(TaskModel.Status.CANCELED, task.getStatus());
    }

    @Test
    public void testExecute_TaskTerminal() {
        this.workflow.setStatus(WorkflowInstanceStatus.COMPLETED);
        this.task.setStatus(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertTrue(queueDAO.remove(queueName, taskId) > 0);
        assertSame(TaskModel.Status.CANCELED, task.getStatus());
    }

    @Test
    public void testExecute_TaskQueueSizeLimit() {
        this.task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.remove(queueName, taskId) > 0);
    }

    @Test
    public void testExecute_TaskRateLimitPerFrequency() {
        this.task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, rateLimitPerFrequency)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.remove(queueName, taskId) > 0);
    }

    @Test
    public void testExecute_TaskQueuePostpone() throws InterruptedException {
        this.task.setStatus(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        when(queueDAO.get(queueName, taskId)).thenReturn(task);
        when(monitors.recordQueueWaitTime(task.getTaskType(), Long.MAX_VALUE)).thenReturn(5000L);
        when(workflowExecutor.decide(workflowId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertTrue(queueDAO.postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs) > 0);
    }

    @Test
    public void testExecute_TaskAsyncComplete() {
        this.task.setStatus(TaskModel.Status.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.remove(queueName, taskId) > 0);
    }
}