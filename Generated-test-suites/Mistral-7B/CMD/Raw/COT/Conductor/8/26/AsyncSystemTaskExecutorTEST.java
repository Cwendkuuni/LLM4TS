package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.TestUtils;

@SpringBootTest
@ActiveProfiles("test")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowModel workflowModel;
    private TaskModel taskModel;
    private String queueName;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        QueueUtils queueUtils = new QueueUtils();
        queueName = queueUtils.getQueueName(taskModel);
        when(queueDAO.get(queueName)).thenReturn(queueModel());
        when(executionDAOFacade.getTaskModel(taskModel.getTaskId())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowModel.getWorkflowId(), false)).thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(taskDef());
        Monitors monitors = mock(Monitors.class);
        when(conductorProperties.getMonitors()).thenReturn(monitors);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(TestUtils.duration(1000));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(TestUtils.duration(1000));
    }

    @Test
    public void testExecuteNullTask() {
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), null);
        assertNull(taskModel);
    }

    @Test
    public void testExecuteTaskAlreadyCompleted() {
        taskModel.setStatus(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskModel.getTaskId());
        verifyTaskNotPostponed();
        verifyTaskNotRemovedFromQueue();
    }

    @Test
    public void testExecuteTaskTerminalStatus() {
        taskModel.setStatus(ExecutionStatus.TERMINAL);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskModel.getTaskId());
        verifyTaskNotPostponed();
        verifyTaskRemovedFromQueue();
    }

    @Test
    public void testExecuteTaskRateLimitPerFrequencyExceeded() {
        taskModel.setRateLimitPerFrequency(1);
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, taskDef())).thenReturn(true);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskModel.getTaskId());
        verifyTaskPostponed();
        verifyTaskNotRemovedFromQueue();
    }

    @Test
    public void testExecuteTaskExecutionLimitExceeded() {
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskModel.getTaskId());
        verifyTaskPostponed();
        verifyTaskNotRemovedFromQueue();
    }

    @Test
    public void testExecuteTaskWorkflowTerminalStatus() {
        workflowModel.setStatus(ExecutionStatus.TERMINAL);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskModel.getTaskId());
        verifyTaskNotPostponed();
        verifyTaskRemovedFromQueue();
    }

    @Test(expected = Exception.class)
    public void testExecuteTaskException() {
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.TERMINAL);
        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskModel.getTaskId());
    }

    @Test
    public void testPostponeTask() {
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        verifyTaskPostponed();
    }

    @Test(expected = Exception.class)
    public void testPostponeTaskException() {
        when(queueDAO.get(queueName)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
    }

    private void verifyTaskNotPostponed() {
        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).remove(queueName, taskIdArgumentCaptor.capture());
        Optional<String> capturedTaskId = Optional.ofNullable(taskIdArgumentCaptor.getValue());
        assertNotNull(capturedTaskId.isPresent());
        assertEquals(taskModel.getTaskId(), capturedTaskId.get());
    }

    private void verifyTaskPostponed() {
        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> secondsArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).postpone(
                queueName,
                taskIdArgumentCaptor.capture(),
                eq(taskModel.getWorkflowPriority()),
                secondsArgumentCaptor.capture());
        Optional<String> capturedTaskId = Optional.ofNullable(taskIdArgumentCaptor.getValue());
        assertNotNull(capturedTaskId.isPresent());
        assertEquals(taskModel.getTaskId(), capturedTaskId.get());
        assertEquals(taskModel.getCallbackAfterSeconds(), secondsArgumentCaptor.getValue());
    }

    private void verifyTaskRemovedFromQueue() {
        verify(queueDAO).remove(queueName, taskModel.getTaskId());
    }

    private QueueModel queueModel() {
        return new QueueModel();
    }

    private TaskDefinition taskDef() {
        return new TaskDefinition();
    }
}