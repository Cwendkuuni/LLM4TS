package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.DummyWorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

@SuppressWarnings("deprecation")
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

    private DummyWorkflowSystemTask dummyWorkflowSystemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Duration.ofSeconds(100));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Duration.ofSeconds(500));
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(getDummyWorkflowModel());
        when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("testQueue");
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(true);
        when(queueDAO.remove(anyString(), anyString()))
                .thenThrow(new RuntimeException("Queue removal failed"));
        when(queueDAO.get(anyString()))
                .thenReturn(Optional.of(new com.netflix.conductor.core.model.QueueModel()));
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(getDummyTaskModel());
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(true);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class)))
                .thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any()))
                .thenReturn(false);
        when(workflowExecutor.decide(anyString())).thenReturn(true);
        dummyWorkflowSystemTask = new DummyWorkflowSystemTask("testTaskType", "testTaskId");
    }

    @Test
    public void testExecuteScheduledTask() {
        String taskId = "testTaskId";
        TaskModel task = getDummyTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove( QueueUtils.getQueueName(task), taskId);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testExecuteInProgressTask() {
        String taskId = "testTaskId";
        TaskModel task = getDummyTaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).postpone("testQueue", taskId, 1, 100);
        // Expected interactions order may vary
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
    }

    @Test
    public void testExecuteCompletedTask() {
        String taskId = "testTaskId";
        TaskModel task = getDummyTaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecuteTerminalTask() {
        String taskId = "testTaskId";
        TaskModel task = getDummyTaskModel();
        task.setStatus(TaskModel.Status.FAILED);

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecuteTaskWithInProgressLimitExceeded() {
        String taskId = "testTaskId";
        TaskModel task = getDummyTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(queueDAO).postpone("testQueue", taskId, 1, 500);
        verify(queueDAO, never()).remove(QueueUtils.getQueueName(task), taskId);
        verify(executionDAOFacade, never()).updateTask(task);
    }

    @Test
    public void testExecuteTaskWithRateLimitExceeded() {
        String taskId = "testTaskId";
        TaskModel task = getDummyTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(queueDAO).postpone("testQueue", taskId, 1, 500);
        verify(queueDAO, never()).remove(QueueUtils.getQueueName(task), taskId);
        verify(executionDAOFacade, never()).updateTask(task);
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteTaskWithQueueRemovalFailure() {
        String taskId = "testTaskId";
        TaskModel task = getDummyTaskModel();

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove( QueueUtils.getQueueName(task), taskId);
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    private TaskModel getDummyTaskModel() {
        TaskModel task = new TaskModel();
        task.setTaskDefName("testTaskDefName");
        task.setTaskId("testTaskId");
        task.setWorkflowInstanceId("testWorkflowInstanceId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        return task;
    }

    private WorkflowModel getDummyWorkflowModel() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("testWorkflowId");
        workflow.setName("testWorkflowName");
        workflow.setStatus(WorkflowStatus.RUNNING);
        workflow.setInstanceStatus(WorkflowInstanceStatus.RUNNING);
        return workflow;
    }
}