package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNotFoundInDB() {
        String taskId = "taskId1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "taskId1";
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(eq(workflowId), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(taskModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "taskId1";
        String workflowId = "workflowId1";
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(eq(workflowId), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(taskModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("queueName"), eq(taskId));
        verify(workflowExecutor, times(1)).decide(eq(workflowId));
    }

    @Test(expected = QueueDAOException.class)
    public void testExecute_queueDAOException() {
        String taskId = "taskId1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        doThrow(new QueueDAOException("QueueDAOException message")).when(queueDAO).remove(anyString(), anyString());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        String taskId = "taskId1";
        String taskType = "taskType1";
        TaskModel taskModel = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(eq("queueName"), eq(taskId), anyInt(), anyInt());
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        String taskId = "taskId1";
        String taskType = "taskType1";
        TaskModel taskModel = mock(TaskModel.class);
        int rateLimitPerFrequency = 5;

        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(taskModel);
        when(metadataDAO.getTaskDef(eq(taskType))).thenReturn(mock(TaskDefinition.class));
        when(taskModel.getRateLimitPerFrequency()).thenReturn(rateLimitPerFrequency);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(eq("queueName"), eq(taskId), anyInt(), anyInt());
    }
}