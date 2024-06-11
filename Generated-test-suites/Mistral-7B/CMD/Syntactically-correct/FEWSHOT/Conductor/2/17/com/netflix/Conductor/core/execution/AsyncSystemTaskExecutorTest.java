package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModelImpl;
import org.junit.Before;
import org.junit.Test;
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
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private Monitors monitors;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(1L);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(1L);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new Object() {});
        when(queueDAO).thenReturn(queueDAOImplMock());
        when(workflowExecutor.decide(anyString())).thenReturn(true);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(systemTask.getTaskType()).thenReturn("TestTaskType");
        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);
    }

    @Test
    public void testExecute_taskExistsAndStatusIsTerminal() {
        String taskId = "testTaskId";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove("TestQueueName", taskId);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_taskDoesNotExist() {
        String taskId = "testTaskId";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove("TestQueueName", systemTask.getTaskType());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_taskIsInProgress() {
        String taskId = "testTaskId";
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone("TestQueueName", taskId, 1, 1);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_taskIsScheduled() {
        String taskId = "testTaskId";
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testExecute_taskIsTerminal() {
        String taskId = "testTaskId";
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove("TestQueueName", taskId);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_concurrencyLimitExceeded() {
        String taskId = "testTaskId";
        when(task.getTaskDefName()).thenReturn("TestTaskDefName");
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove("TestQueueName", systemTask.getTaskType());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_rateLimitExceeded() {
        String taskId = "testTaskId";
        when(task.getTaskDefName()).thenReturn("TestTaskDefName");
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove("TestQueueName", systemTask.getTaskType());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    private QueueDAOImpl queueDAOImplMock() {
        QueueDAOImpl queueDAOImpl = mock(QueueDAOImpl.class);
        when(queueDAOImpl.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(1L);
        return queueDAOImpl;
    }
}