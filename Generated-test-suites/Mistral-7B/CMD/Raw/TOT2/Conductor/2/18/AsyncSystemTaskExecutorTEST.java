package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.workflow.WorkflowInstance;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor systemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private WorkflowSystemTask systemTaskMock;
    private TaskModel taskMock;
    private WorkflowModel workflowModelMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;

    @Before
    public void setup() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        systemTaskMock = mock(WorkflowSystemTask.class);
        taskMock = mock(TaskModel.class);
        workflowModelMock = mock(WorkflowModel.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);

        when(executionDAOFacadeMock.getTaskModel(Mockito.anyString())).thenReturn(taskMock);
        when(executionDAOFacadeMock.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(workflowModelMock);
        when(metadataDAOMock.getTaskDef(Mockito.anyString())).thenReturn(null);

        when(workflowModelMock.getStatus()).thenReturn(WorkflowInstance.Status.RUNNING);
        when(workflowModelMock.getId()).thenReturn("workflowId");

        when(systemTaskMock.getTaskType()).thenReturn("taskType");
        when(systemTaskMock.isAsyncComplete(Mockito.any(TaskModel.class))).thenReturn(false);

        systemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
    }

    @Test
    public void testExecute_taskAlreadyCompleted_workflowTaskCompleted() {
        TaskModel task = Mockito.spy(taskMock);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        systemTaskExecutor.execute(systemTaskMock, task.getId());

        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        Mockito.verify(queueDAOMock).remove(QueueUtils.getQueueName(task), task.getId());
        Mockito.verify(executionDAOFacadeMock).updateTask(taskArgumentCaptor.capture());
        TaskModel capturedTask = taskArgumentCaptor.getValue();
        assertEquals(TaskModel.Status.CANCELED, capturedTask.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_taskIsTerminal() {
        TaskModel task = Mockito.spy(taskMock);
        when(task.getStatus()).thenReturn(TaskModel.Status.FAILED);

        systemTaskExecutor.execute(systemTaskMock, task.getId());

        Mockito.verify(queueDAOMock, Mockito.never()).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecute_taskInProgress() {
        systemTaskExecutor.execute(systemTaskMock, "taskId");

        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        Mockito.verify(queueDAOMock).postpone(
                QueueUtils.getQueueName(taskMock),
                taskMock.getId(),
                taskMock.getWorkflowPriority(),
                systemTaskExecutor.getSystemTaskCallbackTime());
    }

    @Test
    public void testExecute_taskScheduled() {
        TaskModel task = Mockito.spy(taskMock);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        systemTaskExecutor.execute(systemTaskMock, task.getId());

        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        Mockito.verify(queueDAOMock).postpone(
                QueueUtils.getQueueName(task),
                task.getId(),
                task.getWorkflowPriority(),
                systemTaskExecutor.getQueueTaskMessagePostponeSecs());
    }

    @Test
    public void testExecute_workflowCompleted() {
        when(workflowModelMock.getStatus())
                .thenReturn(WorkflowInstance.Status.COMPLETED);

        systemTaskExecutor.execute(systemTaskMock, "taskId");

        ArgumentCaptor<String> workflowIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(workflowExecutorMock).decide(workflowIdArgumentCaptor.capture());
        assertEquals("workflowId", workflowIdArgumentCaptor.getValue());
    }

    @Test
    public void testPostponeQuietly_taskExists() {
        systemTaskExecutor.postponeQuietly("queueName", taskMock);

        Mockito.verify(queueDAOMock).postpone(
                "queueName",
                taskMock.getId(),
                taskMock.getWorkflowPriority(),
                queueTaskMessagePostponeSecs());
    }

    @Test(expected = RuntimeException.class)
    public void testPostponeQuietly_taskNotFound() {
        systemTaskExecutor.postponeQuietly("queueName", taskMock);

        Mockito.verify(queueDAOMock, Mockito.never()).postpone(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyInt());
    }

    @Test
    public void testLoadTaskQuietly_taskExists() {
        TaskModel task = Mockito.spy(taskMock);

        TaskModel loadedTask = systemTaskExecutor.loadTaskQuietly("taskId");

        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        TaskModel task = Mockito.spy(taskMock);

        TaskModel loadedTask = systemTaskExecutor.loadTaskQuietly("taskId");

        assertEquals(null, loadedTask);
    }

    private Instant queueTaskMessagePostponeSecs() {
        return Instant.ofEpochSecond(100);
    }

    private Instant systemTaskCallbackTime() {
        return Instant.ofEpochSecond(200);
    }
}