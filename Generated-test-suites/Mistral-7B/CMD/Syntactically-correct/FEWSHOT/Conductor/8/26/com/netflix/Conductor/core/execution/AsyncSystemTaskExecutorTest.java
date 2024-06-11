package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModelImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {
    private static final String QUEUE_NAME = "testQueueName";
    private static final String TASK_ID = "testTaskId";
    private static final String WORKFLOW_ID = "testWorkflowId";
    private static final String TASK_TYPE = "testTaskType";
    private static final String TASK_DEF_NAME = "testTaskDefName";
    private static final TaskModel TASK = new TaskModel();
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(new Duration(5000));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(new Duration(5000));
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotInExecutionDAO_NullTask() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        verifyNoInteractions(queueDAO);
        verifyNoInteractions(executionDAOFacade);
        verifyNoInteractions(metadataDAO);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted_RemoveTaskFromQueue() {
        TASK.setStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setStatus(WorkflowModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(TASK);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskInExecutionDAO_PostponeTask() {
        TASK.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(TASK);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);
        when(queueDAO.getQueueName(TASK)).thenReturn(QUEUE_NAME);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

        verify(queueDAO).postpone(
                QUEUE_NAME,
                TASK_ID,
                0,
                5000);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskInExecutionDAO_ExecuteTask() {
        TASK.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(TASK);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);
        when(queueDAO.getQueueName(TASK)).thenReturn(QUEUE_NAME);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(workflowExecutor).execute(eq(workflow), eq(TASK), any());
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskInExecutionDAO_ExecuteTaskAsync() {
        TASK.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModelImpl();

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(TASK);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);
        when(queueDAO.getQueueName(TASK)).thenReturn(QUEUE_NAME);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        verify(workflowExecutor).execute(eq(workflow), eq(TASK), any());
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testExecute_TaskInExecutionDAO_TaskCompletionCallback() {
        TASK.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(TASK);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);
        when(queueDAO.getQueueName(TASK)).thenReturn(QUEUE_NAME);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doAnswer(invocation -> {
            TASK.setStatus(TaskModel.Status.COMPLETED);
            return null;
        }).when(systemTask).isAsyncComplete(TASK);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        verifyZeroInteractions(workflowExecutor);
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskInExecutionDAO_TaskCompletionCallbackPostponed() {
        TASK.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(TASK);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);
        when(queueDAO.getQueueName(TASK)).thenReturn(QUEUE_NAME);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doAnswer(invocation -> {
            TASK.setStatus(TaskModel.Status.COMPLETED);
            return null;
        }).when(systemTask).isAsyncComplete(TASK);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        verifyZeroInteractions(workflowExecutor);
        verify(queueDAO).postpone(
                QUEUE_NAME,
                TASK_ID,
                0,
                5000);
    }

    @Test
    public void testExecute_TaskInExecutionDAO_TaskFailure() {
        TASK.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(TASK);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);
        when(queueDAO.getQueueName(TASK)).thenReturn(QUEUE_NAME);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doThrow(new RuntimeException("Test exception")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        verifyZeroInteractions(workflowExecutor);
        verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testPostponeQuietly_NullTask() {
        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, null);

        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testPostponeQuietly_TaskPostponed() {
        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, TASK);

        verify(queueDAO).postpone(
                QUEUE_NAME,
                TASK_ID,
                0,
                5000);
    }

    @Test
    public void testLoadTaskQuietly_NullTask() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertNull(loadedTask);
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade);
        reset(queueDAO);
        reset(metadataDAO);
        reset(workflowExecutor);
    }
}