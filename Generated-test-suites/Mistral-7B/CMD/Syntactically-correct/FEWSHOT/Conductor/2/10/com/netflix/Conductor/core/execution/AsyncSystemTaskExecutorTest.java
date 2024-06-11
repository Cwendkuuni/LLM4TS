package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.utils.QueueUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;

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
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Instant.of(0, 1000L * 60 * 60));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Instant.of(0, 1000L * 60));
    }

    @Test
    public void testExecute_taskNotFound_queueRemoved() {
        String taskId = "taskId1";

        // Given
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void testExecute_taskIsTerminal_queueRemoved() {
        String taskId = "taskId1";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void testExecute_taskIsConcurrentExecutionLimit_queuePostponed() {
        // Given
        TaskModel task = new TaskModel();
        task.setId("taskId1");
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);

        String queueName = QueueUtils.getQueueName(task);

        ArgumentCaptor<TaskModel> captor = ArgumentCaptor.forClass(TaskModel.class);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        // Then
        verify(queueDAO).postpone(
                eq(queueName), eq(task.getId()), anyLong(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_taskIsRateLimitPerFrequency_queuePostponed() {
        // Given
        TaskModel task = new TaskModel();
        task.setId("taskId1");
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflowId1");
        when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("workflowId1", false)).thenReturn(workflow);
        when(metadataDAO.getTaskDef("taskDefName1")).thenReturn(new TaskDefModel());

        String queueName = QueueUtils.getQueueName(task);

        ArgumentCaptor<TaskModel> captor = ArgumentCaptor.forClass(TaskModel.class);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        // Then
        verify(queueDAO).postpone(
                eq(queueName), eq(task.getId()), anyLong(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_taskIsInProgress_queuePostponed() {
        // Given
        TaskModel task = new TaskModel();
        task.setId("taskId1");
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflowId1");

        when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("workflowId1", false)).thenReturn(workflow);

        String queueName = QueueUtils.getQueueName(task);

        ArgumentCaptor<TaskModel> captor = ArgumentCaptor.forClass(TaskModel.class);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        // Then
        verify(queueDAO).postpone(
                eq(queueName), eq(task.getId()), anyLong(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_taskIsCompletedAsync_queueRemoved() {
        // Given
        TaskModel task = new TaskModel();
        task.setId("taskId1");
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflowId1");
        workflow.setStatus(WorkflowInstanceStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("workflowId1", false)).thenReturn(workflow);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        String queueName = QueueUtils.getQueueName(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        // Then
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyLong(), anyLong());
        verify(queueDAO).remove(eq(queueName), eq(task.getId()));
    }

    @Test
    public void testExecute_taskIsTerminal_workflowDecided() {
        // Given
        TaskModel task = new TaskModel();
        task.setId("taskId1");
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflowId1");
        workflow.setStatus(WorkflowInstanceStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(task.getId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("workflowId1", false)).thenReturn(workflow);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        // Then
        verify(workflowExecutor).decide("workflowId1");
    }
}