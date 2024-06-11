package com.netflix.conductor.core.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class AsyncSystemTaskExecutorTest {

    @Mock
    ExecutionDAOFacade executionDAOFacade;

    @Mock
    QueueDAO queueDAO;

    @Mock
    MetadataDAO metadataDAO;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    WorkflowSystemTask systemTask;

    @Mock
    TaskModel taskModel;

    @Mock
    WorkflowModel workflowModel;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(java.time.Duration.ofSeconds(1000));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(java.time.Duration.ofSeconds(1000));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void executeTestWithTaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(systemTask, "taskID");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void executeTestWithTerminalStatusTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "taskID");
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void executeTestWithScheduledStatusTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, "taskID");
        verify(queueDAO).postpone(anyString(), anyString(), any(), anyLong());
    }

    @Test
    public void executeTestWithInProgressStatusTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, "taskID");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).postpone(anyString(), anyString(), any(), anyLong());
        verify(executionDAOFacade).updateTask(any());
    }
}