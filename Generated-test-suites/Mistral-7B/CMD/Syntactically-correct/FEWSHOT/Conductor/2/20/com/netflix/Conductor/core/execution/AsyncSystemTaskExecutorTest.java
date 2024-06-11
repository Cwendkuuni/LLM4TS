package com.netflix.conductor.core.execution;

import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.LOGGER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOException;
import com.netflix.conductor.dao.TaskNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;

    @Before
    public void setup() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);

        executor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
    }

    @Test
    public void testExecute_TaskNotFound_RemovesDeadTaskFromQueue() {
        String taskId = "task-1";
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);

        TaskModel taskModelMock = mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenThrow(new TaskNotFoundException());

        executor.execute(systemTaskMock, taskId);

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(queueDAOMock).remove(queueNameArgumentCaptor.capture(), taskIdArgumentCaptor.capture());
        assertEquals("taskQueue-" + systemTaskMock.getTaskType(), queueNameArgumentCaptor.getValue());
        assertEquals(taskId, taskIdArgumentCaptor.getValue());
    }

    @Test
    public void testExecute_TaskComplete_UpdatesWorkflowAndRemovesTaskFromQueue() {
        String taskId = "task-1";
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        TaskModel taskModelMock = mock(TaskModel.class);
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(executionDAOFacadeMock.getWorkflowModel(Mockito.anyString(), Mockito.eq(false))).thenReturn(workflowModelMock);

        executor.execute(systemTaskMock, taskId);

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(queueDAOMock).remove(queueNameArgumentCaptor.capture(), taskIdArgumentCaptor.capture());
        assertEquals("taskQueue-" + systemTaskMock.getTaskType(), queueNameArgumentCaptor.getValue());
        assertEquals(taskId, taskIdArgumentCaptor.getValue());

        Mockito.verify(workflowExecutorMock).decide(Mockito.eq(workflowModelMock.getWorkflowId()));

        assertEquals(TaskModel.Status.CANCELED, taskModelMock.getStatus());
    }

    @Test
    public void testExecute_TaskNotInProgress_PostponesTaskInQueue() throws QueueDAOException {
        String taskId = "task-1";
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        TaskModel taskModelMock = mock(TaskModel.class);
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(executionDAOFacadeMock.getWorkflowModel(Mockito.anyString(), Mockito.eq(false))).thenReturn(workflowModelMock);
        when(queueDAOMock.get(Mockito.eq("taskQueue-" + systemTaskMock.getTaskType()), Mockito.eq(taskId)))
                .thenReturn(taskModelMock);

        executor.execute(systemTaskMock, taskId);

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> workflowPriorityArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> callbackAfterSecondsArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(queueDAOMock).postpone(
                queueNameArgumentCaptor.capture(),
                taskIdArgumentCaptor.capture(),
                workflowPriorityArgumentCaptor.capture(),
                callbackAfterSecondsArgumentCaptor.capture());
        assertEquals("taskQueue-" + systemTaskMock.getTaskType(), queueNameArgumentCaptor.getValue());
        assertEquals(taskId, taskIdArgumentCaptor.getValue());
        assertEquals(taskModelMock.getWorkflowPriority(), workflowPriorityArgumentCaptor.getValue());
        assertEquals(systemTaskCallbackTime, callbackAfterSecondsArgumentCaptor.getValue());
    }

    @Test
    public void testExecute_TaskInProgress_UpdatesTaskInQueueWithNewCallbackTime() throws QueueDAOException {
        String taskId = "task-1";
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        TaskModel taskModelMock = mock(TaskModel.class);
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(executionDAOFacadeMock.getWorkflowModel(Mockito.anyString(), Mockito.eq(false))).thenReturn(workflowModelMock);
        when(queueDAOMock.get(Mockito.eq("taskQueue-" + systemTaskMock.getTaskType()), Mockito.eq(taskId)))
                .thenReturn(taskModelMock);

        executor.execute(systemTaskMock, taskId);

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> workflowPriorityArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> callbackAfterSecondsArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(queueDAOMock).update(
                queueNameArgumentCaptor.capture(),
                taskIdArgumentCaptor.capture(),
                Mockito.eq(taskModelMock),
                callbackAfterSecondsArgumentCaptor.capture());
        assertEquals("taskQueue-" + systemTaskMock.getTaskType(), queueNameArgumentCaptor.getValue());
        assertEquals(taskId, taskIdArgumentCaptor.getValue());
        assertEquals(systemTaskCallbackTime, callbackAfterSecondsArgumentCaptor.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_TaskFailsDuringExecution() {
        String taskId = "task-1";
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        TaskModel taskModelMock = mock(TaskModel.class);
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(executionDAOFacadeMock.getWorkflowModel(Mockito.anyString(), Mockito.eq(false))).thenReturn(workflowModelMock);

        executor.execute(systemTaskMock, taskId);

        Mockito.verify(executionDAOFacadeMock).updateTask(taskModelMock);
        Mockito.verify(queueDAOMock).remove(Mockito.eq("taskQueue-" + systemTaskMock.getTaskType()), taskId);
    }

    @Test
    public void testPostponeQuietly_TaskExistsInQueue_PostponesTask() throws QueueDAOException {
        String queueName = "taskQueue";
        String taskId = "task-1";

        executor.postponeQuietly(queueName, taskModelMock);

        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> callbackAfterSecondsArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(queueDAOMock).postpone(
                queueName,
                taskIdArgumentCaptor.capture(),
                Mockito.eq(taskModelMock.getWorkflowPriority()),
                callbackAfterSecondsArgumentCaptor.capture());
        assertEquals(taskId, taskIdArgumentCaptor.getValue());
        assertEquals(queueTaskMessagePostponeSecs, callbackAfterSecondsArgumentCaptor.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testPostponeQuietly_NullTaskModel() {
        String queueName = "taskQueue";

        executor.postponeQuietly(queueName, null);
    }

    @Test
    public void testLoadTaskQuietly_TaskExists_ReturnsTaskModel() {
        String taskId = "task-1";
        TaskModel taskModelMock = mock(TaskModel.class);

        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);

        TaskModel taskModel = executor.loadTaskQuietly(taskId);

        assertEquals(taskModelMock, taskModel);
    }

    @Test
    public void testLoadTaskQuietly_TaskDoesNotExist_ReturnsNull() {
        String taskId = "task-1";

        TaskModel taskModel = executor.loadTaskQuietly(taskId);

        assertNull(taskModel);
    }
}