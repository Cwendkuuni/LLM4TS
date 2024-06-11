package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowInstanceModel;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private Logger logger;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        logger = mock(Logger.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Optional.of(5000L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Optional.of(10000L));
    }

    @Test
    public void testExecute_NullTaskId() {
        String taskId = null;
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(logger).error(taskIdCaptor.capture(), eq("TaskId: {} could not be found while executing"),
                taskId);
    }

    @Test
    public void testExecute_NullTask() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = null;

        TaskModel task = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(logger, never()).debug("Task: {} fetched from execution DAO for taskId: {}", eq(task),
                taskId);
        verify(logger, never()).info("Workflow {} has been completed for {}/{}", anyString(),
                systemTask, taskId);
        verify(logger, never()).warn(
                "Concurrent Execution limited for {}:{}, limit:{}", eq(taskId), eq(task.getTaskDefName()),
                eq(task.getRateLimitPerFrequency()));
        verify(logger, never()).warn(
                "RateLimit Execution limited for {}:{}, limit:{}", eq(taskId),
                eq(task.getTaskDefName()), eq(task.getRateLimitPerFrequency()));
        verify(logger, never()).debug("Executing {}/{} in {} state", eq(systemTask), eq(taskId),
                eq(task.getStatus()));
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(logger).info(
                "Workflow {} has been completed for {}/{}", eq(workflow.toShortString()), systemTask,
                taskId);
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }

    @Test
    public void testExecute_TaskNotInQueue() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).remove(eq(queueNameCaptor.capture()), eq(taskIdCaptor.capture()));
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).postpone(
                eq(queueNameCaptor.capture()), eq(taskIdCaptor.capture()), anyInt(), eq(5000L));
    }

    @Test
    public void testExecute_TaskTerminal() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowInstanceModel workflowInstance = mock(WorkflowInstanceModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINATED);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINATED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).remove(eq(queueNameCaptor.capture()), eq(taskIdCaptor.capture()));
        verify(workflowExecutor).decide(eq(workflowIdCaptor.capture()));
    }

    @Test(expected = Exception.class)
    public void testExecute_ExceptionThrown() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        throw new Exception("Test Exception");
    }

    @Test
    public void testPostponeQuietly_NullQueueName() {
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(null, task);
    }

    @Test
    public void testPostponeQuietly_NullTask() {
        String queueName = "queueName";
        TaskModel task = null;

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
    }

    @Test
    public void testPostponeQuietly_TaskNotFound() {
        String queueName = "queueName";
        TaskModel task = mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, never()).postpone(eq(queueName), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly_TaskAlreadyPostponed() {
        String queueName = "queueName";
        TaskModel task = mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO, times(1)).postpone(eq(queueNameCaptor.capture()), eq(taskIdCaptor.capture()),
                anyInt(), eq(10000L));
    }

    @Test
    public void testLoadTaskQuietly_NullTaskId() {
        String taskId = null;

        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        assertNotNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "taskId";

        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
}