package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.jdbc.MetadataDAOImpl;
import com.netflix.conductor.dao.metadata.jdbc.WorkflowModelJdbc;
import com.netflix.conductor.dao.metadata.jdbc.WorkflowModelJdbc.WorkflowModelRow;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskDef;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.utils.Json;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor systemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;

    @Before
    public void setup() {
        ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
        executionDAOFacadeMock = Mockito.mock(ExecutionDAOFacade.class);
        queueDAOMock = Mockito.mock(QueueDAO.class);
        metadataDAOMock = Mockito.mock(MetadataDAO.class);
        Monitors monitors = Mockito.mock(Monitors.class);

        WorkflowExecutor workflowExecutor = new WorkflowExecutor();
        when(queueDAOMock.getWorkflowPriority(anyString())).thenReturn(QueuePriority.HIGH);
        when(metadataDAOMock.getTaskDef(anyString())).thenReturn(getTaskDefMock());
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(getWorkflowModelMock());
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(any(TaskModel.class), any()))
                .thenReturn(false);
        when(executionDAOFacadeMock.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(getTaskModelMock());
        when(executionDAOFacadeMock.updateTask(any(TaskModel.class))).thenReturn(1);
        when(executionDAOFacadeMock.remove(anyString(), anyString())).thenReturn(1);
        when(queueDAOMock.remove(anyString(), anyString())).thenReturn(1);
        when(queueDAOMock.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(1);
        when(queueDAOMock.remove(anyString(), anyString())).thenReturn(1);
        when(queueDAOMock.size(anyString())).thenReturn(0);

        systemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_queueIsRemoved() {
        String taskId = "taskId1";
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowModelRow workflowModelRow = new WorkflowModelRow();
        workflowModelRow.setId(workflowId);
        workflowModelRow.setStatus(WorkflowStatus.COMPLETED.toString());
        workflowModel.setId(workflowId);
        workflowModel.setStatus(WorkflowStatus.COMPLETED);
        when(metadataDAOMock.getTaskDef(anyString())).thenReturn(getTaskDefMock());
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);

        systemTaskExecutor.execute(
                new WorkflowSystemTask() {}, taskId);

        verify(queueDAOMock).remove(QueueUtils.getQueueName(getTaskModelMock()), taskId);
    }

    @Test
    public void testExecute_taskIsInProgress_queueIsPostponed() {
        String taskId = "taskId1";
        String workflowId = "workflowId1";
        TaskModel taskModel = getTaskModelMock();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(metadataDAOMock.getTaskDef(anyString())).thenReturn(getTaskDefMock());
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(getWorkflowModelMock());
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModel);

        systemTaskExecutor.execute(
                new WorkflowSystemTask() {}, taskId);

        verify(queueDAOMock).postpone(
                QueueUtils.getQueueName(getTaskModelMock()),
                taskId,
                QueuePriority.HIGH.getValue(),
                TaskExecutor.queueTaskMessagePostponeSecs);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_taskIsNotFound_queueIsNotRemoved() {
        String taskId = "taskId1";

        systemTaskExecutor.execute(
                new WorkflowSystemTask() {}, taskId);

        verify(queueDAOMock, never()).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_taskIsNotTerminal() {
        String taskId = "taskId1";
        String workflowId = "workflowId1";
        TaskModel taskModel = getTaskModelMock();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowModelRow workflowModelRow = new WorkflowModelRow();
        workflowModelRow.setId(workflowId);
        workflowModel.setId(workflowId);
        workflowModel.setStatus(WorkflowStatus.RUNNING.toString());
        when(metadataDAOMock.getTaskDef(anyString())).thenReturn(getTaskDefMock());
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModel);

        systemTaskExecutor.execute(
                new WorkflowSystemTask() {}, taskId);

        verify(queueDAOMock, never()).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        String taskId = "taskId1";
        String workflowId = "workflowId1";
        TaskModel taskModel = getTaskModelMock();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowModelRow workflowModelRow = new WorkflowModelRow();
        workflowModelRow.setId(workflowId);
        workflowModel.setId(workflowId);
        workflowModel.setStatus(WorkflowStatus.RUNNING.toString());
        when(metadataDAOMock.getTaskDef(anyString())).thenReturn(getTaskDefMock());
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacadeMock.exceedsInProgressLimit(taskModel)).thenReturn(true);

        systemTaskExecutor.execute(
                new WorkflowSystemTask() {}, taskId);

        verify(queueDAOMock, never()).remove(anyString(), eq(taskId));
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        String taskId = "taskId1";
        String workflowId = "workflowId1";
        TaskModel taskModel = getTaskModelMock();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowModelRow workflowModelRow = new WorkflowModelRow();
        workflowModelRow.setId(workflowId);
        workflowModel.setId(workflowId);
        workflowModel.setStatus(WorkflowStatus.RUNNING.toString());
        when(metadataDAOMock.getTaskDef(anyString())).thenReturn(getTaskDefMock());
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacadeMock.exceedsRateLimitPerFrequency(taskModel, getTaskDefMock()))
                .thenReturn(true);

        systemTaskExecutor.execute(
                new WorkflowSystemTask() {}, taskId);

        verify(queueDAOMock, never()).remove(anyString(), eq(taskId));
        verify(queueDAOMock, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    private TaskModel getTaskModelMock() {
        return Mockito.mock(TaskModel.class);
    }

    private TaskDef getTaskDefMock() {
        return Mockito.mock(TaskDef.class);
    }

    private WorkflowModel getWorkflowModelMock() {
        return Mockito.mock(WorkflowModel.class);
    }

    private Optional<Long> getEvaluationOffset(TaskModel taskModel, long systemTaskCallbackTime) {
        return Optional.of(systemTaskCallbackTime);
    }
}