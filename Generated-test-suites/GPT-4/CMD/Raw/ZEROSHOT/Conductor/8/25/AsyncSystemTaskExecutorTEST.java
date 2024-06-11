
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
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
    private WorkflowSystemTask systemTask;
    @Mock
    private TaskModel taskModel;
    @Mock
    private WorkflowModel workflowModel;

    private final String taskId = "123";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getWorkflowInstanceId()).thenReturn("workflow-id");
        when(executionDAOFacade.getWorkflowModel("workflow-id", false)).thenReturn(workflowModel);
    }

    @Test
    public void testExecute_WhenTaskIsNull() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(systemTask, never()).execute(any(), any(), any());
    }

    @Test
    public void testExecute_WhenTaskIsTerminal() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_WhenTaskIsScheduled_AndExceedsInProgressLimit() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_WhenTaskIsScheduled_AndDoesNotExeceedsInProgressLimit_ButExceedsRateLimit() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(eq(taskModel), any())).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_WhenTaskIsScheduled_AndDoesNotExeceedsInProgressLimit_AndDoesNotExceedsRateLimit() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(eq(taskModel), any())).thenReturn(false);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask).start(any(), any(), any());
    }

    @Test
    public void testExecute_WhenTaskIsInProgress() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask).execute(any(), any(), any());
    }
    
    @Test
    public void testExecute_WhenTaskIsPostponing() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_WhenTaskCompletesExecution() {
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void testLoadTaskQuietly_WhenException() {
        doThrow(new RuntimeException()).when(executionDAOFacade).getTaskModel(any());
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testPostponeQuietly_WhenException() {
        doThrow(new RuntimeException()).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly("queue", taskModel);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}
