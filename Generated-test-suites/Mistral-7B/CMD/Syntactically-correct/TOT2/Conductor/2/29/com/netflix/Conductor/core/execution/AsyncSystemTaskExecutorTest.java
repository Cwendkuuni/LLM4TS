import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.stubbing.OngoingStubbing;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    @Rule
    public MockitoJUnit mockitoRule = new MockitoJUnit();

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
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private Monitors monitors;

    private OngoingStubbing<Monitors> monitorsStub;

    @Before
    public void setup() {
        monitorsStub = mockitoRule.stub(Monitors.class);
        when(workflowExecutor.decide(any(String.class))).thenReturn(true);

        when(executionDAOFacade.getTaskModel(any(String.class))).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(any(String.class), anyBoolean())).thenReturn(workflowModel);
        when(queueDAO.remove(any(String.class), any(String.class))).thenAnswer(invocation -> {
            String queueName = invocation.getArgument(0);
            String taskId = invocation.getArgument(1);
            LOGGER.debug("{} removed from queue: {}", taskId, queueName);
        });
        when(queueDAO.postpone(any(String.class), any(String.class), anyInt(), anyInt())).thenAnswer(invocation -> {
            String queueName = invocation.getArgument(0);
            String taskId = invocation.getArgument(1);
            LOGGER.debug("{} postponed in queue: {}", taskId, queueName);
        });
        doAnswer(invocation -> {}).when(conductorProperties).getSystemTaskWorkerCallbackDuration();
        doAnswer(invocation -> {}).when(conductorProperties).getTaskExecutionPostponeDuration();
        when(metadataDAO.getTaskDef(any(String.class))).thenReturn(taskModel);

        asyncSystemTaskExecutor.executionDAOFacade = executionDAOFacade;
        asyncSystemTaskExecutor.queueDAO = queueDAO;
        asyncSystemTaskExecutor.metadataDAO = metadataDAO;
        asyncSystemTaskExecutor.workflowExecutor = workflowExecutor;
        asyncSystemTaskExecutor.conductorProperties = conductorProperties;
        asyncSystemTaskExecutor.monitors = monitorsStub.createMock();
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertNull(taskModel);
        assertTrue(queueDAO.remove(QueueUtils.getQueueName(taskModel), taskId).isPresent());
    }

    @Test
    public void testExecute_workflowCompleted() {
        when(taskModel.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        assertEquals(Status.COMPLETED, workflowModel.getStatus());
        assertFalse(queueDAO.remove(QueueUtils.getQueueName(taskModel), "taskId").isPresent());
        assertTrue(workflowExecutor.decide(workflowModel.getWorkflowInstanceId()));
    }

    @Test
    public void testExecute_workflowInProgress() {
        when(taskModel.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        when(workflowModel.getStatus()).thenReturn(Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        assertEquals(Status.IN_PROGRESS, workflowModel.getStatus());
        assertFalse(queueDAO.remove(QueueUtils.getQueueName(taskModel), "taskId").isPresent());
        assertTrue(workflowExecutor.decide(workflowModel.getWorkflowInstanceId()));

        when(workflowModel.getStatus()).thenReturn(Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        assertEquals(Status.COMPLETED, workflowModel.getStatus());
        assertTrue(queueDAO.remove(QueueUtils.getQueueName(taskModel), "taskId").isPresent());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_concurrentExecutionLimitExceeded() {
        when(taskModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        postponeQuietly(QueueUtils.getQueueName(taskModel), taskModel);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        // Exception should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_rateLimitExceeded() {
        when(taskModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(taskModel.getRateLimitPerFrequency()).thenReturn(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        postponeQuietly(QueueUtils.getQueueName(taskModel), taskModel);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        // Exception should be thrown
    }

    @Test
    public void testExecute_workflowInProgress_noAsyncComplete() {
        when(taskModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(workflowModel.getStatus()).thenReturn(Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
        assertEquals(systemTaskCallbackTime, taskModel.getCallbackAfterSeconds());
        assertTrue(queueDAO.postpone(
                QueueUtils.getQueueName(taskModel),
                "taskId",
                taskModel.getWorkflowPriority(),
                systemTaskCallbackTime).isPresent());
    }

    @Test
    public void testExecute_workflowInProgress_asyncComplete() {
        when(taskModel.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);
        when(workflowModel.getStatus()).thenReturn(Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
        assertSame(systemTask, taskModel.getSystemTask());
        assertFalse(queueDAO.remove(QueueUtils.getQueueName(taskModel), "taskId").isPresent());
    }

    @Test
    public void testExecute_workflowTerminal() {
        when(taskModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(workflowModel.getStatus()).thenReturn(Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        assertEquals(TaskModel.Status.CANCELED, taskModel.getStatus());
        assertEquals("Workflow is in COMPLETED state", taskModel.getReasonForIncompletion());
        assertTrue(queueDAO.remove(QueueUtils.getQueueName(taskModel), "taskId").isPresent());
        assertTrue(workflowExecutor.decide(workflowModel.getWorkflowInstanceId()));
    }

    private void postponeQuietly(String queueName, TaskModel taskModel) {
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
    }
}