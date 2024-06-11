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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Duration;

@RunWith(MockitoJUnitRunner.class)
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

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1L));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        when(taskModel.getTaskId()).thenReturn("taskId");
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(),anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(executionDAOFacade, times(1)).getTaskModel("taskId");
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecute_nullTaskModel() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(executionDAOFacade, times(1)).getTaskModel("taskId");
        verify(workflowExecutor, times(0)).decide(anyString());
    }
}