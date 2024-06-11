
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import static org.junit.Assert.*;

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
    WorkflowSystemTask systemTask;
    @Mock
    private TaskModel task;
    @Mock
    private WorkflowModel workflow;

    String taskId;
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private static final String QUEUE_NAME = "QueueName";

    @Before
    public void setup() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        taskId = "TaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getTaskId()).thenReturn(taskId);
        when(task.getTaskType()).thenReturn("TaskType");
        when(task.getWorkflowInstanceId()).thenReturn("WorkflowId");
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
    }

    @Test
    public void testExecuteWhenTaskIsNull() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO);
    }

    @Test
    public void testExecuteWhenTaskIsTerminal() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWhenInExceedsInProgressLimit() {
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecuteWhenInExceedsRateLimitPerFrequency() {
        when(task.getRateLimitPerFrequency()).thenReturn(10);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any(TaskDef.class))).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test(expected = Exception.class)
    public void testExecuteWhenExceptionIsThrown() {
        when(systemTask.execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class))).thenThrow(new Exception("Error"));
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecuteWhenTaskExecutionCompletes() {
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor).decide(anyString());
        verify(executionDAOFacade).updateTask(any(TaskModel.class));
    }
}
