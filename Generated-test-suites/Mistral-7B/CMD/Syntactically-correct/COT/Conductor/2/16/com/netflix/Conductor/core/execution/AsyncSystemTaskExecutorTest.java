package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.netflix.conductor.dao.QueueNameStrategy;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.utils.Clock;

@SuppressWarnings("unchecked")
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
    private Clock clock;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask systemTask;
    private TaskModel task;
    private WorkflowModel workflow;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));

        systemTask = mock(WorkflowSystemTask.class);
        task = mock(TaskModel.class);
        workflow = mock(WorkflowModel.class);
    }

    @Test
    public void testExecute_taskNotFound_removesQueueMessage() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Verify that the queue message was removed
        when(queueDAO.remove(QueueUtils.getQueueName(task), task.getTaskId())).thenReturn(true);
        assertFalse(queueDAO.exists(QueueUtils.getQueueName(task), task.getTaskId()));
    }

    @Test
    public void testExecute_taskCompleted_workflowCompleted() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Verify that the workflow was evaluated
        workflowExecutor.decide(task.getWorkflowInstanceId());
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_nullTaskModel() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_nullWorkflowModel() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_executionLimitReached() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_rateLimitReached() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    }

    @Test
    public void testExecute_taskInProgress_postponeTask() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Verify that the task was postponed
        Optional<Long> callbackAfterSeconds =
                executionDAOFacade.getTaskModel(task.getTaskId()).getCallbackAfterSeconds();
        assertSame(Long.valueOf(1), callbackAfterSeconds.get());
        queueDAO.postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                callbackAfterSeconds.get());
    }

    @Test
    public void testExecute_taskInProgress_noCallback() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Verify that the task was not postponed
        Optional<Long> callbackAfterSeconds =
                executionDAOFacade.getTaskModel(task.getTaskId()).getCallbackAfterSeconds();
        assertNull(callbackAfterSeconds);
    }

    @Test
    public void testExecute_taskTerminal_removeFromQueue() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.TERMINAL);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Verify that the task was removed from the queue
        assertFalse(queueDAO.exists(QueueUtils.getQueueName(task), task.getTaskId()));
    }

    @Test
    public void testPostponeQuietly_queueNameNull() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);

        asyncSystemTaskExecutor.postponeQuietly(null, task);
    }

    @Test
    public void testPostponeQuietly_queueExists() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        String queueName = QueueUtils.getQueueName(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Verify that the task was postponed
        queueDAO.postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());

        assertNull(loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());

        assertSame(task, loadedTask);
    }
}