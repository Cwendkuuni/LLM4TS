import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueueStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(executionDAOFacade.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.RUNNING);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "taskId";
        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, never()).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }

    @Test
    public void testExecute_taskInQueue() {
        String taskId = "taskId";
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        doNothing().when(queueDAO).postpone(
                eq(QueueUtils.getQueueName(task)), eq(taskId), anyInt(), anyInt());
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(
                eq(QueueUtils.getQueueName(task)), eq(taskId), anyInt(), anyInt());
    }

    @Test
    public void testExecute_taskExecutionLimitExceeded() {
        String taskId = "taskId";
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(
                eq(QueueUtils.getQueueName(task)), eq(taskId), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_rateLimitPerFrequencyExceeded() {
        String taskId = "taskId";
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(
                eq(QueueUtils.getQueueName(task)), eq(taskId), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_taskExecutionSuccess() {
        String taskId = "taskId";
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.RUNNING);
        doNothing().when(systemTask).execute(workflow, task, workflowExecutor);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }

    @Test
    public void testExecute_taskExecutionFailed() {
        String taskId = "taskId";
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.RUNNING);
        doThrow(new RuntimeException("Exception")).when(systemTask).execute(workflow, task, workflowExecutor);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }

    @Test
    public void testExecute_workflowCompletion() {
        String taskId = "taskId";
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, never()).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testPostponeQuietly_taskNotFound() {
        String queueName = "queueName";
        String taskId = "taskId";
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, never()).postpone(eq(queueName), eq(taskId), anyInt(), anyInt());
    }

    @Test
    public void testPostponeQuietly_taskNotFoundInQueue() {
        String queueName = "queueName";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setQueueName(queueName);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO).postpone(eq(queueName), eq(taskId), anyInt(), anyInt());
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(task);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(task, returnedTask);
    }
}