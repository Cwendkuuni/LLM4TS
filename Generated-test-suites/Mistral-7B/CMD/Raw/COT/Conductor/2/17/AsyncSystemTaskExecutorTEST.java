package com.netflix.conductor.core.execution.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueName;
import com.netflix.conductor.dao.TaskDAO.TaskStatus;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor systemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private Logger logger;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private Monitors monitors;

    private WorkflowSystemTask mockWorkflowSystemTask;
    private TaskModel mockTaskModel;
    private WorkflowModel mockWorkflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                () -> 100L);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                () -> 500L);
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        mockWorkflowSystemTask = mock(WorkflowSystemTask.class);
        mockTaskModel = mock(TaskModel.class);
        mockWorkflowModel = mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(mockWorkflowModel);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(mockTaskModel);
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(true);

        systemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor,
                logger,
                monitors);
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor, logger, monitors);
    }

    // Test methods for execute(WorkflowSystemTask, String) go here
    // Test methods for postponeQuietly(String, TaskModel) go here
    // Test methods for loadTaskQuietly(String) go here

}