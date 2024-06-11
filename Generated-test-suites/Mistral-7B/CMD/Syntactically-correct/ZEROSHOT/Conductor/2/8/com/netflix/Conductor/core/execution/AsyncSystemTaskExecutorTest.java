import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask systemTask;
    private String taskId;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        systemTask = mock(WorkflowSystemTask.class);
        taskId = "taskId";
    }

    @After
    public void tearDown() {
        // TODO: add tear down code if needed
    }

    @Test
    public void testExecute_taskNotFound() {
        doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskTerminalState() {
        TaskModel task = mock(TaskModel.class);
        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(true).when(task).isTerminal();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskInProgress() {
        TaskModel task = mock(TaskModel.class);
        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(false).when(task).isTerminal();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // TODO: verify systemTask callback and execution
        verify(queueDAO).postpone(anyString(), taskId, 0L, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskComplete() {
        TaskModel task = mock(TaskModel.class);
        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(true).when(task).isAsyncComplete(any(TaskModel.class));
        doReturn(true).when(task).isTerminal();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void testExecute_taskRateLimitExceeded() {
        TaskModel task = mock(TaskModel.class);
        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(1).when(task).getRateLimitPerFrequency();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskInProgressLimitExceeded() {
        TaskModel task = mock(TaskModel.class);
        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(false).when(task).isTerminal();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), taskId, 0L, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(task);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        TaskModel task = mock(TaskModel.class);
        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }
}