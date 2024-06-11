
package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private WorkflowSystemTask systemTask;
    
    @Mock
    private ConductorProperties conductorProperties;
    
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(30));
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(30));
    }
    
    @Test
    public void testExecuteTaskNull() {
        String taskId = "task1";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, never()).start(any(), any(), any());
    }

    @Test
    public void testExecuteTaskTerminalState() {
        String taskId = "task1";
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, never()).start(any(), any(), any());
    }

    @Test
    public void testExecuteTaskScheduledState() {
        String taskId = "task1";
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask).start(any(), any(), any());
        verify(systemTask, never()).execute(any(), any(), any());
    }

    @Test
    public void testExecuteTaskInProgressState() {
        String taskId = "task1";
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(workflowModel.getStatus()).thenReturn(Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, never()).start(any(), any(), any());
        verify(systemTask).execute(any(), any(), any());
    }
    
    
    @Test
    public void testPostponeQuietly() {
        String queueName = "queue1";
        TaskModel taskModel = mock(TaskModel.class);
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        verify(queueDAO, times(1)).postpone(queueName, taskModel.getTaskId(), taskModel.getWorkflowPriority(), conductorProperties.getTaskExecutionPostponeDuration().getSeconds());
    }
    
    @Test 
    public void testLoadTaskQuietly() {
        String taskId = "task1";
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        assertEquals(taskModel, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
}
