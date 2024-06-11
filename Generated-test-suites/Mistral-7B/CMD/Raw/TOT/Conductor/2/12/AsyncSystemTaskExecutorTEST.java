package com.netflix.conductor.core.execution;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setFieldValue;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.daos.ExecutionDAOFacade;
import com.netflix.conductor.core.daos.QueueDAO;
import com.netflix.conductor.core.daos.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceMetaInfo;
import com.netflix.conductor.model.workflow.WorkflowDefinition;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private WorkflowSystemTask workflowSystemTaskMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

        executor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorProperties,
                workflowExecutor);

        setFieldValue(executor, "queueTaskMessagePostponeSecs", 5L);
        setFieldValue(executor, "systemTaskCallbackTime", 10L);

        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(null);
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(new WorkflowModel());
        when(executionDAOFacadeMock.getWorkflowDefinition(anyString()))
                .thenReturn(new WorkflowDefinition());
        when(executionDAOFacadeMock.getWorkflowInstanceById(anyString()))
                .thenReturn(new WorkflowInstanceMetaInfo());
        when(executionDAOFacadeMock.getWorkflowExecutionLogs(anyString(), anyString()))
                .thenReturn(Collections.emptyList());
        when(executionDAOFacadeMock.getWorkflowExecutionLogsCount(anyString(), anyString()))
                .thenReturn(0L);
        when(executionDAOFacadeMock.exceedsInProgressLimit(any(TaskModel.class)))
                .thenReturn(false);
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(any(TaskModel.class),
                any(TaskDefinition.class)))
                .thenReturn(false);
        when(executionDAOFacadeMock.updateTask(any(TaskModel.class)))
                .thenReturn(true);

        QueueDAO queueDAO = new QueueDAOImpl();
        when(queueDAOMock.remove(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(queueDAOMock.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(true);

        MetadataDAO metadataDAO = mock(MetadataDAO.class);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTaskImpl();
        ArgumentCaptor<WorkflowSystemTask> workflowSystemTaskArgumentCaptor =
                ArgumentCaptor.forClass(WorkflowSystemTask.class);
        workflowSystemTaskMock = mock(WorkflowSystemTask.class);
        when(workflowSystemTaskMock.getTaskType()).thenReturn("TestTaskType");
        when(workflowSystemTaskMock.isAsyncComplete(any(TaskModel.class)))
                .thenReturn(false);
        when(workflowSystemTaskMock.isTaskRetrievalRequired()).thenReturn(true);
        doNothing().when(workflowSystemTaskMock).start(
                any(WorkflowModel.class),
                any(TaskModel.class),
                any(WorkflowExecutor.class));
        doNothing().when(workflowSystemTaskMock).execute(
                any(WorkflowModel.class),
                any(TaskModel.class),
                any(WorkflowExecutor.class));

        setFieldValue(workflowSystemTask, "executionDAOFacade", executionDAOFacadeMock);
        setFieldValue(workflowSystemTask, "queueDAO", queueDAOMock);
        setFieldValue(workflowSystemTask, "metadataDAO", metadataDAOMock);
        setFieldValue(workflowSystemMock, "workflowExecutor", workflowExecutor);
        setFieldValue(executor, "workflowSystemTask", workflowSystemTaskMock);

        AutowiredAnnotatedFieldUtils.autowire(executor);
    }

    @Test
    public void testExecute_TaskNotFound_ShouldRemoveDeadTaskFromQueue() {
        String taskId = "TestTaskId";

        executor.execute(workflowSystemTaskMock, taskId);

        verify(queueDAOMock, times(1)).remove(eq("TestTaskQueue"), eq(taskId));
    }

    @Test
    public void testExecute_TaskInProgress_ShouldPostponeTaskInQueue() {
        String taskId = "TestTaskId";
        Instant currentTime = Instant.now();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setStartTime(currentTime.minusMillis(100));
        task.setQueueWaitTime(500);

        executor.execute(workflowSystemTaskMock, taskId);

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> workflowPriorityArgumentCaptor =
                ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> callbackAfterSecondsArgumentCaptor =
                ArgumentCaptor.forClass(Long.class);

        verify(queueDAOMock, times(1)).postpone(eq("TestTaskQueue"), eq(taskId), anyInt(),
                eq(callbackAfterSecondsArgumentCaptor.capture()));

        Long callbackAfterSeconds = callbackAfterSecondsArgumentCaptor.getValue();
        assertEquals(executor.getSystemTaskCallbackTime(), callbackAfterSeconds);
    }

    @Test
    public void testExecute_TaskCompletion_ShouldRemoveTaskFromQueue() {
        String taskId = "TestTaskId";
        String workflowId = "TestWorkflowId";
        Instant currentTime = Instant.now();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.CANCELED);
        task.setReasonForIncompletion("TestReasonForIncompletion");
        task.setEndTime(currentTime);
        task.setWorkflowInstanceId(workflowId);

        executor.execute(workflowSystemTaskMock, taskId);

        verify(queueDAOMock, times(1)).remove(eq("TestTaskQueue"), eq(taskId));
        verify(workflowExecutor, times(1)).decide(eq(workflowId));
    }
}