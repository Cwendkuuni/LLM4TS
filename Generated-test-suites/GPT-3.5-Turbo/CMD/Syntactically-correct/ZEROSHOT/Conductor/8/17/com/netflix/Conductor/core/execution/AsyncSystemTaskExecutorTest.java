import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
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
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowInstanceId("workflow123");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        doNothing().when(executionDAOFacade).updateTask(any(TaskModel.class));
        doNothing().when(workflowExecutor).decide(anyString());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), anyString());
        verify(executionDAOFacade).updateTask(any(TaskModel.class));
        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void testExecute_taskNotFound() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyZeroInteractions(queueDAO);
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel task = new TaskModel();
        task.setTaskId("task123");
        String queueName = "testQueue";

        doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 10L);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        TaskModel task = new TaskModel();
        task.setTaskId("task123");

        when(executionDAOFacade.getTaskModel("task123")).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("task123");

        assertNotNull(loadedTask);
        assertEquals("task123", loadedTask.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        when(executionDAOFacade.getTaskModel("task123")).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("task123");

        assertNull(loadedTask);
    }

}