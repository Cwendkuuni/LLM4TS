package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.QueueName;
import com.netflix.conductor.dao.QueuePriority;
import com.netflix.conductor.dao.QueueStatus;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.utils.Json;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor systemUnderTest;
    private ExecutionDAOFacade executionDAO;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO mockQueueDAO;
    private ExecutionDAOFacade mockExecutionDAO;
    private MetadataDAO mockMetadataDAO;
    private Monitors mockMonitors;
    private WorkflowSystemTask mockSystemTask;

    @Before
    public void setup() {
        executionDAO = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockExecutionDAO = mock(ExecutionDAOFacade.class);
        mockMetadataDAO = mock(MetadataDAO.class);
        mockMonitors = mock(Monitors.class);

        systemUnderTest = new AsyncSystemTaskExecutor(
                executionDAO,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);

        mockSystemTask = mock(WorkflowSystemTask.class);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(Duration.ofSeconds(5));

        when(metadataDAO.getTaskDef(Mockito.anyString()))
                .thenReturn(mock(WorkflowModel.TaskDefinition.class));

        when(executionDAO.getTaskModel(Mockito.anyString()))
                .thenReturn(createTaskModel("taskId"));

        when(queueDAO.getQueue(Mockito.anyString()))
                .thenReturn(createQueue());

        when(queueDAO.getQueue(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(createQueue()));

        when(queueDAO.postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong()))
                .thenReturn(true);

        when(queueDAO.remove(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        when(executionDAO.updateTask(Mockito.any(TaskModel.class)))
                .thenReturn(new TaskModel());

        when(executionDAO.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(createWorkflowModel());

        when(metadataDAO.getTaskDef(Mockito.anyString()))
                .thenReturn(createTaskDef());

        when(workflowExecutor.decide(Mockito.anyString()))
                .thenReturn(true);

        when(workflowExecutor.getWorkflowInstance(Mockito.anyString()))
                .thenReturn(createWorkflowInstance());

        when(mockMonitors.recordQueueWaitTime(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(true);
    }

    @Test
    public void testExecute_withScheduledTask_whenTaskExecutionLimitIsExceeded_thenTaskIsPostponed() {
        Queue queue = createQueue();
        queue.setSize(10);

        TaskModel task = createTaskModel("taskId");

        when(executionDAO.exceedsInProgressLimit(task))
                .thenReturn(true);

        systemUnderTest.execute(mockSystemTask, task.getId());

        ArgumentCaptor<Queue> queueArg = ArgumentCaptor.forClass(Queue.class);
        verify(queueDAO).postpone(queueArg.capture(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());
        Queue postponedQueue = queueArg.getValue();
        assertEquals(QueueStatus.IN_PROGRESS, postponedQueue.getStatus());
        assertEquals(1, postponedQueue.getSize());
    }

    @Test
    public void testExecute_withScheduledTask_whenRateLimitIsExceeded_thenTaskIsPostponed() {
        Queue queue = createQueue();
        queue.setSize(10);

        TaskModel task = createTaskModel("taskId");
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);

        when(executionDAO.exceedsRateLimitPerFrequency(task, Mockito.any(WorkflowModel.TaskDefinition.class)))
                .thenReturn(true);

        systemUnderTest.execute(mockSystemTask, task.getId());

        ArgumentCaptor<Queue> queueArg = ArgumentCaptor.forClass(Queue.class);
        verify(queueDAO).postpone(queueArg.capture(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());
        Queue postponedQueue = queueArg.getValue();
        assertEquals(QueueStatus.IN_PROGRESS, postponedQueue.getStatus());
        assertEquals(1, postponedQueue.getSize());
    }

    @Test
    public void testExecute_withRunningTask_whenExecutionComplete_thenTaskIsRemovedFromQueue() {
        Queue queue = createQueue();
        queue.setSize(1);

        TaskModel task = createTaskModel("taskId");
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = createWorkflowModel();

        when(workflowExecutor.getWorkflowInstance(workflow.getId()))
                .thenReturn(createWorkflowInstance());

        systemUnderTest.execute(mockSystemTask, task.getId());

        ArgumentCaptor<Queue> queueArg = ArgumentCaptor.forClass(Queue.class);
        verify(queueDAO).remove(queueArg.capture(), Mockito.anyString());
        Queue removedQueue = queueArg.getValue();
        assertEquals(QueueStatus.EMPTY, removedQueue.getStatus());
        assertEquals(0, removedQueue.getSize());
    }

    @Test
    public void testExecute_withRunningTask_whenTaskCompletionCallback_thenTaskIsPostponed() {
        Queue queue = createQueue();
        queue.setSize(1);

        TaskModel task = createTaskModel("taskId");
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setCallbackAfterSeconds(11);

        when(executionDAO.getTaskModel(task.getId()))
                .thenReturn(task);

        systemUnderTest.execute(mockSystemTask, task.getId());

        ArgumentCaptor<Queue> queueArg = ArgumentCaptor.forClass(Queue.class);
        verify(queueDAO).postpone(queueArg.capture(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());
        Queue postponedQueue = queueArg.getValue();
        assertEquals(QueueStatus.IN_PROGRESS, postponedQueue.getStatus());
        assertEquals(1, postponedQueue.getSize());
    }

    @Test
    public void testExecute_withRunningTask_whenTaskCompletion_thenWorkflowIsEvaluated() {
        Queue queue = createQueue();
        queue.setSize(1);

        TaskModel task = createTaskModel("taskId");
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setEndTime(Instant.now().minus(Duration.ofSeconds(10))
                .atOffset(ZoneOffset.UTC));

        WorkflowModel workflow = createWorkflowModel();

        when(workflowExecutor.getWorkflowInstance(workflow.getId()))
                .thenReturn(createWorkflowInstance());

        systemUnderTest.execute(mockSystemTask, task.getId());

        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test
    public void testExecute_withCompletedTask_whenTaskIsInSCHEDULEDState_thenTaskIsRemovedFromQueue() {
        Queue queue = createQueue();
        queue.setSize(1);

        TaskModel task = createTaskModel("taskId");
        task.setStatus(TaskModel.Status.SCHEDULED);

        systemUnderTest.execute(mockSystemTask, task.getId());

        ArgumentCaptor<Queue> queueArg = ArgumentCaptor.forClass(Queue.class);
        verify(queueDAO).remove(queueArg.capture(), Mockito.anyString());
        Queue removedQueue = queueArg.getValue();
        assertEquals(QueueStatus.EMPTY, removedQueue.getStatus());
        assertEquals(0, removedQueue.getSize());
    }

    private TaskModel createTaskModel(String id) {
        TaskModel task = new TaskModel();
        task.setId(id);
        task.setTaskType("testTaskType");
        task.setTaskDefName("testTaskDef");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setStartTime(System.currentTimeMillis());
        task.setWorkflowInstanceId(null);
        task.setCallbackAfterSeconds(0);
        return task;
    }

    private Queue createQueue() {
        return new Queue(QueueName.create("testQueueName"), QueuePriority.HIGH, QueueStatus.IN_PROGRESS, 1);
    }

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel()
                .setId("testWorkflowId")
                .setName("testWorkflowName")
                .setDefinition("testWorkflowDefinition")
                .setStatus(WorkflowInstance.Status.IN_PROGRESS);
    }

    private WorkflowModel.TaskDefinition createTaskDef() {
        return new WorkflowModel.TaskDefinition()
                .setId("testTaskDefId")
                .setName("testTaskDefName")
                .setType("testTaskType");
    }

    private WorkflowInstance createWorkflowInstance() {
        return new WorkflowInstance()
                .setId("testWorkflowInstanceId")
                .setDefinition(Json.toJsonString(createWorkflowModel()));
    }
}