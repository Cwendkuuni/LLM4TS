package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.time.Duration;
import java.util.Optional;

/**
 * Test class for {@link AsyncSystemTaskExecutor}.
 */
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

    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        task = new TaskModel();
        workflow = new WorkflowModel();
        systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(anyString()))
                .thenReturn(task);
        when(queueDAO.getQueueName(any(TaskModel.class)))
                .thenReturn("testQueue");
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArgs();
                        String queueName = (String) args[0];
                        String taskId = (String) args[1];
                        int priority = (int) args[2];
                        long delay = (long) args[3];
                        LOGGER.debug("Postpone task: {} in queue: {}", taskId, queueName);
                        return null;
                    }
                });
        when(queueDAO.remove(anyString(), anyString()))
                .thenAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArgs();
                        String queueName = (String) args[0];
                        String taskId = (String) args[1];
                        LOGGER.debug("Remove task: {} from queue: {}", taskId, queueName);
                        return null;
                    }
                });
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class)))
                .thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any()))
                .thenReturn(false);
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade);
        reset(queueDAO);
        reset(metadataDAO);
        reset(conductorProperties);
        reset(workflowExecutor);
    }

    /**
     * Test for loading a task quietly.
     */
    @Test
    public void testLoadTaskQuietly() {
        TaskModel expectedTask = new TaskModel();
        when(executionDAOFacade.getTaskModel("taskId"))
                .thenReturn(expectedTask);
        TaskModel actualTask = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        assertEquals(expectedTask, actualTask);
    }

    /**
     * Test for exceeding the in-progress limit.
     */
    @Test(expected = RuntimeException.class)
    public void testExceedsInProgressLimit() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.exceedsInProgressLimit(task))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
    }

    /**
     * Test for exceeding the rate limit per frequency.
     */
    @Test(expected = RuntimeException.class)
    public void testExceedsRateLimitPerFrequency() {
        task.setRateLimitPerFrequency(1);
        when(metadataDAO.getTaskDef(task.getTaskDefName()))
                .thenReturn(new TaskDef());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
    }

    /**
     * Test for a task that is already completed.
     */
    @Test
    public void testTaskAlreadyCompleted() {
        workflow.setStatus(WorkflowModel.Status.COMPLETED);
        when(workflow.getStatus()).thenReturn(workflow.getStatus());
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, times(0))
                .postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, times(0))
                .remove(anyString(), anyString());
    }

    /**
     * Test for a task that is scheduled.
     */
    @Test
    public void testTaskScheduled() throws InterruptedException {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, times(1))
                .postpone(eq("testQueue"), eq("taskId"), anyInt(), anyLong());
        verify(queueDAO, times(0))
                .remove(anyString(), anyString());

        Thread.sleep(asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
        verify(queueDAO, times(1))
                .remove(eq("testQueue"), eq("taskId"));
    }

    /**
     * Test for a task that is in progress.
     */
    @Test
    public void testTaskInProgress() throws InterruptedException {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);
        when(systemTask.isAsyncComplete(task))
                .thenReturn(Optional.empty());

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, times(1))
                .postpone(eq("testQueue"), eq("taskId"), anyInt(), anyLong());

        Thread.sleep(asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
        verify(queueDAO, times(1))
                .remove(eq("testQueue"), eq("taskId"));

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, times(1))
                .postpone(eq("testQueue"), eq("taskId"), anyInt(), anyLong());

        Thread.sleep(asyncSystemTaskExecutor.systemTaskCallbackTime);
        verify(queueDAO, times(1))
                .remove(eq("testQueue"), eq("taskId"));
    }

    /**
     * Test for a task that is canceled.
     */
    @Test
    public void testTaskCanceled() throws InterruptedException {
        task.setStatus(TaskModel.Status.CANCELED);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.CANCELED);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, times(0))
                .postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, times(1))
                .remove(eq("testQueue"), eq("taskId"));
    }
}