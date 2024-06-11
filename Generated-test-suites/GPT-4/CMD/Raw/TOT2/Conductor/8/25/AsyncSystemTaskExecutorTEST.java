
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private TaskModel taskModel;
    @Mock
    private WorkflowModel workflowModel;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowSystemTask workflowSystemTask;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecuteMethodWhenTaskIsNotFound() {
        String taskId = "testTaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteMethodWhenWorkflowStatusIsTerminal() {
        String taskId = "testTaskId";
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        workflowSystemTask.execute(workflowModel, taskModel, workflowExecutor);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteMethodWhenTaskStatusIsTerminal() {
        String taskId = "testTaskId";
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        workflowSystemTask.execute(taskModel, taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteMethodHandleExecutionException() {
        String taskId = "testTaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }
}
