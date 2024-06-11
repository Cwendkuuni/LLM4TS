package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;

@RunWith(MockitoJUnitRunner.class)
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
    private WorkflowModel workflowModel;

    @Test
    public void testExecute_taskIsNotFound_taskIsRemovedFromQueue() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_taskIsCompleted_taskIsRemovedFromQueue() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETE);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_taskIsTerminal_taskIsRemovedFromQueue() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.FAILED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_taskIsScheduled_itPostponesTheTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).postpone("queueName", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsRateLimited_itPostponesTheTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefMock());
        when(task.getRateLimitPerFrequency()).thenReturn(1L);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).postpone("queueName", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsConcurrentlyLimited_itPostponesTheTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(executionDAOFacade).exceedsInProgressLimit(task);
        verify(queueDAO).postpone("queueName", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecute_workflowIsTerminal_taskIsCancelled() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.COMPLETE);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(task).setStatus(ExecutionStatus.CANCELED);
    }

    @Test
    public void testExecute_workflowIsTerminal_queueMessageIsRemoved() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.COMPLETE);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_taskIsRunning_itUpdatesTheTaskInDatabase() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(task).setStatus(ExecutionStatus.IN_PROGRESS);
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void testExecute_taskIsRunning_itDecidesTheWorkflow() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void testExecute_taskIsInProgress_itCallbacksAfterSeconds() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(task).setCallbackAfterSeconds(systemTaskCallbackTime);
        verify(queueDAO).postpone("queueName", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsAsyncComplete_itShouldRemoveTaskFromQueue() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_taskIsAsyncComplete_itShouldUpdateTaskStatus() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(task).setStatus(ExecutionStatus.COMPLETE);
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void testExecute_taskIsInProgress_itMonitorsTheQueueWaitTime() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
        when(systemTask.getEvaluationOffset(task, systemTaskCallbackTime)).isPresent().thenReturn(100L);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(Monitors.class).recordQueueWaitTime(task.getTaskType(), 100L);
    }
}

class TaskDefMock implements TaskDef {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getTaskQueue() {
        return "queueName";
    }

    @Override
    public String getTaskType() {
        return "taskType";
    }

    @Override
    public String getTaskExecutor() {
        return "taskExecutor";
    }

    @Override
    public String getTaskConfig() {
        return null;
    }

    @Override
    public String getTaskWorkflowConfig() {
        return null;
    }

    @Override
    public String getTaskPriorities() {
        return null;
    }

    @Override
    public int getTaskPriority() {
        return 0;
    }

    @Override
    public long getTaskExecutionPostponeDuration() {
        return 0;
    }

    @Override
    public long getTaskConcurrentLimit() {
        return 0;
    }

    @Override
    public long getTaskRateLimitPerFrequency() {
        return 0;
    }

    @Override
    public String getTaskDescription() {
        return null;
    }
}