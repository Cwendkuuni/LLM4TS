package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

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
import com.netflix.conductor.core.model.Queue;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.dao.WorkflowInstanceDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.ConductorTestUtils;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    private static final String TASK_ID = "mockTaskId";
    private static final String WORKFLOW_ID = "mockWorkflowId";
    private static final String QUEUE_NAME = "mockQueueName";
    private static final String TASK_TYPE = "mockTaskType";

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

    @Mock
    private WorkflowSystemTask mockSystemTask;

    @Mock
    private TaskModel mockTask;

    @Mock
    private WorkflowModel mockWorkflow;

    @Mock
    private WorkflowInstanceDAO workflowInstanceDAO;

    @Mock
    private WorkflowDAO workflowDAO;

    @Mock
    private Monitors monitors;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getExecutionDAO()).thenReturn(workflowInstanceDAO);
        when(executionDAOFacade.getWorkflowDAO()).thenReturn(workflowDAO);
        when(workflowInstanceDAO.getWorkflowModel(WORKFLOW_ID)).thenReturn(mockWorkflow);
        when(metadataDAO.getTaskDef(TASK_TYPE)).thenReturn(mockSystemTask);
        when(queueDAO.getQueue(QUEUE_NAME)).thenReturn(new Queue());
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(null);
        when(monitors.recordQueueWaitTime(anyString(), anyLong())).thenReturn(null);
        when(monitors.error(anyString(), anyString())).thenReturn(null);
        asyncSystemTaskExecutor.setMonitors(monitors);
    }

    @After
    public void tearDown() {
        ConductorTestUtils.cleanUpMocks(this);
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(LOGGER).error("TaskId: {} could not be found while executing {}", TASK_ID, mockSystemTask);
    }

    @Test
    public void testExecute_taskInProgress() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(LOGGER).debug("Task: {} fetched from execution DAO for taskId: {}", mockTask, TASK_ID);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskTerminal() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_executeTask_inScheduledState() throws InterruptedException {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(mockSystemTask.isAsyncComplete(mockTask)).thenReturn(false);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, anyLong());
        verify(mockTask).setCallbackAfterSeconds(anyLong());
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_executeTask_inProgressState() throws InterruptedException {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(mockSystemTask.isAsyncComplete(mockTask)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_executeTask_completedState() throws InterruptedException {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(mockSystemTask.isAsyncComplete(mockTask)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_terminalWorkflowState() throws InterruptedException {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(mockWorkflow.getStatus()).thenReturn(ExecutionStatus.TERMINAL);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_inProgressLimitExceeded() throws InterruptedException {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(mockTask.getTaskDefName()).thenReturn("taskDefName");
        when(executionDAOFacade.exceedsInProgressLimit(mockTask)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, anyLong());
        verify(mockTask).setCallbackAfterSeconds(anyLong());
        verify(LOGGER).warn("Concurrent Execution limited for {}:{}, limit:{}", TASK_ID, "taskDefName", 1);
    }

    @Test
    public void testExecute_rateLimitExceeded() throws InterruptedException {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(mockTask.getTaskDefName()).thenReturn("taskDefName");
        when(executionDAOFacade.exceedsRateLimitPerFrequency(mockTask, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockSystemTask, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, anyLong());
        verify(mockTask).setCallbackAfterSeconds(anyLong());
        verify(LOGGER).warn(
                "RateLimit Execution limited for {}:{}, limit:{}", TASK_ID, "taskDefName", 1);
    }

    @Test
    public void testPostponeQuietly() throws InterruptedException {
        when(queueDAO.getQueue(QUEUE_NAME)).thenReturn(new Queue());

        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, mockTask);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(mockTask);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertSame(mockTask, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID));
    }
}