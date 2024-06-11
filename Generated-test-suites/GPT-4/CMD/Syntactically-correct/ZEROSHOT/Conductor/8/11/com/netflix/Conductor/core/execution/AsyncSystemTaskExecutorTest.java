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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
    private WorkflowSystemTask systemTask;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    private long systemTaskCallbackTime = 10L;
    private long queueTaskMessagePostponeSecs = 10L;

    private String taskId = "task_id";
    private String queueName = "queue_name";

    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(10L);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(10L);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);


        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                new WorkflowExecutor());


        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(QueueUtils.getQueueName(taskModel)).thenReturn(queueName);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertEquals(task, taskModel);
    }

    @Test
    public void testLoadTaskQuietlyException() {
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertNull(task);
    }

    // Other test methods for execute and postponeQuietly will go here

   // ...
  
}

