package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueNameGenerator;
import com.netflix.conductor.utils.QueueUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private QueueNameGenerator queueNameGenerator;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(new Duration(5000L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(new Duration(5000L));
        when(queueNameGenerator.generateQueueName(task)).thenReturn("testQueue");
    }

    @After
    public void cleanup() {
        // cleanup mocks
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // verify that the task was not found and was removed from the queue
        assertEquals(0, queueDAO.count(QueueUtils.getQueueName(task)));
        assertEquals(null, executionDAOFacade.getTaskModel(task.getTaskId()));
    }

    @Test
    public void testExecute_completedTask() {
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true)).thenReturn(
                new WorkflowModel());
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // verify that the task was marked as completed and removed from the queue
        assertEquals(0, queueDAO.count(QueueUtils.getQueueName(task)));
        assertEquals(TaskModel.Status.COMPLETED, executionDAOFacade.getTaskModel(task.getTaskId()).getStatus());
    }

    @Test
    public void testExecute_inProgressTask() {
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true)).thenReturn(
                new WorkflowModel());
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // verify that the task status was updated and remained in the queue
        assertSame(task, executionDAOFacade.getTaskModel(task.getTaskId()));
        assertTrue(queueDAO.contains(QueueUtils.getQueueName(task), task.getTaskId()));
    }

    @Test
    public void testExecute_postponedTask() {
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true)).thenReturn(
                new WorkflowModel());
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // verify that the task was postponed in the queue
        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(eq("testQueue"), taskArgumentCaptor.capture(), anyInt(), anyInt());
        TaskModel postponedTask = taskArgumentCaptor.getValue();
        assertEquals(task, postponedTask);
        assertEquals(task.getCallbackAfterSeconds(), asyncSystemTaskExecutor.getSystemTaskCallbackTime());
    }

    @Test
    public void testExecute_exceedsConcurrentExecutionLimit() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // verify that the task was postponed in the queue
        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(eq("testQueue"), taskArgumentCaptor.capture(), anyInt(), anyInt());
        TaskModel postponedTask = taskArgumentCaptor.getValue();
        assertEquals(task, postponedTask);
        assertEquals(task.getCallbackAfterSeconds(), asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs());
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // verify that the task was postponed in the queue
        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(eq("testQueue"), taskArgumentCaptor.capture(), anyInt(), anyInt());
        TaskModel postponedTask = taskArgumentCaptor.getValue();
        assertEquals(task, postponedTask);
        assertEquals(task.getCallbackAfterSeconds(), asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs());
    }

    @Test
    public void testExecute_workflowCompleted() {
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true)).thenReturn(
                new WorkflowModel());
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false).getStatus())
                .thenReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // verify that the task was marked as cancelled and removed from the queue
        assertEquals(TaskModel.Status.CANCELED, executionDAOFacade.getTaskModel(task.getTaskId()).getStatus());
        assertEquals(0, queueDAO.count(QueueUtils.getQueueName(task)));
    }
}