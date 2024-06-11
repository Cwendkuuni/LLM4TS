package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.getWorkflowPriority(anyString(), anyString())).thenReturn(0L);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefMock());
    }

    @Test
    public void execute_taskNull_doesNotExecuteOrPersist() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacade, never()).updateTask(task);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyLong(), anyLong());
    }

    @Test
    public void execute_taskCompleted_removesTask() {
        // Given
        givenTaskIsCompleted(task);
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(task.getTaskId()));
    }

    @Test
    public void execute_taskRateLimitExceeded_postponesTask() {
        // Given
        givenTaskIsScheduled(task);
        when(task.getRateLimitPerFrequency()).thenReturn(1L);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(task.getTaskId()),
                eq(0L),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void execute_taskInProgress_updatesCallbackAndPostponesTask() {
        // Given
        givenTaskIsScheduled(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(task).setCallbackAfterSeconds(systemTaskCallbackTime);
        verify(queueDAO).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(task.getTaskId()),
                eq(0L),
                eq(systemTaskCallbackTime));
    }

    @Test
    public void execute_taskInProgress_updatesTaskEndTimeAndRemovesTask()
            throws InterruptedException {
        // Given
        givenTaskIsScheduled(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        Thread.sleep(systemTaskCallbackTime + queueTaskMessagePostponeSecs + 1000);

        // Then
        verify(task).setEndTime(anyLong());
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(task.getTaskId()));
    }

    @Test
    public void execute_taskTerminal_removesTask() {
        // Given
        givenTaskIsTerminal(task);
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(task.getTaskId()));
    }

    @Test
    public void execute_taskTerminal_decidesWorkflow() {
        // Given
        givenTaskIsTerminal(task);
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(workflowExecutor).decide(eq(workflow.getId()));
    }

    @Test
    public void execute_taskExceedsInProgressLimit_postponesTask() {
        // Given
        givenTaskIsScheduled(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(task.getTaskId()),
                eq(0L),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void execute_taskConcurrentLimitExceeded_postponesTask() {
        // Given
        givenTaskIsScheduled(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(task.getTaskId()),
                eq(0L),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void execute_taskConcurrentLimitNotExceededAndRateLimitNotExceeded_executesTask() {
        // Given
        givenTaskIsScheduled(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(false);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(systemTask).execute(eq(workflow), eq(task), eq(workflowExecutor));
    }

    @Test
    public void execute_taskError_recordsErrorAndPersistsTask() {
        // Given
        String taskId = "taskId";

        // When
        doThrow(new RuntimeException("Test Error"))
                .when(systemTask)
                .execute(eq(workflow), eq(task), eq(workflowExecutor));
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(Monitors).error(eq(AsyncSystemTaskExecutor.class.getSimpleName()), eq("executeSystemTask"));
        verify(LogbackLogger.class).error(
                eq("Error executing system task - Test Error"), eq(taskId));
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void postponeQuietly_taskNull_doesNotPostponeOrLogError() {
        // Given
        String queueName = "queueName";
        TaskModel task = null;

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Then
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyLong(), anyLong());
        verify(LogbackLogger.class, never()).error(anyString());
    }

    @Test
    public void postponeQuietly_taskNotNull_postponesAndLogsErrorIfFailure() {
        // Given
        String queueName = "queueName";
        String taskId = "taskId";
        long priority = 0L;

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Then
        verify(queueDAO).postpone(
                eq(queueName),
                eq(taskId),
                eq(priority),
                anyLong());
    }

    @Test
    public void loadTaskQuietly_taskNull_returnsNull() {
        // Given
        String taskId = "taskId";

        // When
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertNull(result);
    }

    @Test
    public void loadTaskQuietly_taskNotNull_returnsTask() {
        // Given
        String taskId = "taskId";

        // When
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertEquals(task, result);
    }

    private void givenTaskIsScheduled(TaskModel task) {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
    }

    private void givenTaskIsTerminal(TaskModel task) {
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);
    }

    private static class TaskDefMock implements com.netflix.conductor.dao.TaskDef {

        @Override
        public String getTaskId() {
            return "taskId";
        }

        @Override
        public long getPriority() {
            return 0L;
        }

        @Override
        public long getRateLimitPerFrequency() {
            return 0L;
        }

        @Override
        public String getTaskDefName() {
            return "taskDefName";
        }

        @Override
        public String getDescription() {
            return "description";
        }

        @Override
        public Optional<TaskInputDef> getTaskInputDef() {
            return Optional.empty();
        }
    }
}