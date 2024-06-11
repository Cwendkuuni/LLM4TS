package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO.QueueMessage;
import com.netflix.conductor.metrics.Monitors;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask mockWorkflowSystemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockWorkflowSystemTask = mock(WorkflowSystemTask.class);
    }

    @Test
    public void testExecute_taskNotFound_queueMessageRemoved() {
        String taskId = "taskId1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("queueName1"), eq(taskId));
    }

    @Test
    public void testExecute_taskExists_workflowNotTerminal_taskNotPostponed() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskExists_workflowTerminal_taskRemoved() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.COMPLETED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(eq(queueName), eq(taskId));
    }

    @Test
    public void testExecute_taskExists_taskPostponed() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(new WorkflowModel());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskExists_workflowNotTerminal_taskUpdated() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        WorkflowModel workflowModel = new WorkflowModel();
        when(executionDAOFacade.getWorkflowModel(eq(workflowModel.getId()), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(workflowExecutor, times(1)).decide(eq(workflowModel.getId()));
    }

    @Test
    public void testExecute_taskExists_workflowTerminal_taskUpdated() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(eq(workflowModel.getId()), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(workflowExecutor, times(1)).decide(eq(workflowModel.getId()));
    }

    @Test
    public void testExecute_taskNotExist_queueMessageNotRemoved() {
        String taskId = "taskId1";
        String queueName = "queueName1";

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, never()).remove(eq(queueName), eq(taskId));
    }

    @Test
    public void testExecute_queueSizeLimitExceeded() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_rateLimitExceeded() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(new WorkflowModel());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_workflowEvaluated() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(eq(workflowModel.getId()), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(workflowExecutor, times(1)).decide(eq(workflowModel.getId()));
    }

    @Test
    public void testPostpone_queueMessagePostponed() {
        String queueName = "queueName1";
        String taskId = "taskId1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setQueueName(queueName);
        QueueMessage queueMessage = new QueueMessage();
        queueMessage.setTaskId(taskId);
        queueMessage.setWorkflowPriority(0);
        queueMessage.setCallbackAfterSeconds(100L);

        when(queueDAO.getNextMessage(queueName)).thenReturn(Optional.of(queueMessage));

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), anyInt(), eq(100L));
    }

    @Test
    public void testPostponeQuietly_queueMessageNotFound() {
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId1");

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "taskId1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(taskModel);
    }

    @Test
    public void testLoadTaskQuietly_taskLoaded() {
        String taskId = "taskId1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskModel, loadedTaskModel);
    }

    @Test
    public void testExecute_taskExists_taskNotUpdated() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(eq(workflowModel.getId()), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(taskModel, never()).setStatus(any());
    }

    @Test
    public void testExecute_taskExists_workflowNotTerminal_callbackSet() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        Instant currentTime = Instant.now();
        long callbackAfterSeconds = 100L;

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(taskModel).setCallbackAfterSeconds(callbackAfterSeconds);
    }

    @Test
    public void testExecute_taskExists_workflowTerminal_workflowUpdated() {
        String taskId = "taskId1";
        String queueName = "queueName1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(eq(workflowModel.getId()), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(workflowModel).setStatus(ExecutionStatus.TERMINATED);
    }
}