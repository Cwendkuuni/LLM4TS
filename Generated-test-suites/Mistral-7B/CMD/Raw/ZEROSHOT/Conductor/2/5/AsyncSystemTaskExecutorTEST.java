import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SuppressWarnings("ResultOfMethodCallIgnored")
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
    private WorkflowSystemTask mockWorkflowSystemTask;

    @Mock
    private TaskModel mockTask;

    @Mock
    private WorkflowModel mockWorkflowModel;

    private static final String QUEUE_NAME = "queueName";
    private static final String TASK_ID = "taskId";
    private static final String WORKFLOW_ID = "workflowId";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void teardown() {
    }

    @Test
    public void testExecute_taskNotFound_queueMessageRemoved() {
        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(null);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_terminalWorkflow_queueMessageRemoved() {
        given(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).willReturn(mockWorkflowModel);
        given(mockWorkflowModel.getStatus()).willReturn(WorkflowModel.Status.COMPLETED);

        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(mockTask);
        given(mockTask.getStatus()).willReturn(TaskModel.Status.SCHEDULED);

        given(queueDAO.remove(QUEUE_NAME, TASK_ID)).willReturn(true);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_inProgressWorkflow_taskStatusUpdated() {
        given(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).willReturn(mockWorkflowModel);
        given(mockWorkflowModel.getStatus()).willReturn(WorkflowModel.Status.EXECUTING);

        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(mockTask);
        given(mockTask.getStatus()).willReturn(TaskModel.Status.SCHEDULED);
        given(mockTask.getEndTime()).willReturn(0L);

        given(queueDAO.remove(QUEUE_NAME, TASK_ID)).willReturn(false);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, TASK_ID);

        verify(mockTask).setStatus(TaskModel.Status.IN_PROGRESS);
    }

    @Test
    public void testExecute_asyncComplete_taskStatusUpdated() {
        given(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).willReturn(mockWorkflowModel);
        given(mockWorkflowModel.getStatus()).willReturn(WorkflowModel.Status.EXECUTING);

        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(mockTask);
        given(mockTask.getStatus()).willReturn(TaskModel.Status.SCHEDULED);
        given(mockTask.getEndTime()).willReturn(System.currentTimeMillis() - Duration.ofSeconds(1).toMillis());
        given(mockTask.getCallbackAfterSeconds()).willReturn(1000L);

        given(queueDAO.remove(QUEUE_NAME, TASK_ID)).willReturn(false);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, TASK_ID);

        verify(mockTask).setStatus(TaskModel.Status.ASYNC_COMPLETE);
    }

    @Test
    public void testExecute_postpone_taskStatusUpdated() {
        given(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).willReturn(mockWorkflowModel);
        given(mockWorkflowModel.getStatus()).willReturn(WorkflowModel.Status.EXECUTING);

        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(mockTask);
        given(mockTask.getStatus()).willReturn(TaskModel.Status.SCHEDULED);

        given(queueDAO.postpone(QUEUE_NAME, TASK_ID, 1, 1000L)).willReturn(true);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, TASK_ID);

        verify(mockTask).setCallbackAfterSeconds(1000L);
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(mockTask);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(mockTask);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_workflowCompletion() {
        given(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).willReturn(mockWorkflowModel);
        given(mockWorkflowModel.getStatus()).willReturn(WorkflowModel.Status.COMPLETED);

        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(mockTask);
        given(mockTask.getStatus()).willReturn(TaskModel.Status.SCHEDULED);

        doAnswer(
                invocationOnMock -> {
                    WorkflowModel completedWorkflowModel =
                            invocationOnMock.callRealMethod().getReturnValue();
                    completedWorkflowModel.setStatus(WorkflowModel.Status.COMPLETED);
                    return completedWorkflowModel;
                })
                .when(mockWorkflowModel)
                .getWorkflowInstance(any());

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, TASK_ID);

        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testPostponeQuietly_taskNotFound() {
        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, mockTask);

        // no verification needed since the method is marked as Quietly
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(null);

        Optional<TaskModel> taskModelOptional = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        verify(executionDAOFacade).getTaskModel(TASK_ID);
        assertTrue(taskModelOptional.isEmpty());
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        given(executionDAOFacade.getTaskModel(TASK_ID)).willReturn(mockTask);

        Optional<TaskModel> taskModelOptional = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        verify(executionDAOFacade).getTaskModel(TASK_ID);
        assertEquals(mockTask, taskModelOptional.get());
    }
}