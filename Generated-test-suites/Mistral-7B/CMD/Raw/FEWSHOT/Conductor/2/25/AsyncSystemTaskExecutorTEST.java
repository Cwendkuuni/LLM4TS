package com.netflix.conductor.core.execution;

import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.queueDAO;
import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.executionDAOFacade;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOException;
import com.netflix.conductor.dao.TaskDAOException;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor systemTaskExecutor;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(executionDAOFacade.getTaskModel("taskId"))
                .thenReturn(mock(TaskModel.class));
        when(queueDAO.remove("queueName", "taskId"))
                .thenReturn(true);
        when(queueDAO.postpone("queueName", "taskId", 0, 0)).thenReturn(true);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(mock(WorkflowModel.class));
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        TaskModel loadedTask = systemTaskExecutor.loadTaskQuietly("taskId");
        assertThat(loadedTask, is(notNullValue()));
    }

    @Test
    public void testLoadTaskQuietlyWithNull() {
        when(executionDAOFacade.getTaskModel("taskId")).thenThrow(new TaskDAOException("Error"));
        TaskModel loadedTask = systemTaskExecutor.loadTaskQuietly("taskId");
        assertThat(loadedTask, is(nullValue()));
    }

    @Test
    public void testExecuteWithTaskCompletion() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(true);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessageRemoved("queueName", "taskId");
    }

    @Test
    public void testExecuteWithWorkflowCompletion() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessageRemoved("queueName", "taskId");
    }

    @Test(expected = Exception.class)
    public void testExecuteWithWorkflowFailure() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(workflowModel.getStatus()).thenThrow(new RuntimeException("Workflow failed"));

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessagePostponed("queueName", "taskId", queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteWithTaskPostponement() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessagePostponed("queueName", "taskId", queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteWithTaskPostponementAndCallback() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);

        systemTaskExecutor.execute(systemTask, "taskId");

        Optional<Long> callbackAfterSeconds = systemTask.getCallbackAfterSeconds(taskModel, systemTaskCallbackTime);
        long callbackAfterSecondsValue = callbackAfterSeconds.get();
        verify(queueDAO).postpone(
                "queueName",
                "taskId",
                0,
                callbackAfterSecondsValue);
    }

    @Test
    public void testExecuteWithTaskPostponementAndNullCallback() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);

        systemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).postpone(
                "queueName",
                "taskId",
                0,
                systemTaskCallbackTime);
    }

    @Test
    public void testExecuteWithTaskFailureAndNoConcurrentLimit() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(workflowModel.getStatus()).thenThrow(new RuntimeException("Workflow failed"));

        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(false);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessagePostponed("queueName", "taskId", queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteWithTaskFailureAndConcurrentLimit() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(workflowModel.getStatus()).thenThrow(new RuntimeException("Workflow failed"));

        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testExecuteWithTaskRateLimit() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(taskModel.getRateLimitPerFrequency()).thenReturn(1L);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                taskModel, metadataDAO.getTaskDef(taskModel.getTaskDefName())))
                .thenReturn(true);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testExecuteWithTaskPostponementAndWorkflowCompletion() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessageRemoved("queueName", "taskId");
    }

    @Test
    public void testExecuteWithTaskPostponementAndWorkflowTerminalState() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.FAILED);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessagePostponed("queueName", "taskId", queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteWithTaskPostponementAndWorkflowRunningState() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessagePostponed("queueName", "taskId", queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteWithTaskPostponementAndWorkflowScheduledState() throws Exception {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.SCHEDULED);

        systemTaskExecutor.execute(systemTask, "taskId");

        verifyQueueMessagePostponed("queueName", "taskId", queueTaskMessagePostponeSecs);
    }

    private void verifyQueueMessageRemoved(String queueName, String taskId) {
        try {
            queueDAO.remove(queueName, taskId);
        } catch (QueueDAOException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyQueueMessagePostponed(String queueName, String taskId, long delay) {
        try {
            queueDAO.postpone(queueName, taskId, 0, delay);
        } catch (QueueDAOException e) {
            throw new RuntimeException(e);
        }
    }
}