package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.WorkflowInstance;
import com.netflix.conductor.utils.Json;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private Logger logger;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private Queue queue;

    @Before
    public void setUp() {
        when(executionDAOFacade.getTaskModel(Mockito.anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(workflow);
        when(queueDAO.getQueue(Mockito.anyString())).thenReturn(queue);
        when(metadataDAO.getTaskDef(Mockito.anyString())).thenReturn("TaskDef");
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(mock(Long.class));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(mock(Long.class));
        when(workflowExecutor.decide(Mockito.anyString())).thenReturn(Void.TYPE);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.isInfoEnabled()).thenReturn(true);
        when(logger.isErrorEnabled()).thenReturn(true);
    }

    @After
    public void tearDown() {
        // Clear mocks for next test
        asyncSystemTaskExecutor = null;
    }

    @Test
    public void testExecute_taskIsNull() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verifyNoInteractions(executionDAOFacade, queueDAO, metadataDAO, logger, workflowExecutor);
    }

    @Test
    public void testExecute_taskIsTerminal() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        when(task.getStatus().isTerminal()).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
        verifyNoInteractions(executionDAOFacade, logger, workflowExecutor);
    }

    @Test
    public void testExecute_taskIsScheduled() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, "TaskDef")).thenReturn(false);
        when(queue.getSize()).thenReturn(1);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task),
                taskId,
                (int) (Math.random() * 10),
                systemTaskCallbackTime.longValue());
        verify(logger).debug(
                "{} removed from queue: {}", task, QueueUtils.getQueueName(task));
        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test
    public void testExecute_taskIsInProgress() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(queue.getSize()).thenReturn(1);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).execute(workflow, task, workflowExecutor);
        verify(logger).debug("Finished execution of {}/{}-{}", systemTask, taskId, task.getStatus());
        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test
    public void testExecute_taskHasCompleted() throws Exception {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
        verify(logger).info(
                "Workflow {} has been completed for {}/{}", workflow, systemTask, taskId);
        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test
    public void testExecute_taskThrowsException() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        try {
            asyncSystemTaskExecutor.execute(systemTask, taskId);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected
        }

        verify(logger).error("Error executing system task - {}, with id: {}", systemTask, taskId);
        verifyNoInteractions(executionDAOFacade, queueDAO, metadataDAO, logger, workflowExecutor);
    }

    @Test
    public void testPostponeQuietly_queueIsNull() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testPostponeQuietly_queueExists() {
        String queueName = "queueName";
        Queue queue = mock(Queue.class);
        when(queue.getSize()).thenReturn(0);
        when(queueDAO.getQueue(queueName)).thenReturn(queue);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        verify(queueDAO).remove(queueName, task.getId());
        verify(logger).debug("{} removed from queue: {}", task, queueName);
    }

    @Test
    public void testLoadTaskQuietly_taskExists() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task.getTaskId(), loadedTask.getTaskId());
        assertEquals(task.getTaskType(), loadedTask.getTaskType());
        verifyNoInteractions(executionDAOFacade);
    }

    @Test
    public void testLoadTaskQuietly_taskDoesNotExist() {
        String taskId = "taskId";

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
        verifyNoInteractions(executionDAOFacade);
    }
}