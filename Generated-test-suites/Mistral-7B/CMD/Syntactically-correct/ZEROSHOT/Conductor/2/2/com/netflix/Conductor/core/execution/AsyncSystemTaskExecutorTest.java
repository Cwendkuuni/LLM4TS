package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
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
import com.netflix.conductor.utils.PropertiesUtil;

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
    private WorkflowSystemTask systemTask;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PropertiesUtil.setProperty("conductor.systemTaskWorkerCallbackDuration.seconds", "10");
        PropertiesUtil.setProperty("conductor.taskExecutionPostponeDuration.seconds", "5");
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                java.time.Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                java.time.Duration.ofSeconds(5));
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(new TaskModel());
        when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
                .thenReturn(false);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(new WorkflowModel());
        when(metadataDAO.getTaskDef("taskDefName")).thenReturn(new TaskModel());
        when(queueDAO.remove("queueName", "taskId")).thenReturn(true);
        when(queueDAO.postpone("queueName", "taskId", 1, 5)).thenReturn(true);
        when(queueDAO.remove("queueName", "taskId")).thenReturn(true);
        when(queueDAO.getQueueMessage("queueName", "taskId")).thenReturn(new QueueMessage());
        when(workflowExecutor.decide("workflowId")).thenReturn(true);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_taskIsNull_queueMessageIsRemoved() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove("queueName", taskId);
    }

    @Test
    public void testExecute_taskIsCompleted_queueMessageIsRemoved() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove("queueName", taskId);
    }

    @Test
    public void testExecute_taskIsInProgress_queueMessageIsPostponed() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        QueueMessage queueMessage = new QueueMessage();
        verify(queueDAO).postpone("queueName", taskId, 1, 5);
        verify(queueDAO).getQueueMessage("queueName", taskId).equals(queueMessage);
    }

    @Test
    public void testExecute_taskIsScheduled_queueMessageIsPostponed() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        QueueMessage queueMessage = new QueueMessage();
        verify(queueDAO).postpone("queueName", taskId, 1, 5);
        verify(queueDAO).getQueueMessage("queueName", taskId).equals(queueMessage);
    }

    @Test
    public void testExecute_taskRateLimitIsExceeded_queueMessageIsPostponed() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        when(metadataDAO.getTaskDef("taskDefName")).thenReturn(task);
        task.setRateLimitPerFrequency(1);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        QueueMessage queueMessage = new QueueMessage();
        verify(queueDAO).postpone("queueName", taskId, 1, 5);
        verify(queueDAO).getQueueMessage("queueName", taskId).equals(queueMessage);
    }

    @Test
    public void testExecute_taskExecutionLimitIsExceeded_queueMessageIsPostponed() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        QueueMessage queueMessage = new QueueMessage();
        verify(queueDAO).postpone("queueName", taskId, 1, 5);
        verify(queueDAO).getQueueMessage("queueName", taskId).equals(queueMessage);
    }

    @Test
    public void testExecute_taskIsCompleted_workflowIsDecided() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_taskIsTerminal_workflowIsDecided() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_taskIsFailed_workflowIsDecided() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.FAILED);
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor).decide(workflowId);
    }
}