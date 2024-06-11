package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Optional;

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
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private QueueMessage queueMessage;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflowExecutor.decide(anyString())).thenReturn(Optional.empty());
        when(queueDAO.get(anyString(), anyLong())).thenReturn(queueMessage);
        when(queueDAO.postpone(anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.empty());
        when(queueDAO.remove(anyString(), anyLong())).thenReturn(true);
        when(queueDAO.update(any(QueueMessage.class))).thenReturn(true);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskModel());
    }

    @Test
    public void testExecute_nullTask() {
        asyncSystemTaskExecutor.execute(systemTask, null);
        assertNull(task);
    }

    @Test
    public void testExecute_completedTask() {
        task.setStatus(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        assertTrue(queueDAO.remove(QueueUtils.getQueueName(task), task.getTaskId()));
    }

    @Test
    public void testExecute_terminalWorkflow() {
        workflow.setStatus(WorkflowInstanceStatus.COMPLETED);
        when(workflow.getStatus()).thenReturn(WorkflowStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        assertTrue(queueDAO.remove(QueueUtils.getQueueName(task), task.getTaskId()));
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_inProgressWorkflow() {
        workflow.setStatus(WorkflowInstanceStatus.RUNNING);
        when(workflow.getStatus()).thenReturn(WorkflowStatus.RUNNING);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_queueMessageNotFound() {
        when(queueDAO.get(QueueUtils.getQueueName(task), task.getTaskId())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    }

    @Test
    public void testExecute_taskInProgress() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        assertFalse(queueDAO.remove(QueueUtils.getQueueName(task), task.getTaskId()));
    }

    @Test
    public void testExecute_taskScheduled() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        assertFalse(queueDAO.remove(QueueUtils.getQueueName(task), task.getTaskId()));
        assertSame(queueMessage, queueDAO.get(QueueUtils.getQueueName(task), task.getTaskId()));
    }

    @Test
    public void testExecute_taskExceedsInProgressLimit() {
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        assertTrue(queueDAO.postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_taskExceedsRateLimitPerFrequency() {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        assertTrue(queueDAO.postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs));
    }

    @Test
    public void testPostponeQuietly_queueMessageNotFound() {
        asyncSystemTaskExecutor.postponeQuietly(QueueUtils.getQueueName(task), task);
    }

    @Test
    public void testPostponeQuietly_queueMessageFound() {
        when(queueDAO.get(QueueUtils.getQueueName(task), task.getTaskId())).thenReturn(queueMessage);
        asyncSystemTaskExecutor.postponeQuietly(QueueUtils.getQueueName(task), task);
        assertTrue(queueDAO.update(queueMessage));
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());
        assertNull(task);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());
        assertSame(task, executionDAOFacade.getTaskModel(task.getTaskId()));
    }

    @Test(expected = InvocationTargetException.class)
    public void testExecute_exceptionInTaskExecution() throws Exception {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenThrow(new Exception("Exception in WorkflowModel"));
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    }
}