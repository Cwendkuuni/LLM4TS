package com.netflix.conductor.core.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.Queue;
import com.netflix.conductor.dao.WorkflowExecutionStatus;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.metadata.TaskDefinition;
import com.netflix.conductor.model.workflow.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.utils.ClockUtils;

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
    private Logger logger;

    @Mock
    private TaskDefinition taskDef;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private Queue queue;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                TimeUnit.SECONDS.valueOf(1L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                TimeUnit.SECONDS.valueOf(5L));
        when(executionDAOFacade.getTaskModel(anyString())).thenAnswer(new Answer<TaskModel>() {
            @Override
            public TaskModel answer(InvocationOnMock invocation) throws Exception {
                String taskId = (String) invocation.getArguments()[0];
                return taskModel;
            }
        });
        when(queueDAO.get(anyString())).thenReturn(queue);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(taskDef);
        when(workflowExecutor.decide(anyString())).thenReturn(Void.TYPE);
        when(systemTask.isAsyncComplete(any())).thenReturn(true);

        ClockUtils.setCurrentTime(System.currentTimeMillis());
    }

    @After
    public void cleanup() {
        ClockUtils.reset();
    }

    @Test
    public void testExecuteTaskThatIsAlreadyCompleted() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName(taskModel);
        TaskModel task = loadTaskQuietly(taskId);

        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(task.getWorkflowInstanceId()).thenReturn("some-workflow-id");

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(queueDAO).remove(queueNameCaptor.capture(), taskIdCaptor.capture());
        Queue expectedQueue = queue;
        Queue actualQueue = queueNameCaptor.getValue();
        assertThat(expectedQueue, is(actualQueue));

        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide("some-workflow-id");
    }

    @Test
    public void testExecuteTaskThatIsTerminal() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName(taskModel);

        when(task.getStatus()).thenReturn(TaskModel.Status.FAILED);
        when(task.getWorkflowInstanceId()).thenReturn("some-workflow-id");

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(queueDAO).remove(queueNameCaptor.capture(), taskIdCaptor.capture());
        Queue expectedQueue = queue;
        Queue actualQueue = queueNameCaptor.getValue();
        assertThat(expectedQueue, is(actualQueue));

        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide("some-workflow-id");
    }

    @Test
    public void testExecuteTaskThatIsScheduledAndHasReachedInProgressLimit() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName(taskModel);

        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("some-workflow-id");
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(queueDAO).postpone(queueNameCaptor.capture(), taskIdCaptor.capture(), anyInt(), anyLong());
        Queue expectedQueue = queue;
        Queue actualQueue = queueNameCaptor.getValue();
        assertThat(expectedQueue, is(actualQueue));

        verify(executionDAOFacade, never()).updateTask(taskModel);
        verify(workflowExecutor, never()).decide("some-workflow-id");
    }

    @Test
    public void testExecuteTaskThatIsScheduledAndHasExceededRateLimitPerFrequency() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName(taskModel);

        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("some-workflow-id");
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, taskDef)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(queueDAO).postpone(queueNameCaptor.capture(), taskIdCaptor.capture(), anyInt(), anyLong());
        Queue expectedQueue = queue;
        Queue actualQueue = queueNameCaptor.getValue();
        assertThat(expectedQueue, is(actualQueue));

        verify(executionDAOFacade, never()).updateTask(taskModel);
        verify(workflowExecutor, never()).decide("some-workflow-id");
    }

    @Test
    public void testExecuteTaskThatIsInProgress() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName(taskModel);

        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(task.getWorkflowInstanceId()).thenReturn("some-workflow-id");

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(queueDAO).postpone(queueNameCaptor.capture(), taskIdCaptor.capture(), anyInt(), anyLong());
        Queue expectedQueue = queue;
        Queue actualQueue = queueNameCaptor.getValue();
        assertThat(expectedQueue, is(actualQueue));

        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide("some-workflow-id");
    }

    @Test
    public void testExecuteTaskThatIsInProgressAndHasCompletedAsync() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName(taskModel);

        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(task.getWorkflowInstanceId()).thenReturn("some-workflow-id");
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(queueDAO).remove(queueNameCaptor.capture(), taskIdCaptor.capture());
        Queue expectedQueue = queue;
        Queue actualQueue = queueNameCaptor.getValue();
        assertThat(expectedQueue, is(actualQueue));

        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide("some-workflow-id");
    }

    @Test
    public void testExecuteTaskThatIsTerminalAndHasCompletedAsync() {
        String taskId = "some-task-id";
        String queueName = QueueUtils.getQueueName(taskModel);

        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);
        when(task.getWorkflowInstanceId()).thenReturn("some-workflow-id");
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(queueDAO).remove(queueNameCaptor.capture(), taskIdCaptor.capture());
        Queue expectedQueue = queue;
        Queue actualQueue = queueNameCaptor.getValue();
        assertThat(expectedQueue, is(actualQueue));

        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide("some-workflow-id");
    }

    @Test
    public void testPostponeTaskThatIsAlreadyPostponed() {
        String queueName = QueueUtils.getQueueName(taskModel);
        String taskId = "some-task-id";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, never()).postpone(queueName, taskId, anyInt(), anyLong());
    }

    @Test
    public void testPostponeTaskThatIsNotInQueue() {
        String queueName = QueueUtils.getQueueName(taskModel);
        String taskId = "some-task-id";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, never()).get(queueName);
    }

    @Test
    public void testLoadTaskThatDoesNotExist() {
        String taskId = "some-task-id";

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(logger).error("TaskId: {} could not be found while executing {}", taskId, systemTask);
    }
}