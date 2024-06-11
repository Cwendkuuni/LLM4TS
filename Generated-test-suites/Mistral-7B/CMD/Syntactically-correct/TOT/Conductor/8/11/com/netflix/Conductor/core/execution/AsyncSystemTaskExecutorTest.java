package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.metadata.TaskDefVersion;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModelImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        workflow = new WorkflowModelImpl();
        task = new TaskModel();
        task.setTaskDefName("testTask");
        task.setWorkflowInstanceId("testWorkflow");
        systemTask = new WorkflowSystemTaskImpl();
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefVersion("testTaskDefVersion"));
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_queueIsRemoved() throws Exception {
        // Given
        task.setStatus(TaskModel.Status.COMPLETED);
        String taskId = "testTaskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskIsInProgress_queueIsPostponed() throws Exception {
        // Given
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        String taskId = "testTaskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).postpone(queueName, taskId, 0, systemTaskCallbackTime);
    }

    @Test
    public void testExecute_taskIsScheduled_queueIsAdded() throws Exception {
        // Given
        task.setStatus(TaskModel.Status.SCHEDULED);
        String taskId = "testTaskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).add(queueName, taskId, workflow.getPriority());
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_taskIsNotFound() throws Exception {
        // Given
        String taskId = "testTaskId";
        String queueName = QueueUtils.getQueueName(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        assertNull(task);
        verify(queueDAO, never()).add(queueName, anyString(), anyInt());
        verify(queueDAO, never()).postpone(queueName, anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).remove(queueName, anyString());
    }

    @Test
    public void testExecute_workflowIsTerminal() throws Exception {
        // Given
        workflow.setStatus(WorkflowInstanceStatus.TERMINATED);
        task.setStatus(TaskModel.Status.SCHEDULED);
        String taskId = "testTaskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true)).thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_exceedsConcurrentLimit() throws Exception {
        // Given
        TaskModel existingTask = new TaskModel();
        existingTask.setTaskDefName(task.getTaskDefName());
        existingTask.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setStatus(TaskModel.Status.SCHEDULED);
        String taskId = "testTaskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).add(queueName, anyString(), anyInt());
        verify(queueDAO, never()).postpone(queueName, anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).remove(queueName, anyString());
        verify(executionDAOFacade, never()).updateTask(task);
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() throws Exception {
        // Given
        TaskModel existingTask = new TaskModel();
        existingTask.setTaskDefName(task.getTaskDefName());
        existingTask.setStatus(TaskModel.Status.SCHEDULED);
        task.setStatus(TaskModel.Status.SCHEDULED);
        String taskId = "testTaskId";
        String queueName = QueueUtils.getQueueName(task);
        long rateLimitPerFrequency = 10L;
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, Mockito.any())).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).add(queueName, anyString(), anyInt());
        verify(queueDAO, never()).postpone(queueName, anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).remove(queueName, anyString());
        verify(executionDAOFacade, never()).updateTask(task);
    }

    @Test
    public void testPostponeQuietly_queueIsRemoved() throws Exception {
        // Given
        String queueName = QueueUtils.getQueueName(task);

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Then
        verify(queueDAO, times(1)).remove(queueName, task.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly_taskIsLoaded() throws Exception {
        // Given
        String taskId = "testTaskId";

        // When
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertSame(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound() throws Exception {
        // Given
        String taskId = "testTaskId";

        // When
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertNull(loadedTask);
    }
}