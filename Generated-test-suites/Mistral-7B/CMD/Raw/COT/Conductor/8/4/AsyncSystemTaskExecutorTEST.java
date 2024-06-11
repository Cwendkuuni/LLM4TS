package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.workflow.WorkflowModelEntity;

@SuppressWarnings("unchecked")
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
    private Logger logger;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private String taskId = "testTaskId";
    private WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    private TaskModel task;
    private WorkflowModel workflow;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.get(anyString(), anyString())).thenReturn(null);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt())).thenReturn(QueueMessage.EMPTY);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(mock(Duration.class));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(mock(Duration.class));
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
        asyncSystemTaskExecutor.setLogger(logger);
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_taskDoesNotExist() {
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(logger, times(1)).error("TaskId: {} could not be found while executing {}", taskId, systemTask);
    }

    @Test
    public void testExecute_taskIsTerminal() {
        task.setStatus(TaskModel.Status.TERMINAL);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskIsScheduled() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), anyInt());
    }

    @Test
    public void testExecute_taskInProgress() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, times(1)).execute(workflow, task, workflowExecutor);
    }

    @Test(expected = Exception.class)
    public void testExecute_exceptionThrown() {
        doThrow(new RuntimeException("Test exception")).when(systemTask).execute(workflow, task, workflowExecutor);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_workflowTerminal() {
        workflow.setStatus(WorkflowInstanceStatus.TERMINAL);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskRateLimitPerFrequencyExceeded() {
        task.setRateLimitPerFrequency(5);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(mock(WorkflowModelEntity.class));
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(logger, times(1)).warn(
                "RateLimit Execution limited for {}, limit:{}",
                taskId,
                task.getRateLimitPerFrequency());
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), anyInt());
    }

    @Test
    public void testExecute_concurrentExecutionLimitExceeded() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(logger, times(1)).warn(
                "Concurrent Execution limited for {}",
                taskId);
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), anyInt());
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), anyInt());
    }

    @Test
    public void testLoadTaskQuietly_taskExists() {
        task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskDoesNotExist() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.empty());
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(loadedTask);
    }
}