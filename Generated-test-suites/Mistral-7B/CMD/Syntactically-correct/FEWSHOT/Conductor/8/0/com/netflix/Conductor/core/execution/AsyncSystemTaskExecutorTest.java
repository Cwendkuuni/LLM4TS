package com.netflix.conductor.core.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import java.time.Duration;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncSystemTaskExecutorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);
    private ExecutionDAOFacade executionDAOFacade;
    private MetadataDAO metadataDAO;
    private QueueDAO queueDAO;
    private AsyncSystemTaskExecutor systemTaskExecutor;

    @Before
    public void setup() {
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        metadataDAO = mock(MetadataDAO.class);
        queueDAO = new QueueDAOImpl();
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        when(executionDAOFacade.getTaskModel(UUID.randomUUID().toString())).thenReturn(null);
        when(executionDAOFacade.getWorkflowModel(UUID.randomUUID().toString(), true)).thenReturn(
                new WorkflowModel());
        when(executionDAOFacade.getWorkflowModel(UUID.randomUUID().toString(), false)).thenReturn(
                new WorkflowModel());
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(null);
        when(executionDAOFacade.getExecutionDAO()).thenReturn(mock(ExecutionDAO.class));
        systemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @After
    public void teardown() {
        queueDAO.clear();
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        systemTaskExecutor.execute(systemTask, taskId);
        TaskModel task = executionDAOFacade.getTaskModel(taskId);
        assertThat(task, nullValue());
        LOGGER.assertDebugMatches("Task: <null> fetched from execution DAO for taskId: {}", taskId);
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        String queueName = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowModel.Status.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        systemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        assertThat(queueDAO.get(queueName, taskId), nullValue());
        LOGGER.assertInfo("Workflow {} has been completed for {}/{}", workflow.toShortString(),
                systemTask, taskId);
    }

    @Test
    public void testExecute_TaskScheduled() {
        String taskId = UUID.randomUUID().toString();
        String queueName = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(UUID.randomUUID().toString());
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), true)).thenReturn(workflow);
        systemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        assertThat(queueDAO.get(queueName, taskId), nullValue());
        LOGGER.assertDebug("Executing {}/{} in {} state", task.getTaskType(), task.getTaskId(),
                task.getStatus());
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = UUID.randomUUID().toString();
        String queueName = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(UUID.randomUUID().toString());
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), true)).thenReturn(workflow);
        systemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        WorkflowSystemTask executedSystemTask = queueDAO.get(queueName, taskId);
        assertThat(executedSystemTask, is(notNullValue()));
        LOGGER.assertDebug("Executing {}/{} in {} state", task.getTaskType(), task.getTaskId(),
                task.getStatus());
    }

    @Test
    public void testExecute_TaskAsyncComplete() {
        String taskId = UUID.randomUUID().toString();
        String queueName = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(UUID.randomUUID().toString());
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), true)).thenReturn(workflow);
        when(workflowExecutor.decide(workflow.getId())).thenReturn(null);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        systemTaskExecutor.execute(systemTask, taskId);
        assertThat(queueDAO.get(queueName, taskId), nullValue());
        LOGGER.assertDebug("{} removed from queue: {}", task, queueName);
    }

    @Test
    public void testExecute_TaskError() {
        String taskId = UUID.randomUUID().toString();
        String queueName = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(UUID.randomUUID().toString());
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), true)).thenReturn(workflow);
        when(workflowExecutor.decide(workflow.getId())).thenThrow(new RuntimeException());
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        systemTaskExecutor.execute(systemTask, taskId);
        TaskModel errorTask = executionDAOFacade.getTaskModel(taskId);
        assertThat(errorTask, notNullValue());
        LOGGER.assertError("Error executing system task - {}, with id: {}", systemTask, taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = UUID.randomUUID().toString();
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        systemTaskExecutor.postponeQuietly(queueName, task);
        assertThat(queueDAO.get(queueName, taskId), nullValue());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel loadedTask = systemTaskExecutor.loadTaskQuietly(taskId);
        assertThat(loadedTask, is(sameInstance(task)));
    }
}