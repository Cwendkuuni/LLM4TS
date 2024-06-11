package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModelImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
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
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflowModel;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowModel.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(mock(TaskDef.class));
        when(queueDAO.getQueueName(task)).thenReturn("queueName");
        when(queueDAO.postpone(
                        "queueName",
                        task.getTaskId(),
                        1,
                        5000))
                .thenReturn(true);
        when(queueDAO.remove("queueName", task.getTaskId()))
                .thenReturn(true);
        QueueUtils queueUtils = mock(QueueUtils.class);
        when(queueDAO.getQueueName(task)).thenReturn(queueUtils.getQueueName(task));
    }

    @Test
    public void execute_taskIsAlreadyCompleted_queueIsRemoved() {
        // Given
        WorkflowInstanceStatus workflowInstanceStatus = WorkflowInstanceStatus.COMPLETED;
        workflowModel.setStatus(workflowInstanceStatus);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId()));
        assertTrue(queueDAO.remove(queueNameCaptor.capture(queueName), task.getTaskId()));
    }

    @Test
    public void execute_taskIsNotTerminalAndIsNotScheduled_queueIsPostponed() {
        // Given
        TaskModel.Status status = TaskModel.Status.SCHEDULED;
        task.setStatus(status);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        assertSame(task, asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId()));
        assertTrue(queueDAO.postpone(
                        queueNameCaptor.capture(queueName),
                        task.getTaskId(),
                        1,
                        5000));
    }

    @Test
    public void execute_taskIsScheduledAndIsNotAsyncComplete_queueIsRemoved() {
        // Given
        TaskModel.Status status = TaskModel.Status.SCHEDULED;
        task.setStatus(status);
        boolean isTaskAsyncComplete = false;

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        assertSame(task, asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId()));
        assertTrue(queueDAO.remove(queueNameCaptor.capture(queueName), task.getTaskId()));
    }

    @Test
    public void execute_taskIsTerminalAndIsNotScheduled_queueIsRemoved() {
        // Given
        ExecutionStatus executionStatus = ExecutionStatus.FAILED;
        workflowModel.setStatus(WorkflowInstanceStatus.FAILED);
        ExecutionStatus workflowExecutionStatus = new ExecutionStatus(executionStatus, null);
        workflowModel.setStatus(workflowExecutionStatus);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId()));
        assertTrue(queueDAO.remove(queueNameCaptor.capture(queueName), task.getTaskId()));
    }

    @Test(expected = RuntimeException.class)
    public void execute_exceptionIsThrown_queueIsNotRemoved() {
        // Given
        Monitors monitors = mock(Monitors.class);
        when(executionDAOFacade.updateTask(task)).thenThrow(new RuntimeException());

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        // Then
        // Assertion order is important for this test
        assertFalse(queueDAO.remove(task.getTaskId(), "queueName"));
        Monitors.error(
                AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
        LogBackLoggerAppender logBackLoggerAppender = new LogBackLoggerAppender();
        logBackLoggerAppender.assertHasLogMessage(
                "Error executing system task - {}", "Error executing system task - RuntimeException", taskId);
    }
}

class LogBackLoggerAppender {
    private final List<String> logMessages = new ArrayList<>();

    public void assertHasLogMessage(String pattern, String message, String arg) {
        List<ILoggingEvent> events = LogManager.getRootLogger().listAppenders().getAppender("StdErrAppender")
                .listEvents();
        assertEquals(1, events.size());
        ILoggingEvent event = events.get(0);
        assertNotNull(event.getMessage().getFormattedMessage());
        String formattedMessage = event.getMessage().getFormattedMessage();
        String logMessage = PatternLayout.patternToLoggingEvent(formattedMessage, pattern);
        assertEquals(message, logMessage);
        logMessages.add(formattedMessage);
    }
}

class TaskDef {}