package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.Message;
import com.netflix.conductor.dao.QueueDAO.QueueName;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.WorkflowExecutor;

@SuppressWarnings("unchecked")
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

    @Captor
    private ArgumentCaptor<Message> messageArgumentCaptor;

    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;
    private QueueName queueName;
    private String taskId;
    private String workflowId;

    @Before
    public void setup() {
        task = mock(TaskModel.class);
        workflow = mock(WorkflowModel.class);
        systemTask = mock(WorkflowSystemTask.class);
        queueName = mock(QueueName.class);
        taskId = "mock_task_id";
        workflowId = "mock_workflow_id";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflowId, Boolean.FALSE)).thenReturn(workflow);
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);
        when(queueDAO.postpone(queueName, taskId, 0, Duration.ofSeconds(1L))).thenReturn(true);
        when(queueDAO.postpone(queueName, taskId, 0, Duration.ofSeconds(2L))).thenReturn(true);
        when(queueDAO.getMessages(queueName, 1)).thenReturn(new Object[] { messageArgumentCaptor.capture() });
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(2L));
        when(workflowExecutor.decide(workflowId)).thenReturn(Boolean.TRUE);
    }

    @Test
    public void testExecuteWithNullTaskId() {
        asyncSystemTaskExecutor.execute(systemTask, null);
        assertEquals(0, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertNull(asyncSystemTaskExecutor.executionDAOFacade);
        assertNull(asyncSystemTaskExecutor.queueDAO);
        assertNull(asyncSystemTaskExecutor.metadataDAO);
        assertNull(asyncSystemTaskExecutor.workflowExecutor);
    }

    @Test
    public void testExecuteWithNullSystemTask() {
        asyncSystemTaskExecutor.execute(null, taskId);
        assertEquals(0, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertEquals(taskId, asyncSystemTaskExecutor.executionDAOFacade.getTaskModel(taskId).get().getId());
        assertNull(asyncSystemTaskExecutor.queueDAO);
        assertNull(asyncSystemTaskExecutor.metadataDAO);
        assertNull(asyncSystemTaskExecutor.workflowExecutor);
    }

    @Test
    public void testExecuteWithNullTaskModel() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.empty());
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(1, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.executionDAOFacade);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.queueDAO);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.metadataDAO);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.workflowExecutor);
    }

    @Test
    public void testExecuteWithNullWorkflowModel() {
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflowId, Boolean.FALSE)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(1, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.executionDAOFacade);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.queueDAO);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.metadataDAO);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.workflowExecutor);
    }

    @Test
    public void testExecuteWithTaskAlreadyTerminal() {
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(0, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertFalse(queueDAO.remove(queueName, taskId));
        assertEquals(0, Monitors.getRecordedQueueWaitTime(task.getTaskType()));
        assertNull(workflowExecutor.decide(workflowId));
    }

    @Test
    public void testExecuteWithTaskInScheduledState() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(1, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.executionDAOFacade);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.queueDAO);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.metadataDAO);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.workflowExecutor);
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        Message message = messageArgumentCaptor.getValue();
        assertEquals(taskId, message.getTaskId());
        assertEquals(QueueName.EXECUTION_QUEUE, message.getQueueName());
        assertEquals(task.getWorkflowPriority(), message.getPriority());
        assertEquals(1L, message.getTimestamp());
        assertEquals(2L, message.getPostponeSeconds());
        assertNull(workflowExecutor.decide(workflowId));
    }

    @Test
    public void testExecuteWithTaskInProgressState() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(0, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.executionDAOFacade);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.queueDAO);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.metadataDAO);
        assertSame(asyncSystemTaskExecutor, asyncSystemTaskExecutor.workflowExecutor);
        assertFalse(queueDAO.remove(queueName, taskId));
        assertEquals(workflow, executionDAOFacade.getWorkflowModel(workflowId, Boolean.FALSE));
        when(systemTask.execute(workflow, task, workflowExecutor)).thenCallRealMethod();
        systemTask.execute(workflow, task, workflowExecutor);
        assertFalse(queueDAO.remove(queueName, taskId));
        assertTrue(workflowExecutor.decide(workflowId));
    }

    @Test
    public void testExecuteWithTaskAsyncCompleteAndNotInScheduledState() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        boolean isTaskAsyncComplete = true;
        when(systemTask.isAsyncComplete(task)).thenReturn(isTaskAsyncComplete);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(0, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertFalse(queueDAO.remove(queueName, taskId));
        assertFalse(queueDAO.getMessages(queueName, 1)[0] instanceof Message);
        assertTrue(workflowExecutor.decide(workflowId));
    }

    @Test
    public void testExecuteWithTaskAsyncCompleteAndInScheduledState() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        boolean isTaskAsyncComplete = true;
        when(systemTask.isAsyncComplete(task)).thenReturn(isTaskAsyncComplete);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(1, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        Message message = messageArgumentCaptor.getValue();
        assertEquals(taskId, message.getTaskId());
        assertEquals(QueueName.EXECUTION_QUEUE, message.getQueueName());
        assertEquals(task.getWorkflowPriority(), message.getPriority());
        assertEquals(1L, message.getTimestamp());
        assertEquals(1L, message.getPostponeSeconds());
        assertNull(workflowExecutor.decide(workflowId));
    }

    @Test
    public void testExecuteWithTaskTerminalState() {
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(0, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertFalse(queueDAO.remove(queueName, taskId));
        assertEquals(0, Monitors.getRecordedQueueWaitTime(task.getTaskType()));
        assertNull(workflowExecutor.decide(workflowId));
    }

    @Test
    public void testExecuteWithTaskExceedingConcurrentExecutionLimit() {
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(1, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertFalse(queueDAO.remove(queueName, taskId));
        assertFalse(queueDAO.postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs));
        assertNull(workflowExecutor.decide(workflowId));
    }

    @Test
    public void testExecuteWithTaskExceedingRateLimitPerFrequency() {
        when(task.getRateLimitPerFrequency())
                .thenReturn(Duration.ofSeconds(1L));
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(1, Monitors.getErrorCount(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask"));
        assertFalse(queueDAO.remove(queueName, taskId));
        assertFalse(queueDAO.postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs));
        assertNull(workflowExecutor.decide(workflowId));
    }
}