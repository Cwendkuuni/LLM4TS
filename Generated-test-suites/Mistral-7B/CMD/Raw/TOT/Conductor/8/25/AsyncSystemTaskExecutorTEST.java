import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.test.ConductorTestBase;
import com.netflix.conductor.test.MockConductorProperties;
import com.netflix.conductor.test.TestUtils;
import com.netflix.conductor.utils.Json;
import com.netflix.conductor.utils.mapper.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest extends ConductorTestBase {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowModel workflowModel;
    private TaskModel taskModel;
    private String taskId = "taskId";
    private String workflowId = "workflowId";
    private WorkflowSystemTask workflowSystemTask;
    private OngoingStubbing<Monitors> monitorsStubbing;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = new MockConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(1000L);
        conductorProperties.setTaskExecutionPostponeDuration(1000L);
        workflowExecutor = mock(WorkflowExecutor.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(true);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "executionDAOFacade", executionDAOFacade);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "queueDAO", queueDAO);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "metadataDAO", metadataDAO);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "workflowExecutor", workflowExecutor);
        monitorsStubbing = mock(Monitors.class);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "monitors", monitorsStubbing);
        workflowModel = createWorkflowModel();
        taskModel = createTaskModel();
        workflowSystemTask = new WorkflowSystemTask() {};
        ReflectionTestUtils.setFieldValue(workflowSystemTask, "logger", logger);
    }

    @Test
    public void testExecute_TaskIsAlreadyCompleted_QueueIsRemoved() {
        given(executionDAOFacade.getWorkflowModel(workflowId, false)).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);
        given(queueDAO.get(QueueUtils.getQueueName(taskModel))).willReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName(taskModel), taskId);
    }

    @Test
    public void testExecute_TaskIsInProgress_TaskIsUpdatedAndCallbackIsPostponed() {
        given(executionDAOFacade.getWorkflowModel(workflowId, false)).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);
        given(queueDAO.get(QueueUtils.getQueueName(taskModel))).willReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(
                QueueUtils.getQueueName(taskModel),
                taskId,
                taskModel.getWorkflowPriority(),
                eq(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds()));
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_TaskIsTerminal_TaskIsRemovedFromQueue() {
        given(executionDAOFacade.getWorkflowModel(workflowId, false)).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);
        given(queueDAO.get(QueueUtils.getQueueName(taskModel))).willReturn(taskModel);

        taskModel.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName(taskModel), taskId);
    }

    @Test
    public void testExecute_TaskIsScheduled_TaskIsPostponed() {
        given(executionDAOFacade.getWorkflowModel(workflowId, false)).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);
        given(queueDAO.get(QueueUtils.getQueueName(taskModel))).willReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(
                QueueUtils.getQueueName(taskModel),
                taskId,
                taskModel.getWorkflowPriority(),
                eq(conductorProperties.getTaskExecutionPostponeDuration().getSeconds()));
    }

    @Test
    public void testExecute_TaskIsInErrorState_TaskIsUpdatedAndCallbackIsPostponed() {
        given(executionDAOFacade.getWorkflowModel(workflowId, false)).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);
        given(queueDAO.get(QueueUtils.getQueueName(taskModel))).willReturn(taskModel);

        taskModel.setStatus(TaskModel.Status.FAILED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(
                QueueUtils.getQueueName(taskModel),
                taskId,
                taskModel.getWorkflowPriority(),
                eq(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds()));
    }

    @Test
    public void testExecute_TaskIsRateLimited_TaskIsPostponed() {
        given(executionDAOFacade.getWorkflowModel(workflowId, false)).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);
        given(queueDAO.get(QueueUtils.getQueueName(taskModel))).willReturn(taskModel);
        given(metadataDAO.getTaskDef(taskModel.getTaskDefName())).willReturn(createTaskDefinition());

        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(
                QueueUtils.getQueueName(taskModel),
                taskId,
                taskModel.getWorkflowPriority(),
                eq(conductorProperties.getTaskExecutionPostponeDuration().getSeconds()));
    }

    @Test
    public void testExecute_TaskIsConcurrentlyExecutionLimited_TaskIsPostponed() {
        given(executionDAOFacade.getWorkflowModel(workflowId, false)).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);
        given(queueDAO.get(QueueUtils.getQueueName(taskModel))).willReturn(taskModel);

        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(
                QueueUtils.getQueueName(taskModel),
                taskId,
                taskModel.getWorkflowPriority(),
                eq(conductorProperties.getTaskExecutionPostponeDuration().getSeconds()));
    }

    @Test
    public void testPostponeQuietly_TaskIsPostponed() {
        String queueName = "queueName";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO).postpone(
                queueName,
                taskId,
                taskModel.getWorkflowPriority(),
                eq(conductorProperties.getTaskExecutionPostponeDuration().getSeconds()));
    }

    @Test
    public void testLoadTaskQuietly_TaskIsLoaded() {
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);

        TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        assertEquals(taskModel, loadedTaskModel);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        ReflectionTestUtils.setFieldValue(workflowInstance, "workflowId", workflowId);
        ReflectionTestUtils.setFieldValue(workflowInstance, "name", "workflowName");
        ReflectionTestUtils.setFieldValue(workflowInstance, "status", WorkflowInstance.Status.RUNNING);
        ReflectionTestUtils.setFieldValue(workflowInstance, "startedAt", 0L);
        ReflectionTestUtils.setFieldValue(workflowInstance, "completedAt", 0L);
        ReflectionTestUtils.setFieldValue(workflowInstance, "version", 1L);
        WorkflowModel workflowModel = new WorkflowModel();
        ReflectionTestUtils.setFieldValue(workflowModel, "workflowDefName", "workflowDefName");
        ReflectionTestUtils.setFieldValue(workflowModel, "workflowInstance", workflowInstance);
        return workflowModel;
    }

    private TaskModel createTaskModel() {
        TaskModel taskModel = new TaskModel();
        ReflectionTestUtils.setFieldValue(taskModel, "taskId", taskId);
        ReflectionTestUtils.setFieldValue(taskModel, "workflowInstanceId", workflowId);
        ReflectionTestUtils.setFieldValue(taskModel, "taskDefName", "taskDefName");
        ReflectionTestUtils.setFieldValue(taskModel, "status", TaskModel.Status.SCHEDULED);
        ReflectionTestUtils.setFieldValue(taskModel, "workflowPriority", 1);
        return taskModel;
    }

    private TaskDefinition createTaskDefinition() {
        return TestUtils.createTaskDefinition("taskDefName", null);
    }
}