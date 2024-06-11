
package com.netflix.conductor.core.execution;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import java.util.Optional;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(java.time.Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(java.time.Duration.ofSeconds(10));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecuteSunnyCase() {
        String taskId = "testTask";
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getTaskId()).thenReturn(taskId);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, workflowExecutor);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskNotFoundException() {
        String taskId = "testTask";
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskExceedsInProgressLimit() {
        String taskId = "testTask";
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getTaskId()).thenReturn(taskId);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    } 
}
