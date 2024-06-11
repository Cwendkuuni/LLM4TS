package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
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
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueName;
import com.netflix.conductor.metrics.Monitors;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    private TaskModel task;

    @Before
    public void setup() {
        task = new TaskModel();
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(new WorkflowModel());
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(workflowExecutor.decide(anyString())).thenReturn(true);
        when(workflowExecutor.getWorkerName()).thenReturn("TestWorker");

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsCompleted_TaskIsRemovedFromQueue() {
        String queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(queueName)).thenReturn(Optional.of(new QueueMessage()));
        when(queueDAO.remove(queueName, task.getTaskId())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO).remove(queueName, task.getTaskId());
    }

    @Test
    public void testExecute_TaskIsInProgress_TaskIsUpdatedAndCallbackIsSet() {
        String queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(queueName)).thenReturn(Optional.of(new QueueMessage()));
        when(queueDAO.postpone(
                queueName,
                task.getTaskId(),
                QueuePriority.HIGH,
                eq(queueTaskMessagePostponeSecs))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO).postpone(
                eq(queueName),
                eq(task.getTaskId()),
                eq(QueuePriority.HIGH),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_TaskIsTerminal_TaskIsRemovedFromQueue() {
        String queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(queueName)).thenReturn(Optional.of(new QueueMessage()));
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(new WorkflowModel(ExecutionStatus.COMPLETED));
        when(queueDAO.remove(queueName, task.getTaskId())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO).remove(queueName, task.getTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_TaskIsTerminal_ExceptionIsThrown() {
        String queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(queueName)).thenReturn(Optional.of(new QueueMessage()));
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(new WorkflowModel(ExecutionStatus.COMPLETED));

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO, never()).remove(queueName, task.getTaskId());
    }

    @Test
    public void testExecute_TaskIsScheduled_TaskIsPostponed() {
        String queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(queueName)).thenReturn(Optional.of(new QueueMessage()));
        when(queueDAO.postpone(
                queueName,
                task.getTaskId(),
                QueuePriority.HIGH,
                eq(queueTaskMessagePostponeSecs))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO).postpone(
                eq(queueName),
                eq(task.getTaskId()),
                eq(QueuePriority.HIGH),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_TaskIsRateLimited_TaskIsPostponed() {
        String queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(queueName)).thenReturn(Optional.of(new QueueMessage()));
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO).postpone(
                eq(queueName),
                eq(task.getTaskId()),
                any(),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_TaskIsConcurrentlyExecutionLimited_TaskIsPostponed() {
        String queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(queueName)).thenReturn(Optional.of(new QueueMessage()));
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(queueDAO).postpone(
                eq(queueName),
                eq(task.getTaskId()),
                any(),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testPostponeQuietly_TaskIsPostponed() {
        String queueName = QueueUtils.getQueueName(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(
                eq(queueName),
                eq(task.getTaskId()),
                any(),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadTaskQuietly_TaskIsNull() {
        asyncSystemTaskExecutor.loadTaskQuietly(null);
    }
}

class TaskDefinition {
    // Empty class
}

class QueueMessage {
    // Empty class
}

class QueueUtils {
    // Static helper class
}