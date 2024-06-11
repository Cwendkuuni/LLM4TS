package com.netflix.conductor.core.execution.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOTestFactory;
import com.netflix.conductor.dao.test.ExecutionDAOFacadeTestFactory;
import com.netflix.conductor.dao.test.MetadataDAOTestFactory;
import com.netflix.conductor.model.WorkflowModelTestFactory;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = ExecutionDAOFacadeTestFactory.newInstance();
        queueDAO = QueueDAOTestFactory.getMockQueueDAO();
        metadataDAO = MetadataDAOTestFactory.getMockMetadataDAO();
        conductorProperties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        executor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_nullTaskId() {
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        executor.execute(systemTask, null);
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(""), "");
    }

    @Test
    public void testExecute_nullTask() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        executor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(task), taskId);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        executor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(""), taskId);
    }

    @Test
    public void testExecute_terminalTask() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        executor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(task), taskId);
    }

    @Test
    public void testExecute_inProgressLimitExceeded() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        executor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(
                QueueUtils.getQueueName(task),
                taskId,
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_rateLimitExceeded() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getTaskDefName()).thenReturn("task-def");
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(
                WorkflowModelTestFactory.newInstance().getTaskDef("task-def"));
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);
        executor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(
                QueueUtils.getQueueName(task),
                taskId,
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_asyncCompleteTask() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        executor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(task), taskId);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_workflowCompleted() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = WorkflowModelTestFactory.newInstance().getWorkflowModel();
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        executor.execute(systemTask, taskId);
        fail("Expected exception: Workflow is in COMPLETED state");
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = WorkflowModelTestFactory.newInstance().getWorkflowModel();
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);
        executor.execute(systemTask, taskId);
        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test
    public void testPostponeQuietly_nullQueueName() {
        TaskModel task = Mockito.mock(TaskModel.class);
        executor.postponeQuietly(null, task);
    }

    @Test
    public void testPostponeQuietly_nullTask() {
        String queueName = "queue-name";
        executor.postponeQuietly(queueName, null);
    }

    @Test
    public void testPostponeQuietly_success() {
        String queueName = "queue-name";
        TaskModel task = Mockito.mock(TaskModel.class);
        executor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test(expected = RuntimeException.class)
    public void testPostponeQuietly_queueNotFound() {
        String queueName = "queue-name";
        TaskModel task = Mockito.mock(TaskModel.class);
        executor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        fail("Expected exception: Queue not found: queueName=" + queueName);
    }
}