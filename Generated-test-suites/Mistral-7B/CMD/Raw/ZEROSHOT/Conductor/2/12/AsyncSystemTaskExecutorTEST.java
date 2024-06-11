import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.testutils.Extensions;
import com.netflix.conductor.testutils.TestWorkflowModelBuilder;
import com.netflix.conductor.testutils.TestWorkflowSystemTaskBuilder;
import com.netflix.conductor.utils.QueueUtilsImpl;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@Extensions(executors = Executors.newSingleThreadExecutor())
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
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
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private Monitors monitors;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(conductorProperties, "systemTaskWorkerCallbackDuration", TestExtensions.millis(100));
        ReflectionTestUtils.setFieldValue(
                conductorProperties, "taskExecutionPostponeDuration", TestExtensions.millis(500));
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "queueDAO", queueDAO);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "metadataDAO", metadataDAO);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "executionDAOFacade", executionDAOFacade);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "workflowExecutor", workflowExecutor);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "conductorProperties", conductorProperties);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModelImpl());
        when(workflowExecutor.decide(anyString())).thenReturn(Void.TYPE);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(mock(TaskDefinition.class));
        when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("testQueue");
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                queueDAO.postpone((String) args[0], (String) args[1], (int) args[2], (int) args[3]);
                return null;
            }
        });
    }

    @Test
    public void testExecuteNullTask() {
        String taskId = "nullTaskId";
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertNull(executionDAOFacade.getTaskModel(taskId));
        assertTrue(queueDAO.remove(QueueUtils.getQueueName(null), taskId));
    }

    @Test
    public void testExecuteTerminalTask() {
        String taskId = "terminalTaskId";
        TaskModel terminalTask = new TaskModel();
        terminalTask.setId(taskId);
        terminalTask.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(terminalTask);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertTrue(queueDAO.remove(QueueUtils.getQueueName(terminalTask), taskId));
        assertEquals(terminalTask.getStatus(), TaskModel.Status.COMPLETED);
    }

    @Test
    public void testExecuteScheduledTaskWithLimitExceeded() {
        String taskId = "scheduledTaskId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doAnswer(
                invocationOnMock -> {
                    Object[] args = invocationOnMock.getArguments();
                    executionDAOFacade.exceedsInProgressLimit((TaskModel) args[0]);
                    postponeQuietly((String) args[0], (TaskModel) args[0]);
                }).when(executionDAOFacade).exceedsInProgressLimit(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertTrue(queueDAO.getSize(QueueUtils.getQueueName(task)) > 0);
    }

    @Test
    public void testExecuteScheduledTaskWithRateLimitExceeded() {
        String taskId = "scheduledTaskId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doAnswer(
                invocationOnMock -> {
                    Object[] args = invocationOnMock.getArguments();
                    executionDAOFacade.exceedsRateLimitPerFrequency(
                            (TaskModel) args[0], any(TaskDefinition.class));
                    postponeQuietly((String) args[0], (TaskModel) args[0]);
                }).when(executionDAOFacade).exceedsRateLimitPerFrequency(task, any(TaskDefinition.class));
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertTrue(queueDAO.getSize(QueueUtils.getQueueName(task)) > 0);
    }

    @Test
    public void testExecuteTaskWithRunningWorkflow() {
        String taskId = "runningWorkflowTaskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setInstanceStatus(WorkflowInstanceStatus.RUNNING);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertFalse(queueDAO.remove(QueueUtils.getQueueName(task), taskId));
    }

    @Test
    public void testExecuteTaskWithRunningWorkflowAndTerminalStatus() {
        String taskId = "runningWorkflowTaskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setInstanceStatus(WorkflowInstanceStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertTrue(queueDAO.remove(QueueUtils.getQueueName(task), taskId));
    }

    @Test
    public void testExecuteTaskWithWorkflowCompletion() {
        String taskId = "workflowCompletionTaskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setInstanceStatus(WorkflowInstanceStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertEquals(ExecutionStatus.COMPLETED, task.getStatus());
    }

    @Test
    public void testExecuteTaskWithWorkflowError() {
        String taskId = "workflowErrorTaskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setInstanceStatus(WorkflowInstanceStatus.ERROR);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertEquals(ExecutionStatus.ERROR, task.getStatus());
    }

    @Test
    public void testExecuteTaskWithNoWorkflow() {
        String taskId = "noWorkflowTaskId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertFalse(queueDAO.remove(QueueUtils.getQueueName(task), taskId));
    }

    @Test
    public void testExecuteTaskWithAsyncComplete() throws InterruptedException, IOException {
        String taskId = "asyncCompleteTaskId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();
        WorkflowSystemTaskImpl systemTask = new WorkflowSystemTaskImpl();
        systemTask.setTaskRetrievalRequired(false);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(workflow);
        when(workflowSystemTask.isAsyncComplete(task)).thenReturn(true);
        when(workflowSystemTask.execute(any(), any(), any()))
                .thenReturn(Optional.of(new ExecutionResultImpl()));
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertFalse(queueDAO.getSize(QueueUtils.getQueueName(task)) > 0);
    }

    @Test
    public void testExecuteTaskWithNoAsyncComplete() throws InterruptedException, IOException {
        String taskId = "noAsyncCompleteTaskId";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();
        WorkflowSystemTaskImpl systemTask = new WorkflowSystemTaskImpl();
        systemTask.setTaskRetrievalRequired(false);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(workflow);
        when(workflowSystemTask.isAsyncComplete(task)).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertTrue(queueDAO.getSize(QueueUtils.getQueueName(task)) > 0);
    }

    @After
    public void tearDown() {
        queueDAO.shutdown();
    }

    private void postponeQuietly(String queueName, TaskModel task) {
        try {
            queueDAO.postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        } catch (Exception e) {
            LOGGER.error("Error postponing task: {} in queue: {}", task.getTaskId(), queueName);
        }
    }
}

class ExecutionResultImpl {
    @Override
    public String toString() {
        return "ExecutionResultImpl{}";
    }
}