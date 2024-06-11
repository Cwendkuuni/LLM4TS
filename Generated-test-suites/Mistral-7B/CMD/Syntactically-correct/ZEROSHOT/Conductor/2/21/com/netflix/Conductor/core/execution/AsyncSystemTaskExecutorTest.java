import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.MetricsUtils;

import java.time.Duration;
import java.util.Optional;

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

    @InjectMocks
    private AsyncSystemTaskExecutor systemUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "taskId1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doThrow(new RuntimeException("Task not found")).when(executionDAOFacade).getTaskModel(taskId);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(queueDAO, never()).postpone(anyString(), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskScheduled() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskAsyncComplete() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskTerminal() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(ExecutionStatus.FAILED);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskRateLimitPerFrequencyExceeded() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(mock(TaskDefinition.class));
        when(task.getRateLimitPerFrequency()).thenReturn(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskInProgressLimitExceeded() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_workflowCompleted() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        systemUnderTest.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor).decide(eq(workflow.getId()));
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = mock(TaskModel.class);
        doNothing().when(queueDAO).postpone(queueName, task.getTaskId(), anyInt(), anyLong());

        systemUnderTest.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getTaskId(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "taskId1";
        TaskModel task = null;

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("Task not found"));

        TaskModel loadedTask = systemUnderTest.loadTaskQuietly(taskId);

        Assert.assertNull(loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "taskId1";
        TaskModel task = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = systemUnderTest.loadTaskQuietly(taskId);

        Assert.assertEquals(task, loadedTask);
    }

    public static class TaskDefinition {
        public int getRateLimitPerFrequency() {
            return 1;
        }
    }
}