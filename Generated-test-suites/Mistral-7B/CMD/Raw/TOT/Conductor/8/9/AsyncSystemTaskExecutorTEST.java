package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

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
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(1000L);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(5000L);
    }

    @Test
    public void testExecute_taskIsNull_doesNotProceed() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // The task should not be removed from the queue and the workflow should not be evaluated
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskIsCompleted_removesTaskFromQueue() {
        String taskId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName(taskModel), taskId);
        verify(workflowExecutor).decide(workflowModel.getId());
    }

    @Test
    public void testExecute_taskIsInProgress_doesNotRemoveTaskFromQueue() {
        String taskId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verifyZeroInteractions(queueDAO);
        verifyZeroInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskIsScheduled_postponesTaskInQueue() {
        String taskId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        Monitors monitors = mock(Monitors.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false)).thenReturn(workflowModel);
        when(queueDAO.postpone(QueueUtils.getQueueName(taskModel), taskId, 1, 5000L)).thenReturn(true);

        when(conductorProperties.getMonitors()).thenReturn(monitors);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(QueueUtils.getQueueName(taskModel), taskId, 1, 5000L);
        verifyZeroInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskIsTerminal_removesTaskFromQueue() {
        String taskId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName(taskModel), taskId);
        verify(workflowExecutor).decide(workflowModel.getId());
    }

    @Test
    public void testExecute_taskIsRateLimited_postponesTaskInQueue() {
        String taskId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        Monitors monitors = mock(Monitors.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false)).thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(mock(TaskDefinition.class));

        when(conductorProperties.getMonitors()).thenReturn(monitors);
        when(queueDAO.postpone(QueueUtils.getQueueName(taskModel), taskId, 1, 10000L)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(QueueUtils.getQueueName(taskModel), taskId, 1, 10000L);
        verifyZeroInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskIsInProgressAndIsAsyncComplete_removesTaskFromQueue() {
        String taskId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verifyZeroInteractions(queueDAO);
        verifyZeroInteractions(workflowExecutor);
    }
}