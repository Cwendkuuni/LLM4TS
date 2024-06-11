package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

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
import com.netflix.conductor.model.workflow.WorkflowInstanceStatus;

@Component
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
    private Monitors monitors;

    @Mock
    private QueueUtils queueUtils;

    private ExecutionDAOFacade executionDAOFacadeSpy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executionDAOFacadeSpy = mock(ExecutionDAOFacade.class);
        when(executionDAOFacade).thenReturn(executionDAOFacadeSpy);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Duration.ofSeconds(10));
        when(queueUtils.getQueueName(any(TaskModel.class))).thenReturn("testQueue");
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeSpy, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_existingTask_executesAndUpdatesStatus() {
        String taskId = "taskId1";
        TaskModel task = createTaskModel();
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(false);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyTaskExecution(task, WorkflowInstanceStatus.IN_PROGRESS);
    }

    @Test
    public void testExecute_existingTask_completedAsync_updatesStatus() {
        String taskId = "taskId1";
        TaskModel task = createTaskModel();
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyTaskExecution(task, WorkflowInstanceStatus.COMPLETED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecute_nonExistentTask_throwsException() {
        String taskId = "taskId1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_taskAlreadyCompleted_doesNotUpdateStatus() {
        String taskId = "taskId1";
        TaskModel task = createTaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(false);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoInteractions(executionDAOFacadeSpy);
    }

    @Test
    public void testExecute_taskTerminalState_skipsExecution() {
        String taskId = "taskId1";
        TaskModel task = createTaskModel();
        task.setStatus(TaskModel.Status.FAILED);
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoInteractions(systemTask);
    }

    @Test
    public void testExecute_taskRateLimitPerFrequency_skipsExecution() {
        String taskId = "taskId1";
        String taskDefName = "taskDefName";
        long rateLimitPerFrequency = 10;
        TaskModel task = createTaskModel();
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(taskDefName)).thenReturn(new TaskDefinition());
        when(executionDAOFacadeSpy.exceedsRateLimitPerFrequency(task, any(TaskDefinition.class)))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoInteractions(systemTask);
    }

    @Test
    public void testExecute_taskConcurrentLimit_skipsExecution() {
        String taskId = "taskId1";
        String taskDefName = "taskDefName";
        int concurrentLimit = 5;
        TaskModel task = createTaskModel();
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacadeSpy.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoInteractions(systemTask);
    }

    @Test
    public void testExecute_taskInQueue_postponesTask() {
        String taskId = "taskId1";
        TaskModel task = createTaskModel();
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(eq("testQueue"), eq(taskId), anyInt(), anyInt());
    }

    @Test
    public void testExecute_taskCompletion_removesTaskFromQueue() {
        String taskId = "taskId1";
        TaskModel task = createTaskModel();
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);

        when(executionDAOFacadeSpy.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.remove(eq("testQueue"), eq(taskId))).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(eq("testQueue"), eq(taskId));
    }

    @Test
    public void testExecute_workflowCompletion_decidesWorkflow() {
        String workflowId = "workflowId1";
        TaskModel task = createTaskModel();
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacadeSpy.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacadeSpy.getWorkflowModel(workflowId, anyBoolean())).thenReturn(workflowModel);
        when(workflowExecutor.decide(workflowId)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        verify(workflowExecutor).decide(workflowId);
    }

    private TaskModel createTaskModel() {
        return new TaskModel()
                .setTaskId("taskId1")
                .setTaskType("taskType1")
                .setTaskDefName("taskDefName")
                .setStatus(TaskModel.Status.SCHEDULED)
                .setStartTime(Instant.now().getEpochSecond())
                .setWorkflowInstanceId(workflowId)
                .setWorkflowPriority(1);
    }

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel()
                .setId(workflowId)
                .setName("workflowName")
                .setStatus(ExecutionStatus.RUNNING);
    }

    private void verifyTaskExecution(TaskModel task, WorkflowInstanceStatus expectedWorkflowInstanceStatus) {
        verify(executionDAOFacadeSpy).updateTask(any(TaskModel.class));
        verify(queueDAO).remove(eq("testQueue"), eq(task.getTaskId()));
        verify(workflowExecutor).decide(anyString());

        assertEquals(expectedWorkflowInstanceStatus, task.getWorkflowInstanceStatus());
        assertFalse(task.isStatusTerminal());
        assertEquals(expectedWorkflowInstanceStatus, task.getWorkflowInstanceStatus());
    }

}