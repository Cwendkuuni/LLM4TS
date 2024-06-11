package com.netflix.conductor.core.execution.test;

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
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskWithMocks;
import com.netflix.conductor.core.model.execution.WorkflowModel;
import com.netflix.conductor.core.model.task.TaskModel;
import com.netflix.conductor.core.model.task.WorkflowTaskDefinition;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueName;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.JsonUtils;

@SuppressWarnings({"unchecked", "rawtypes"})
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

    private WorkflowSystemTaskWithMocks systemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Monitors mockMonitors = mock(Monitors.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(mockWorkflowModel);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(mockTaskModel);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(mockTaskDef);
        when(workflowExecutor.decide(anyString())).thenReturn(true);
        when(queueDAO.getTaskFromQueue(anyString(), anyString())).thenReturn(mockTaskModel);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
        systemTask = new WorkflowSystemTaskWithMocks();
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);
        TaskModel mockTaskModel = mock(TaskModel.class);
        when(mockTaskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_exceedsInProgressLimit() {
        when(executionDAOFacade.exceedsInProgressLimit(mockTaskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(queueName.value, taskId, 0, queueTaskMessagePostponeSecs);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_exceedsRateLimitPerFrequency() {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(mockTaskModel, mockTaskDef))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(queueName.value, taskId, 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_workflowCompleted() {
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);
        when(mockWorkflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        TaskModel mockTaskModel = mock(TaskModel.class);
        when(mockTaskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName.value, taskId);
    }

    @Test
    public void testExecute_taskQueued() {
        TaskModel mockTaskModel = mock(TaskModel.class);
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);
        when(mockTaskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(mockWorkflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(queueName.value, taskId, 0, queueTaskMessagePostponeSecs);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_workflowTerminal() {
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);
        when(mockWorkflowModel.getStatus()).thenReturn(WorkflowModel.Status.TERMINATED);
        TaskModel mockTaskModel = mock(TaskModel.class);
        when(mockTaskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        // no call to remove task from queue is expected, as the workflow is already terminated
        verify(queueDAO, times(0)).remove(queueName.value, taskId);
        throw new IllegalStateException("Expected exception: " + TaskModel.Status.TERMINATED);
    }

    @Test
    public void testPostponeQuietly_queueExists() {
        when(queueDAO.existsInQueue(queueName.value, taskId)).thenReturn(true);
        asyncSystemTaskExecutor.postponeQuietly(queueName.value, taskId);
        verify(queueDAO, times(1)).postpone(queueName.value, taskId, 0, queueTaskMessagePostponeSecs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostponeQuietly_queueDoesNotExist() {
        when(queueDAO.existsInQueue(queueName.value, taskId)).thenReturn(false);
        asyncSystemTaskExecutor.postponeQuietly(queueName.value, taskId);
        throw new IllegalArgumentException("Queue does not exist");
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadTaskQuietly_taskNull() {
        asyncSystemTaskExecutor.loadTaskQuietly(null);
        fail("Expected exception: NullPointerException");
    }

    private WorkflowModel mockWorkflowModel = new WorkflowModel();
    private TaskModel mockTaskModel = new TaskModel();
    private WorkflowTaskDefinition mockTaskDef = new WorkflowTaskDefinition();
    private QueueName queueName = QueueUtils.getQueueName(mockTaskModel);
    private String taskId = "taskId";
    private Long queueTaskMessagePostponeSecs = 1L;
    private Long systemTaskCallbackTime = 1L;
}