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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

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
    private ConductorProperties conductorProperties;
    @Mock
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(60));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(60));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        // arrange
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        
        // action
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        //assert
        verify(executionDAOFacade).getTaskModel(taskId);
    }

    @Test
    public void testLoadTaskQuietly() {
        // arrange
        String taskId = "taskId";
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        // action
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        //assert
        assertEquals(taskModel, result);
    }

    @Test
    public void testPostponeQuietly() {
        // arrange
        String queueName = "queueName";
        TaskModel taskModel = mock(TaskModel.class);

        // action
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        //assert
        verify(queueDAO).postpone(queueName, taskModel.getTaskId(), taskModel.getWorkflowPriority(), conductorProperties.getTaskExecutionPostponeDuration().getSeconds());
    }

}