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
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowInstance;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.TestConductorProperties;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowInstance workflowInstance;

    @Mock
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConductorProperties conductorProperties = new TestConductorProperties();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.toShortString()).thenReturn("Workflow Test");
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefMock());
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.getFromQueue(anyString(), anyString(), anyInt())).thenReturn(null);
        when(queueDAO.getQueuedMessages(anyString())).thenReturn(new QueuedMessageMock());
        when(workflowExecutor.decide(anyString())).thenReturn(null);
        when(systemTask.isAsyncComplete(any())).thenReturn(false);
        when(systemTask.start(any(), any(), any()))
                .thenThrow(new RuntimeException("System Task execution failed"));
        when(systemTask.execute(any(), any(), any()))
                .thenThrow(new RuntimeException("System Task execution failed"));
        when(task.getRateLimitPerFrequency()).thenReturn(0L);
        when(task.getWorkflowInstanceId()).thenReturn("workflow-instance-id");
    }

    @Test
    public void testExecute_taskIsNotPresent() {
        asyncSystemTaskExecutor.execute(systemTask, "task-id");
        verify(executionDAOFacade, never()).getTaskModel("task-id");
    }

    @Test
    public void testExecute_taskIsTerminal() {
        task.setStatus(ExecutionStatus.TERMINALED);
        asyncSystemTaskExecutor.execute(systemTask, "task-id");
        verify(queueDAO, times(1)).remove("queue-name", "task-id");
    }

    @Test
    public void testExecute_taskIsScheduled() {
        task.setStatus(ExecutionStatus.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, "task-id");
        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(executionDAOFacade, times(1)).getWorkflowModel("workflow-instance-id", false);
        verify(queueDAO, never()).postpone("queue-name", "task-id", anyInt(), anyInt());
        verify(queueDAO, times(1)).remove("queue-name", "task-id");
    }

    @Test
    public void testExecute_concurrentExecutionLimitExceeded() {
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "task-id");
        verify(queueDAO, times(1)).postpone("queue-name", "task-id", anyInt(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_rateLimitExceeded() {
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, workflowModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "task-id");
        verify(queueDAO, times(1)).postpone("queue-name", "task-id", anyInt(), queueTaskMessagePostponeSecs);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_systemTaskExecutionFailed() {
        asyncSystemTaskExecutor.execute(systemTask, "task-id");
        verify(systemTask, times(1)).start(workflowInstance, task, workflowExecutor);
        verify(systemTask, times(1)).execute(workflowInstance, task, workflowExecutor);
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("queue-name", task);
        verify(queueDAO, times(1)).postpone("queue-name", "task-id", anyInt(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskPresent() {
        asyncSystemTaskExecutor.loadTaskQuietly("task-id");
        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
    }

    @Test
    public void testLoadTaskQuietly_taskNotPresent() {
        asyncSystemTaskExecutor.loadTaskQuietly("task-id");
        verify(executionDAOFacade, never()).getTaskModel("task-id");
    }
}

class TaskDefMock implements TaskDef {

    @Override
    public String getName() {
        return "TaskDefMock";
    }

    @Override
    public String getDescription() {
        return "TaskDefMock description";
    }

    @Override
    public String getType() {
        return "TaskDefMockType";
    }
}

class QueuedMessageMock {

    private final String taskId;
    private final String queueName;
    private final int priority;
    private final long callbackTime;

    public QueuedMessageMock(String taskId, String queueName, int priority, long callbackTime) {
        this.taskId = taskId;
        this.queueName = queueName;
        this.priority = priority;
        this.callbackTime = callbackTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getQueueName() {
        return queueName;
    }

    public int getPriority() {
        return priority;
    }

    public long getCallbackTime() {
        return callbackTime;
    }
}