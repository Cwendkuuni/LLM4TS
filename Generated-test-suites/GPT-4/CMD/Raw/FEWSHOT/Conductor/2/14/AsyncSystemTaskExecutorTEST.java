package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.mockito.Mockito.*;

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
    
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(20L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(20L));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskAlreadyTerminal() {
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskInProgressLimitExceeded() {
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getTaskType()).thenReturn("testTaskType");
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    // similar test cases can be written for other scenarios
}