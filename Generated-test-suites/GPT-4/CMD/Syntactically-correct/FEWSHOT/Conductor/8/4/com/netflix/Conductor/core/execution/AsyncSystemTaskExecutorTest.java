package com.netflix.conductor.core.execution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    @Test
    public void testExecute() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskId("task_id");

        WorkflowModel workflowModel = new WorkflowModel();

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(java.time.Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(java.time.Duration.ofSeconds(5));

        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflowModel);

        AsyncSystemTaskExecutor systemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        systemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, times(1)).updateTask(any());
    }
}