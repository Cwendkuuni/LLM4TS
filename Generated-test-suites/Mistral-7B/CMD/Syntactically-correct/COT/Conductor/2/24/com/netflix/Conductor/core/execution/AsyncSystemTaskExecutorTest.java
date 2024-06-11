package com.netflix.conductor.core.execution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
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
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.workflow.WorkflowInstance;
import com.netflix.conductor.utils.mocks.MockWorkflowExecutor;

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

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowInstance workflowInstance;

    @Mock
    private QueueMessage queueMessage;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private MockWorkflowExecutor mockWorkflowExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(mock(TaskModel.class));
        when(queueDAO.getQueueMessage(anyString(), anyString())).thenReturn(queueMessage);
        when(queueMessage.getTaskId()).thenReturn("taskId");
        when(queueMessage.getWorkflowPriority()).thenReturn(1);
        when(workflowExecutor.decide(anyString())).thenReturn(null);
        when(workflowExecutor.getWorkflowExecutor()).thenReturn(mockWorkflowExecutor);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the task is removed from the queue
        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_TaskCompleted() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the task is removed from the queue
        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_TaskTerminal() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.FAILED);
        when(workflowModel.getStatus()).thenReturn(WorkflowInstance.State.FAILED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the task is removed from the queue
        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_TaskExecutionLimitReached() {
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the task is postponed
        verify(queueDAO).postpone("queueName", "taskId", 1, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_RateLimitPerFrequencyExceeded() {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, mock(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the task is postponed
        verify(queueDAO).postpone("queueName", "taskId", 1, 1);
    }

    @Test
    public void testExecute_TaskScheduled() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the task is updated and the workflow is evaluated
        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide("workflowId");
    }

    @Test
    public void testExecute_TaskInProgress() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the task is updated and the workflow is evaluated
        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide("workflowId");
    }

    @Test
    public void testExecute_TaskAsyncComplete() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the task is removed from the queue
        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_TaskError() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        // Verify that the error is logged and the task is postponed
        verify(Monitors).error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
        verify(queueDAO).postpone("queueName", "taskId", 1, 1);
    }
}