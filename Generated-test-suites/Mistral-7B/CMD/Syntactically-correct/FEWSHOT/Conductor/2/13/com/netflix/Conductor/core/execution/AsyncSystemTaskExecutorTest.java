package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.data.TaskModel;
import com.netflix.conductor.common.data.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);
    private static final String QUEUE_NAME = "testQueueName";
    private static final String TASK_TYPE = "testTaskType";

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Captor private ArgumentCaptor<String> queueNameCaptor;
    @Captor private ArgumentCaptor<String> taskIdCaptor;

    @Test
    public void testExecute_TaskNotFound_QueueMessageRemoved() {
        TaskModel task = null;
        when(executionDAOFacade.getTaskModel(taskIdCaptor.capture())).thenReturn(task);
        when(queueDAO.remove(queueNameCaptor.capture(), taskIdCaptor.capture()))
                .thenReturn(true);

        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(queueNameCaptor.getValue(), QUEUE_NAME);
        assertEquals(taskIdCaptor.getValue(), taskId);
        assertFalse(taskIdCaptor.getValue().equals(task.getTaskId()));
    }

    @Test
    public void testExecute_TaskCompleted_QueueMessageRemoved() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskIdCaptor.capture())).thenReturn(task);
        when(queueDAO.remove(queueNameCaptor.capture(), taskIdCaptor.capture()))
                .thenReturn(true);

        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(queueNameCaptor.getValue(), QUEUE_NAME);
        assertEquals(taskIdCaptor.getValue(), taskId);
        assertFalse(taskIdCaptor.getValue().equals(task.getTaskId()));
    }

    @Test
    public void testExecute_TaskInProgress_QueueMessagePostponed() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskIdCaptor.capture())).thenReturn(task);
        when(queueDAO.postpone(queueNameCaptor.capture(), taskIdCaptor.capture(), 0, 1))
                .thenReturn(true);

        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(queueNameCaptor.getValue(), QUEUE_NAME);
        assertEquals(taskIdCaptor.getValue(), taskId);
        assertTrue(taskIdCaptor.getValue().equals(task.getTaskId()));
    }

    @Test
    public void testExecute_TaskError_QueueMessageNotRemoved() {
        TaskModel task = new TaskModel();
        TaskModel expectedTask = new TaskModel();
        expectedTask.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskIdCaptor.capture())).thenReturn(task);
        when(queueDAO.remove(queueNameCaptor.capture(), taskIdCaptor.capture())).thenThrow(new RuntimeException());

        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(queueNameCaptor.getValue(), QUEUE_NAME);
        assertEquals(taskIdCaptor.getValue(), taskId);
        assertSame(task, expectedTask);
    }

    @Test
    public void testExecute_TaskInProgress_CallbackAfterSecondsSet() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskIdCaptor.capture())).thenReturn(task);

        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        when(systemTask.getEvaluationOffset(task, 1)).thenReturn(1000L);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertTrue(task.getCallbackAfterSeconds() > 0);
    }

    @Test
    public void testExecute_TaskCompleted_CallbackAfterSecondsNotSet() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskIdCaptor.capture())).thenReturn(task);

        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertNull(task.getCallbackAfterSeconds());
    }

    @Test
    public void testExecute_TaskInProgress_WorkflowEvaluated() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskIdCaptor.capture())).thenReturn(task);
        when(workflowExecutor.decide(taskIdCaptor.capture())).thenReturn(true);

        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(taskId, taskIdCaptor.getValue());
        verify(workflowExecutor).decide(taskIdCaptor.capture());
    }

    @Test
    public void testExecute_TaskCompleted_WorkflowNotEvaluated() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskIdCaptor.capture())).thenReturn(task);

        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyZeroInteractions(workflowExecutor);
    }
}