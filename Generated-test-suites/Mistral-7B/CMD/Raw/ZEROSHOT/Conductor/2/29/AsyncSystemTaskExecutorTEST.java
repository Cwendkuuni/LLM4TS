package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor, conductorProperties);
    }

    @Test
    public void testExecute_taskIsNull() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_terminalWorkflow() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        when(task.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_taskInQueueWithScheduledStatus() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(queueDAO.remove("queueName", "taskId")).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).postpone("queueName", "taskId", 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskInQueueWithInProgressStatus() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, times(2)).postpone("queueName", "taskId", 0, anyLong());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_executionLimitExceeded() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).postpone("queueName", "taskId", 0, queueTaskMessagePostponeSecs);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_rateLimitExceeded() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).postpone("queueName", "taskId", 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskCompletion() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, never()).remove("queueName", "taskId");
        verify(queueDAO, times(2)).postpone("queueName", "taskId", 0, anyLong());
        verify(workflowExecutor).decide("workflowId");
    }

    @Test
    public void testExecute_asyncTaskCompletion() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowStatus.IN_PROGRESS);
        when(task.getCallbackAfterSeconds()).thenReturn(1000L);
        when(task.getEndTime()).thenReturn(System.currentTimeMillis());
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove("queueName", "taskId");
        verify(workflowExecutor, never()).decide("workflowId");
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = QueueUtils.getQueueName(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, "taskId", 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_nullTaskModel() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        assertNull(loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_validTaskModel() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        assertEquals(task, loadedTask);
    }
}