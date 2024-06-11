package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.WorkflowInstanceDAO;
import com.netflix.conductor.dao.WorkflowModelDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.WorkflowExecutionUtils;

@Component
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
    private WorkflowInstanceDAO workflowInstanceDAO;

    @Mock
    private WorkflowModelDAO workflowModelDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(new ExecutionProperties(1L, TimeUnit.SECONDS));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(new ExecutionProperties(1L, TimeUnit.SECONDS));
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());
        when(workflowModelDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(queueDAO.get(anyString(), anyString())).thenReturn(new QueueMessage());
        when(queueDAO.postpone(anyString(), anyString(), any(QueuePriority.class), anyLong())).thenReturn(new QueueMessage());
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.removeQuietly(anyString(), anyString())).thenReturn(true);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any(TaskDefinition.class))).thenReturn(false);
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(true);
        when(workflowInstanceDAO.getWorkflowPriority(anyString())).thenReturn(QueuePriority.NORMAL);
        when(workflowExecutor.decide(anyString())).thenReturn(true);
        when(monitors.error(anyString(), anyString())).thenReturn(true);
        when(monitors.recordQueueWaitTime(anyString(), anyLong())).thenReturn(true);
    }

    @Test
    public void testExecute_whenTaskIsNotFound_shouldNotUpdateTaskAndRemoveItFromQueue() {
        String taskId = UUID.randomUUID().toString();
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).removeQuietly(anyString(), taskId);
        verify(executionDAOFacade, never()).getTaskModel(taskId);
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_whenTaskIsInTerminalState_shouldRemoveItFromQueue() {
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_whenTaskIsScheduledAndHasNotExceededRateLimitOrConcurrentExecutionLimit_shouldExecuteTaskAndUpdateItInQueue() {
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskType("TestTaskType");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(anyString(), taskId)).thenReturn(new QueueMessage());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).remove(anyString(), taskId);
        verify(queueDAO, never()).postpone(anyString(), taskId, any(QueuePriority.class), anyLong());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_whenTaskIsScheduledAndHasExceededRateLimit_shouldPostponeTaskAndUpdateItInQueue() {
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskType("TestTaskType");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(anyString(), taskId)).thenReturn(new QueueMessage());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).postpone(anyString(), taskId, any(QueuePriority.class), anyLong());
        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_whenTaskIsScheduledAndHasExceededConcurrentExecutionLimit_shouldPostponeTaskAndUpdateItInQueue() {
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskType("TestTaskType");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(anyString(), taskId)).thenReturn(new QueueMessage());
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).postpone(anyString(), taskId, any(QueuePriority.class), anyLong());
        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_whenTaskIsInProgress_shouldExecuteTaskAndUpdateItInQueue() {
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        task.setTaskType("TestTaskType");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(anyString(), taskId)).thenReturn(new QueueMessage());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).remove(anyString(), taskId);
        verify(queueDAO, never()).postpone(anyString(), taskId, any(QueuePriority.class), anyLong());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_whenTaskExecutionFails_shouldRecordErrorAndUpdateTaskInQueue() {
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        task.setTaskType("TestTaskType");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(anyString(), taskId)).thenReturn(new QueueMessage());

        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getWorkflowModel(anyString(), true)).thenReturn(workflow);

        try {
            asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
            fail("expected an exception");
        } catch (Exception e) {
            verify(monitors).error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
            verify(queueDAO).postpone(anyString(), taskId, any(QueuePriority.class), anyLong());
            verify(executionDAOFacade).updateTask(task);
        }
    }

    @Test
    public void testExecute_whenTaskHasCompleted_shouldRemoveItFromQueueAndDecideWorkflow() {
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);
        task.setTaskType("TestTaskType");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(anyString(), taskId)).thenReturn(new QueueMessage());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_whenTaskHasBeenPostponed_shouldUpdateTaskInQueue() {
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskType("TestTaskType");
        task.setCallbackAfterSeconds(1L);
        task.setTaskId("TestTaskId");
        task.setWorkflowInstanceId("TestWorkflowInstanceId");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(anyString(), taskId)).thenReturn(new QueueMessage());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        QueueMessage queueMessage = new QueueMessage();
        queueMessage.setTaskId("TestTaskId");
        queueMessage.setWorkflowPriority(QueuePriority.NORMAL);
        queueMessage.setCallbackAfterSeconds(1L);

        verify(queueDAO).postpone(anyString(), taskId, QueuePriority.NORMAL, 1L);
    }
}

class ExecutionProperties {
    private final long duration;
    private final TimeUnit timeUnit;

    ExecutionProperties(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public long getSeconds() {
        return duration * timeUnit.toSeconds();
    }
}

class TaskDefinition {
    // Empty class for mocking
}

class QueueMessage {
    private String taskId;
    private QueuePriority workflowPriority;
    private long callbackAfterSeconds;

    public String getTaskId() {
        return taskId;
    }

    public QueuePriority getWorkflowPriority() {
        return workflowPriority;
    }

    public long getCallbackAfterSeconds() {
        return callbackAfterSeconds;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setWorkflowPriority(QueuePriority workflowPriority) {
        this.workflowPriority = workflowPriority;
    }

    public void setCallbackAfterSeconds(long callbackAfterSeconds) {
        this.callbackAfterSeconds = callbackAfterSeconds;
    }
}

class WorkflowModel {
    private String id;
    private String name;
    private ExecutionStatus status;

    public WorkflowModel() {
        this.id = "TestWorkflowInstanceId";
        this.name = "TestWorkflowName";
        this.status = ExecutionStatus.TERMINAL;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public String toShortString() {
        return "Workflow " + id + " (" + name + ")";
    }
}