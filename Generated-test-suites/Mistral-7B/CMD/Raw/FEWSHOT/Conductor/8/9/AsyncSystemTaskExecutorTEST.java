package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModelImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncSystemTaskExecutorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);
    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = new QueueDAOImpl();
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        executor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecuteNullTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-id";
        executor.execute(systemTask, taskId);
        assertEquals("TaskId: task-id could not be found while executing {}", LOGGER.getClass(), "task-id");
    }

    @Test
    public void testExecuteNullSystemTask() {
        String taskId = "task-id";
        executor.execute(null, taskId);
        assertEquals("SystemTask could not be found while executing {}", LOGGER.getClass(), "task-id");
    }

    @Test
    public void testExecuteCompletedTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        executor.execute(systemTask, "task-id");
        verifyLog("Task {}/{} was already completed.", LOGGER.getClass(), "task-id", systemTask);
        verifyQueueRemove(queueDAO, "task-id");
    }

    @Test
    public void testExecuteTerminalTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        executor.execute(systemTask, "task-id");
        verifyLog("Workflow {} has been completed for {}/{}", LOGGER.getClass(), "workflow-id", systemTask);
        verifyQueueRemove(queueDAO, "task-id");
    }

    @Test
    public void testExecuteQueueSizeLimit() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        executor.execute(systemTask, "task-id");
        verifyLog(
                "Concurrent Execution limited for {}:{}, limit:{}",
                LOGGER.getClass(),
                "task-id",
                "task-def-name");
        verifyQueuePostpone(queueDAO, "task-id");
    }

    @Test
    public void testExecuteRateLimitPerFrequency() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        executor.execute(systemTask, "task-id");
        verifyLog(
                "RateLimit Execution limited for {}:{}, limit:{}",
                LOGGER.getClass(),
                "task-id",
                "task-def-name",
                "rate-limit-per-frequency");
        verifyQueuePostpone(queueDAO, "task-id");
    }

    @Test
    public void testExecuteScheduledTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        executor.execute(systemTask, "task-id");
        verifyTaskIncrementPollCount(task);
        verifyLog("Executing {}/{} in {} state", LOGGER.getClass(), "task-type", "task-id", "SCHEDULED");
    }

    @Test
    public void testExecuteInProgressTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        executor.execute(systemTask, "task-id");
        verifyTaskExecute(systemTask, task);
        verifyQueueRemove(queueDAO, "task-id");
    }

    @Test
    public void testExecuteAsyncCompleteTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setAsyncComplete(true);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        executor.execute(systemTask, "task-id");
        verifyQueueRemove(queueDAO, "task-id");
    }

    @Test
    public void testExecuteAsyncCompleteInProgressTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setAsyncComplete(true);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        executor.execute(systemTask, "task-id");
        verifyQueueRemove(queueDAO, "task-id");
    }

    @Test
    public void testExecuteAsyncCompleteTerminalTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.TERMINAL);
        task.setAsyncComplete(true);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        executor.execute(systemTask, "task-id");
        verifyLog("Workflow {} has been completed for {}/{}", LOGGER.getClass(), "workflow-id", systemTask);
        verifyQueueRemove(queueDAO, "task-id");
    }

    @Test
    public void testLoadTaskQuietlyNullTaskId() {
        executor.loadTaskQuietly(null);
        assertNull(executionDAOFacade.getTaskModel(anyString()));
    }

    @Test
    public void testLoadTaskQuietlyValidTaskId() {
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        TaskModel loadedTask = executor.loadTaskQuietly("task-id");
        assertSame(task, loadedTask);
    }

    @Test
    public void testPostponeQuietlyNullQueueName() {
        TaskModel task = new TaskModel();
        executor.postponeQuietly(null, task);
        assertNull(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()));
    }

    @Test
    public void testPostponeQuietlyValidQueueName() {
        String queueName = "queue-name";
        TaskModel task = new TaskModel();
        when(queueDAO.postpone(queueName, anyString(), anyInt(), anyLong()))
                .thenThrow(new RuntimeException());

        executor.postponeQuietly(queueName, task);
        verifyLog("Error postponing task: {} in queue: {}", LOGGER.getClass(), "task-id", queueName);
    }

    private void verifyLog(String message, Class<?> loggerClass, Object... arguments) {
        verify(logger).info(message, arguments);
    }

    private void verifyQueueRemove(QueueDAO queueDAO, String taskId) {
        when(queueDAO.remove(anyString(), taskId)).thenReturn(true);
        executor.execute(null, taskId);
        verify(queueDAO).remove(eq(queueName), eq(taskId));
    }

    private void verifyQueuePostpone(QueueDAO queueDAO, String taskId) {
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(new QueueMessage());
        executor.execute(null, taskId);
        verify(queueDAO).postpone(eq(queueName), eq(taskId), eq(priority), eq(delay));
    }

    private void verifyTaskIncrementPollCount(TaskModel task) {
        task.incrementPollCount();
        verify(task).incrementPollCount();
    }

    private void verifyTaskExecute(WorkflowSystemTask systemTask, TaskModel task) {
        WorkflowModel workflow = new WorkflowModelImpl();
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        systemTask.start(workflow, task, workflowExecutor);
        verify(systemTask).start(eq(workflow), eq(task), eq(workflowExecutor));
    }
}

class TaskDefinition {
    int rateLimitPerFrequency = 5;
}