package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.model.Queue;
import com.netflix.conductor.dao.model.WorkflowDefinition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowModel workflowModel;
    private TaskModel initialTaskModel;
    private WorkflowSystemTask systemTaskMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.executionDAOFacade = mock(ExecutionDAOFacade.class);
        this.queueDAO = mock(QueueDAO.class);
        this.metadataDAO = mock(MetadataDAO.class);
        this.conductorProperties = mock(ConductorProperties.class);
        this.workflowExecutor = mock(WorkflowExecutor.class);

        ConductorProperties conductorProperties = new ConductorProperties();
        when(this.conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(conductorProperties);
        when(this.conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(conductorProperties);

        this.systemTaskMock = mock(WorkflowSystemTask.class);
        this.initialTaskModel = new TaskModel();
        this.initialTaskModel.setTaskDefName("testTaskDefName");
        this.initialTaskModel.setTaskId("testTaskId");
        this.initialTaskModel.setStatus(TaskModel.Status.SCHEDULED);
        this.initialTaskModel.setWorkflowInstanceId("testWorkflowInstanceId");
        this.initialTaskModel.setQueueName("testQueueName");

        when(this.executionDAOFacade.getWorkflowModel(
                anyString(), anyBoolean())).thenReturn(this.workflowModel);
        when(this.executionDAOFacade.getTaskModel(anyString())).thenReturn(this.initialTaskModel);
        when(this.executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(this.initialTaskModel);

        when(this.metadataDAO.getTaskDef(anyString())).thenReturn(new WorkflowDefinition());

        when(this.queueDAO.postpone(
                anyString(), anyString(), anyInt(), anyLong())).thenReturn(new Queue());
        when(this.queueDAO.remove(anyString(), anyString())).thenReturn(true);

        this.executor = new AsyncSystemTaskExecutor(
                this.executionDAOFacade,
                this.queueDAO,
                this.metadataDAO,
                this.conductorProperties,
                this.workflowExecutor);
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_queueIsRemoved() {
        this.initialTaskModel.setStatus(TaskModel.Status.COMPLETED);

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        verify(this.queueDAO).remove(this.initialTaskModel.getQueueName(), this.initialTaskModel.getTaskId());
        verifyNoMoreInteractions(this.systemTaskMock);
    }

    @Test
    public void testExecute_taskIsAlreadyTerminal_queueIsRemoved() {
        this.initialTaskModel.setStatus(TaskModel.Status.FAILED);

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        verify(this.queueDAO).remove(this.initialTaskModel.getQueueName(), this.initialTaskModel.getTaskId());
        verifyNoMoreInteractions(this.systemTaskMock);
    }

    @Test
    public void testExecute_taskIsNotScheduledOrAsyncComplete_queueIsPostponed() {
        this.initialTaskModel.setStatus(TaskModel.Status.SCHEDULED);
        doThrow(new RuntimeException("Test Exception")).when(this.systemTaskMock).start(
                any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        verify(this.queueDAO).postpone(
                this.initialTaskModel.getQueueName(),
                this.initialTaskModel.getTaskId(),
                anyInt(),
                this.conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds());
        verifyNoMoreInteractions(this.systemTaskMock);
    }

    @Test
    public void testExecute_taskIsInProgress_executionIsCalled() {
        this.initialTaskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        verify(this.systemTaskMock).execute(
                this.workflowModel, this.initialTaskModel, this.workflowExecutor);
    }

    @Test
    public void testExecute_taskIsInProgress_callbackIsSetAndQueueIsPostponed() {
        this.initialTaskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        verify(this.initialTaskModel).setCallbackAfterSeconds(
                this.conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds());
        verify(this.queueDAO).postpone(
                this.initialTaskModel.getQueueName(),
                this.initialTaskModel.getTaskId(),
                anyInt(),
                this.conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds());
    }

    @Test
    public void testExecute_taskIsInProgress_taskIsCompleted_queueIsRemoved() {
        this.initialTaskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        this.initialTaskModel.setStatus(TaskModel.Status.COMPLETED);

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        verify(this.queueDAO).remove(this.initialTaskModel.getQueueName(), this.initialTaskModel.getTaskId());
        verifyNoMoreInteractions(this.systemTaskMock);
    }

    @Test
    public void testExecute_taskIsInProgress_workflowIsCompleted_workflowIsDecided() {
        this.initialTaskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        this.workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        this.executor.execute(this.systemTaskMock, this.initialTaskModel.getTaskId());

        verify(this.workflowExecutor).decide(this.workflowModel.getWorkflowInstanceId());
    }

    @Test
    public void testPostponeQuietly_queueIsPostponed() {
        this.executor.postponeQuietly("testQueueName", this.initialTaskModel);

        verify(this.queueDAO).postpone(
                "testQueueName",
                this.initialTaskModel.getTaskId(),
                anyInt(),
                this.queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskIsLoaded() {
        TaskModel loadedTaskModel = this.executor.loadTaskQuietly("testTaskId");

        assertEquals(this.initialTaskModel, loadedTaskModel);
    }
}