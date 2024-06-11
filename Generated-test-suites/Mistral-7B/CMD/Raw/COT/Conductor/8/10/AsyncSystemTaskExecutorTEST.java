package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private WorkflowExecutor workflowExecutorMock;
    private ConductorProperties conductorPropertiesMock;

    @Before
    public void setup() {
        executionDAOFacadeMock = Mockito.mock(ExecutionDAOFacade.class);
        queueDAOMock = Mockito.mock(QueueDAO.class);
        metadataDAOMock = Mockito.mock(MetadataDAO.class);
        workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);
        conductorPropertiesMock = Mockito.mock(ConductorProperties.class);

        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(getTaskModel());
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(getWorkflowModel());
        when(queueDAOMock.get(anyString(), anyString())).thenReturn(Optional.of(getTaskModel()));
        when(queueDAOMock.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(getTaskModel().getTaskId());
        when(queueDAOMock.remove(anyString(), anyString())).thenReturn(true);
        when(metadataDAOMock.getTaskDef(anyString())).thenReturn(getTaskDef());
        when(workflowExecutorMock.decide(anyString())).thenReturn(true);
        when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration()).thenReturn(Mockito.mock(Duration.class));
        when(conductorPropertiesMock.getTaskExecutionPostponeDuration()).thenReturn(Mockito.mock(Duration.class));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
    }

    @Test
    public void testExecute_nullTaskId() {
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, null);
        verify(executionDAOFacadeMock, never()).getTaskModel(null);
    }

    @Test
    public void testExecute_nonExistingTask() {
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, "nonExistingTaskId");
        verify(executionDAOFacadeMock, never()).getTaskModel("nonExistingTaskId");
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        TaskModel task = getTaskModel();
        TaskModel expectedTask = getTaskModel();
        expectedTask.setStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = getWorkflowModel();
        WorkflowModel expectedWorkflow = getWorkflowModel();
        expectedWorkflow.setStatus(WorkflowInstanceStatus.COMPLETED);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel("workflowId", false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock, never()).remove(anyString(), anyString());
        verify(executionDAOFacadeMock).updateTask(expectedTask);
        verify(workflowExecutorMock).decide("workflowId");
    }

    @Test
    public void testExecute_taskInProgress() {
        TaskModel task = getTaskModel();
        TaskModel expectedTask = getTaskModel();
        WorkflowModel workflow = getWorkflowModel();
        WorkflowModel expectedWorkflow = getWorkflowModel();
        expectedWorkflow.setStatus(WorkflowInstanceStatus.IN_PROGRESS);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel("workflowId", false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock).postpone(eq("queueName"), eq("taskId"), anyInt(), eq(queueTaskMessagePostponeSecs));
        verify(executionDAOFacadeMock).updateTask(expectedTask);
        verify(workflowExecutorMock).decide("workflowId");
    }

    @Test
    public void testExecute_taskScheduled() {
        TaskModel task = getTaskModel();
        TaskModel expectedTask = getTaskModel();
        WorkflowModel workflow = getWorkflowModel();
        WorkflowModel expectedWorkflow = getWorkflowModel();
        expectedWorkflow.setStatus(WorkflowInstanceStatus.SCHEDULED);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel("workflowId", false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock).postpone(eq("queueName"), eq("taskId"), anyInt(), eq(queueTaskMessagePostponeSecs));
        verify(executionDAOFacadeMock).updateTask(expectedTask);
        verify(workflowExecutorMock).decide("workflowId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecute_exceedsInProgressLimit() {
        TaskModel task = getTaskModel();
        WorkflowModel workflow = getWorkflowModel();
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel("workflowId", false)).thenReturn(workflow);
        when(executionDAOFacadeMock.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock).postpone(eq("queueName"), eq("taskId"), anyInt(), eq(queueTaskMessagePostponeSecs));
        verify(executionDAOFacadeMock, never()).updateTask(task);
        verify(workflowExecutorMock, never()).decide("workflowId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecute_exceedsRateLimitPerFrequency() {
        TaskModel task = getTaskModel();
        WorkflowModel workflow = getWorkflowModel();
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel("workflowId", false)).thenReturn(workflow);
        when(metadataDAOMock.getTaskDef(task.getTaskDefName())).thenReturn(getTaskDef());
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(task, getTaskDef())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock).postpone(eq("queueName"), eq("taskId"), anyInt(), eq(queueTaskMessagePostponeSecs));
        verify(executionDAOFacadeMock, never()).updateTask(task);
        verify(workflowExecutorMock, never()).decide("workflowId");
    }

    @Test
    public void testExecute_taskTerminal() {
        TaskModel task = getTaskModel();
        TaskModel expectedTask = getTaskModel();
        expectedTask.setStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = getWorkflowModel();
        WorkflowModel expectedWorkflow = getWorkflowModel();
        expectedWorkflow.setStatus(WorkflowInstanceStatus.COMPLETED);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel("workflowId", false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOMock).remove(eq("queueName"), eq("taskId"));
        verify(executionDAOFacadeMock).updateTask(expectedTask);
        verify(workflowExecutorMock).decide("workflowId");
    }

    @Test
    public void testExecute_taskError() {
        TaskModel task = getTaskModel();
        TaskModel expectedTask = getTaskModel();
        expectedTask.setStatus(TaskModel.Status.ERROR);
        WorkflowModel workflow = getWorkflowModel();
        WorkflowModel expectedWorkflow = getWorkflowModel();
        expectedWorkflow.setStatus(WorkflowInstanceStatus.ERROR);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);

        when(executionDAOFacadeMock.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacadeMock.getWorkflowModel("workflowId", false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock).remove(eq("queueName"), eq("taskId"));
        verify(executionDAOFacadeMock).updateTask(expectedTask);
        verify(workflowExecutorMock).decide("workflowId");
    }

    @Test
    public void testPostponeQuietly_nullQueueName() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                null,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
        TaskModel task = getTaskModel();

        asyncSystemTaskExecutor.postponeQuietly(null, task);

        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly_nullTask() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);

        asyncSystemTaskExecutor.postponeQuietly(QueueUtils.getQueueName(getTaskModel()), null);

        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    private TaskModel getTaskModel() {
        return new TaskModel() {{
            setTaskDefName("testTaskDefName");
            setTaskId("testTaskId");
            setWorkflowInstanceId("testWorkflowInstanceId");
            setStatus(TaskModel.Status.SCHEDULED);
            setStartTime(0L);
            setEndTime(0L);
        }};
    }

    private WorkflowModel getWorkflowModel() {
        return new WorkflowModel() {{
            setId("testWorkflowInstanceId");
            setName("testWorkflowName");
            setStatus(WorkflowStatus.NEW);
            setCreateTime(0L);
            setUpdateTime(0L);
        }};
    }

    private WorkflowModel.TaskDef getTaskDef() {
        return new WorkflowModel.TaskDef() {{
            setName("testTaskDefName");
            setId("testTaskDefId");
            setTaskType("testTaskType");
            setTaskClass("testTaskClass");
            setRateLimitPerFrequency(0);
            setTaskRetrievalRequired(false);
            setTaskExecutionPostponeDuration(new Duration(1000L));
        }};
    }
}