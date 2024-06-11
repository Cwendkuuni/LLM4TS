package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(1L);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(1L);
    }

    @Test
    public void testExecute_taskNotFound_logErrorAndExit() {
        String taskId = "taskId1";
        doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(LOGGER, times(1)).error("TaskId: {} could not be found while executing {}", taskId, systemTask);
    }

    @Test
    public void testExecute_workflowCompleted_removeTaskAndDecideWorkflow() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        verify(queueDAO, times(1)).remove(anyString(), task.getId());
        verify(workflowExecutor, times(1)).decide(workflow.getId());
    }

    @Test
    public void testExecute_taskIsTerminal_removeTask() {
        TaskModel task = Mockito.mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        verify(queueDAO, times(1)).remove(anyString(), task.getId());
    }

    @Test
    public void testExecute_taskIsScheduled_postponeTask() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        Queue queue = Mockito.mock(Queue.class);
        verify(queueDAO, times(1)).postpone(anyString(), task.getId(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_queueSizeExceedsLimit_postponeTask() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        Queue queue = Mockito.mock(Queue.class);
        verify(queueDAO, times(1)).postpone(anyString(), task.getId(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_rateLimitExceeded_postponeTask() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowInstanceStatus.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        Queue queue = Mockito.mock(Queue.class);
        verify(queueDAO, times(1)).postpone(anyString(), task.getId(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = Mockito.mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, task.getId(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskFound_returnTask() {
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        assertEquals(task, returnedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound_returnNull() {
        doReturn(null).when(executionDAOFacade).getTaskModel(anyString());

        TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        assertNull(returnedTask);
    }
}