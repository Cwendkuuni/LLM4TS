package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.workflow.WorkflowExecution;
import com.netflix.conductor.model.workflow.WorkflowExecutionStatus;

@SuppressWarnings("deprecation")
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
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowInstance workflowInstance;

    @Mock
    private WorkflowExecution workflowExecution;

    @Mock
    private QueueUtils queueUtils;

    @Mock
    private Monitors monitors;

    private String queueName;
    private String taskId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        queueName = "testQueueName";
        taskId = "testTaskId";
    }

    @After
    public void tearDown() {
        // Reset mocks to avoid leaking state between tests
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteNullTask() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.empty());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task was not found, and therefore no further actions were taken
        verify(queueDAO, never()).remove(eq(queueName), eq(taskId));
        verify(queueDAO, never()).postpone(anyString(), any(), anyLong(), anyLong());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecuteTerminalTask() {
        taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(eq(queueName), eq(taskId));
        verify(workflowExecutor, times(1)).decide(workflowId);
        // verify no further actions were taken since the task is already terminal
        verify(queueDAO, never()).postpone(anyString(), any(), anyLong(), anyLong());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecuteScheduledTask() {
        taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));

        when(queueDAO.remove(queueName, taskId)).thenThrow(RuntimeException.class);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task was postponed in the queue
        verify(queueDAO, times(1)).postpone(
                eq(queueName),
                eq(taskId),
                anyLong(),
                eq(systemTaskCallbackTime));
        // verify no further actions were taken since the task is already scheduled
        verify(queueDAO, never()).remove(eq(queueName), eq(taskId));
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecuteInProgressTask() {
        taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(workflowExecutor.decide(eq(workflowId))).thenReturn(WorkflowExecutionStatus.CONTINUE);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task was executed, and the workflow was decided
        verify(queueDAO, times(1)).remove(eq(queueName), eq(taskId));
        verify(queueDAO, never()).postpone(anyString(), any(), anyLong(), anyLong());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecuteAsyncCompleteTask() {
        taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.IN_PROGRESS);
        taskModel.setAsyncComplete(true);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(workflowExecutor.decide(eq(workflowId))).thenReturn(WorkflowExecutionStatus.CONTINUE);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task was removed from the queue and the workflow was decided
        verify(queueDAO, times(1)).remove(eq(queueName), eq(taskId));
        verify(queueDAO, never()).postpone(anyString(), any(), anyLong(), anyLong());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecuteExceedsInProgressLimit() {
        taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task was postponed in the queue
        verify(queueDAO, times(1)).postpone(
                eq(queueName),
                eq(taskId),
                anyLong(),
                eq(queueTaskMessagePostponeSecs));
        // verify no further actions were taken since the task is already in progress and exceeds
        // the limit
        verify(queueDAO, never()).remove(eq(queueName), eq(taskId));
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecuteExceedsRateLimitPerFrequency() {
        taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.IN_PROGRESS);
        taskModel.setTaskDefName("testTaskDefName");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(new Object());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any(Object.class)))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the task was postponed in the queue
        verify(queueDAO, times(1)).postpone(
                eq(queueName),
                eq(taskId),
                anyLong(),
                eq(queueTaskMessagePostponeSecs));
        // verify no further actions were taken since the task exceeds the rate limit per frequency
        verify(queueDAO, never()).remove(eq(queueName), eq(taskId));
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteExceptionDuringExecution() {
        taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(taskModel));
        when(workflowModel.getStatus()).thenReturn(new Object());
        when(workflowSystemTask.start(any(), any(), any())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Verify that the exception was logged and the task was not removed from the queue
        verify(queueDAO, never()).remove(eq(queueName), eq(taskId));
        verify(queueDAO, never()).postpone(anyString(), any(), anyLong(), anyLong());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(monitors, times(1)).error(anyString(), anyString());
        verify(LOGGER, times(1)).error(anyString(), anyString(), any(Throwable.class));
    }
}