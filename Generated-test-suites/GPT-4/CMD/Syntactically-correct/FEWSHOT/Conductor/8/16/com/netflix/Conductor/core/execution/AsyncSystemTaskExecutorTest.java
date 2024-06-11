
package com.netflix.conductor.core.execution;

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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.*;

import java.time.Duration;

@RunWith(MockitoJUnitRunner.class)
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
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
    }

    @Test
    public void testExecute(){
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    String taskId = "sometaskid";
    TaskModel task = mock(TaskModel.class);
        
    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
    asyncSystemTaskExecutor.execute(systemTask, taskId);
        
    verify(executionDAOFacade).getTaskModel(taskId);
    verify(task, times(2)).getTaskId();
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "taskid";
        TaskModel task = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        assertEquals(task, returnedTask);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = mock(TaskModel.class);

        when(task.getTaskId()).thenReturn("taskid");
        when(task.getWorkflowPriority()).thenReturn(2);
        
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 1);
    }
}
