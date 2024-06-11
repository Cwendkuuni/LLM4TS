package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.worker.WorkerContext;

import java.time.Duration;
import java.time.Instant;

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

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private Monitors monitors;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_nullTask_noActionTaken() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_completedTask_noActionTaken() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_terminalWorkflow_taskRemovedFromQueue() {
        WorkflowModelImpl workflowModel = new WorkflowModelImpl();
        workflowModel.setStatus(WorkflowInstanceStatus.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("workflowId");

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_inProgressLimitExceeded_taskPostponed() {
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_rateLimitExceeded_taskPostponed() {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenThrow(new IllegalStateException("RateLimitExceeded"));
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskExecutionCompleted_taskRemovedFromQueue() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.RUNNING);
        when(systemTaskCallbackTime).thenReturn(Duration.ofSeconds(5).toMillis());
        when(task.getCallbackAfterSeconds()).thenReturn(0L);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_taskExecutionInProgress_taskPostponed() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(systemTaskCallbackTime).thenReturn(Duration.ofSeconds(5).toMillis());
        when(task.getCallbackAfterSeconds()).thenReturn(0L);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), systemTaskCallbackTime);
    }

    @Test
    public void testExecute_taskExecutionFailed_taskPostponed() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(systemTaskCallbackTime).thenReturn(Duration.ofSeconds(5).toMillis());
        when(task.getCallbackAfterSeconds()).thenReturn(0L);

        doThrow(new RuntimeException("TestException")).when(workflowSystemTask).execute(workflow, task, workflowExecutor);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), systemTaskCallbackTime);
    }

    @Test
    public void testExecute_taskExecutionCompleted_workflowDecided() {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.RUNNING);
        when(systemTaskCallbackTime).thenReturn(Duration.ofSeconds(5).toMillis());
        when(task.getCallbackAfterSeconds()).thenReturn(0L);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(workflowExecutor).decide("workflowId");
    }

    @Test
    public void testPostponeQuietly_queuePostponed() {
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskLoaded() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertEquals(task, loadedTask);
    }
}