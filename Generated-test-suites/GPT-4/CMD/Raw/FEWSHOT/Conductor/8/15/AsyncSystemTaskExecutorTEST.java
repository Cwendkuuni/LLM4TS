
package com.netflix.conductor.core.execution;

import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
    
    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 	
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.of(1, ChronoUnit.SECONDS));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.of(1, ChronoUnit.SECONDS));
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class); 
        String taskId = "taskId";

        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(any(), any(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = mock(TaskModel.class);
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(any(), any(), anyInt(), anyLong());
    }
    
    @Test
    public void testLoadTaskQuietly() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(mock(TaskModel.class));
        asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}
