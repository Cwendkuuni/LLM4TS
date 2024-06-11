package com.netflix.conductor.core.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.QueueName;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private Queue queue;

    @Mock
    private Monitors monitors;

    @Before
    public void setup() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(queueDAO.get(QueueName.fromString(anyString()), anyString())).thenReturn(queue);
        when(workflowExecutor.decide(anyString())).thenReturn(Void.TYPE);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(mock(Duration.class));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(mock(Duration.class));
        when(monitors.recordQueueWaitTime(anyString(), anyLong())).thenReturn(Void.TYPE);
    }

    @Test
    public void testLoadTaskQuietlyNullTask() {
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testLoadTaskQuietlyNotNullTask() {
        assertThat(asyncSystemTaskExecutor.loadTaskQuietly(taskId), is(task));
    }

    @Test
    public void testExecuteNullTaskId() {
        assertNull(asyncSystemTaskExecutor.execute(systemTask, null));
    }

    @Test
    public void testExecuteNullSystemTask() {
        assertNull(asyncSystemTaskExecutor.execute(null, taskId));
    }

    @Test
    public void testExecuteTaskInProgress() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(
                queueName, task.getTaskId(), task.getWorkflowPriority(), systemTaskCallbackTime);
    }

    @Test
    public void testExecuteTaskScheduled() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).start(workflowModel, task, workflowExecutor);
    }

    @Test
    public void testExecuteTaskCompleted() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(queueName, task.getTaskId());
    }

    @Test
    public void testExecuteTaskTerminal() {
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(queueName, task.getTaskId());
    }

    @Test
    public void testExecuteException() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        try {
            asyncSystemTaskExecutor.execute(systemTask, taskId);
        } catch (Exception e) {
            // expected
        }

        verify(monitors).error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
    }
}