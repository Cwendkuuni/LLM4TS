package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private WorkflowSystemTask systemTask;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecute() {
        String taskId = "1";
        String taskName = "";

        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(java.time.Duration.ofSeconds(1));
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(java.time.Duration.ofSeconds(1));

        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getTaskId()).thenReturn(taskId);
        when(taskModel.getWorkflowPriority()).thenReturn(1);
        when(taskModel.getTaskDefName()).thenReturn(taskName);
        when(taskModel.getTaskType()).thenReturn(taskName);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(taskId, true)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskId);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "1";
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        assertEquals(taskModel, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
}