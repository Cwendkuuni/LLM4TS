package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.util.PropertiesUtil;

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel task;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        PropertiesUtil.setSystemProperty("conductor.task.execution.postpone.duration", "1000");

        task = new TaskModel();
        task.setTaskDefName("testTaskDef");
        task.setTaskId("testTaskId");
        task.setWorkflowInstanceId("testWorkflowId");
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setWorkflowPriority(1);
        task.setQueueName(QueueUtils.getQueueName(task));
        task.setCallbackAfterSeconds(0L);
    }

    @After
    public void tearDown() {
        asyncSystemTaskExecutor = null;
        task = null;
    }

    @Test
    public void testExecute_TaskIsTerminal_QueueIsRemoved() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getWorkflowModel(eq("testWorkflowId"), anyBoolean()))
                .thenReturn(workflowModel);

        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, times(1)).remove(eq(task.getQueueName()), eq(task.getTaskId()));
    }

    @Test
    public void testExecute_TaskIsTerminal_WorkflowIsEvaluated() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getWorkflowModel(eq("testWorkflowId"), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(workflowExecutor, times(1)).decide("testWorkflowId");
    }

    @Test
    public void testExecute_TaskIsNotTerminal_QueueIsPostponed() {
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, times(1)).postpone(
                eq(task.getQueueName()),
                eq(task.getTaskId()),
                eq(task.getWorkflowPriority()),
                eq(asyncSystemTaskExecutor.getSystemTaskCallbackTime() * 1000L));
    }

    @Test
    public void testExecute_TaskAlreadyCompleted_QueueIsRemoved() {
        task.setStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, times(1)).remove(eq(task.getQueueName()), eq(task.getTaskId()));
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_TaskExecutionLimitExceeded_QueueIsPostponed() {
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.of(task));
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, times(1)).postpone(
                eq(task.getQueueName()),
                eq(task.getTaskId()),
                eq(task.getWorkflowPriority()),
                eq(asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs() * 1000L));

        // Exception should be thrown
        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_RateLimitPerFrequencyExceeded_QueueIsPostponed() {
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.of(task));
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowModel());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any(WorkflowModel.class)))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        // Exception should be thrown
        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");
    }

    @Test
    public void testExecute_TaskIsInProgress() {
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(systemTask, times(1)).execute(any(), any(), any());
    }

    @Test
    public void testExecute_TaskIsAsyncComplete() {
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setAsyncComplete(true);

        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, times(1)).remove(eq(task.getQueueName()), eq(task.getTaskId()));
    }

    @Test
    public void testExecute_TaskIsCancelled() {
        task.setStatus(ExecutionStatus.CANCELED);

        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, times(1)).remove(eq(task.getQueueName()), eq(task.getTaskId()));
    }

    @Test
    public void testPostponeQuietly_QueueIsPostponed() {
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(true);

        asyncSystemTaskExecutor.postponeQuietly("testQueueName", task);

        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                eq(asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs() * 1000L));
    }

    @Test
    public void testLoadTaskQuietly_TaskIsLoadingSuccessfully() {
        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.of(task));

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

        assertNotNull(loadedTask);
        assertEquals("testTaskId", loadedTask.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly_TaskIsNotLoadingSuccessfully() {
        when(executionDAOFacade.getTaskModel(eq("testTaskId"))).thenReturn(Optional.empty());

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

        assertNull(loadedTask);
    }

    private long getQueueTaskMessagePostponeSecs() {
        return Long.parseLong(System.getProperty("conductor.task.execution.postpone.duration"));
    }

    private long getSystemTaskCallbackTime() {
        return Long.parseLong(System.getProperty("conductor.systemTask.callback.duration"));
    }
}