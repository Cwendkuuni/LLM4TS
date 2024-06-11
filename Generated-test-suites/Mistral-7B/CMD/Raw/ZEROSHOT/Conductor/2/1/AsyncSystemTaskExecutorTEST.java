import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
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
    private WorkflowModel workflowModel;

    private Long queueTaskMessagePostponeSecs;
    private Long systemTaskCallbackTime;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QueueUtils queueUtils = new QueueUtils();
        when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn(queueUtils.getQueueName(task));
        queueTaskMessagePostponeSecs = 1L;
        systemTaskCallbackTime = 2L;
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(queueTaskMessagePostponeSecs);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Optional.of(systemTaskCallbackTime));
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowExecutor.decide(anyString())).thenReturn(Void.TYPE);
    }

    @After
    public void tearDown() {
        // Clear mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteWithNullTaskId() {
        asyncSystemTaskExecutor.execute(systemTask, null);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithCompletedTask() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(anyString(), task.getTaskId());
    }

    @Test
    public void testExecuteWithTerminalTask() {
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(anyString(), task.getTaskId());
    }

    @Test
    public void testExecuteWithScheduledTaskAndRateLimitExceeded() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getRateLimitPerFrequency()).thenReturn(1L);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(anyString(), task.getTaskId(), workflowModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteWithScheduledTaskAndConcurrentExecutionLimitExceeded() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(anyString(), task.getTaskId(), workflowModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteWithInProgressTask() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(systemTask).execute(workflowModel, task, workflowExecutor);
        verify(queueDAO).postpone(anyString(), task.getTaskId(), workflowModel.getWorkflowPriority(), systemTaskCallbackTime);
    }

    @Test
    public void testExecuteWithAsyncCompleteTask() {
        when(task.getStatus()).thenReturn(TaskModel.Status.ASYNC_COMPLETE);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(anyString(), task.getTaskId());
    }

    @Test
    public void testExecuteWithWorkflowTerminalState() {
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(anyString(), task.getTaskId());
    }
}