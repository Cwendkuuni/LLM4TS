package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceModel;
import com.netflix.conductor.model.WorkflowModelImpl;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;

    @Captor
    private ArgumentCaptor<TaskModel> taskModelCaptor;

    @Before
    public void setup() {
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Instant.of(0, 100, 0, 0));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Instant.of(0, 10, 0, 0));

        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        executor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);

        MockitoAnnotations.initMocks(this);

        Monitors monitors = mock(Monitors.class);
        when(workflowExecutor.decide(anyString())).thenReturn(monitors);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        executor.execute(systemTask, taskId);

        // The task was not found in the execution DAO, it should be removed from the queue
        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_completedTask() {
        String workflowId = "123";
        String taskId = "456";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setStatus(WorkflowInstanceModel.Status.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setWorkflowInstanceId(workflowId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);

        executor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_inProgressTask() throws InterruptedException {
        String workflowId = "123";
        String taskId = "456";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setStatus(WorkflowInstanceModel.Status.IN_PROGRESS);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setWorkflowInstanceId(workflowId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);

        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        executor.execute(systemTask, taskId);

        verify(queueDAO).postpone(
                anyString(),
                eq(taskId),
                anyInt(),
                eq(queueTaskMessagePostponeSecs.toSeconds()));
        verify(workflowExecutor).decide(workflowId);
        verify(workflowModel).setStatus(WorkflowInstanceModel.Status.COMPLETED);
        verify(executionDAOFacade).updateTask(taskModelCaptor.capture());
        TaskModel updatedTask = taskModelCaptor.getValue();
        assertTrue(updatedTask.getStatus().isTerminal());
    }

    @Test
    public void testExecute_terminalTask() {
        String workflowId = "123";
        String taskId = "456";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setStatus(WorkflowInstanceModel.Status.FAILED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setWorkflowInstanceId(workflowId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);

        executor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_exceedInProgressLimit() {
        String workflowId = "123";
        String taskId = "456";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setWorkflowInstanceId(workflowId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setRateLimitPerFrequency(5);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(new Object());

        executor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(queueDAO).postpone(
                anyString(),
                eq(taskId),
                anyInt(),
                eq(queueTaskMessagePostponeSecs.toSeconds()));
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testExecute_exceedRateLimitPerFrequency() {
        String workflowId = "123";
        String taskId = "456";
        WorkflowModel workflowModel = new WorkflowModelImpl();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setWorkflowInstanceId(workflowId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(new Object());

        executor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(queueDAO).postpone(
                anyString(),
                eq(taskId),
                anyInt(),
                eq(queueTaskMessagePostponeSecs.toSeconds()));
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testPostponeQuietly_taskPostponed() {
        String queueName = "queueName";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        executor.postponeQuietly(queueName, taskModel);

        verify(queueDAO).postpone(queueName, taskModel.getTaskId(), anyInt(), eq(queueTaskMessagePostponeSecs.toSeconds()));
    }

    @Test
    public void testLoadTaskQuietly_taskLoaded() {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("taskType");
        taskModel.setWorkflowInstanceId("workflowId");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel loadedTaskModel = executor.loadTaskQuietly(taskId);

        assertSame(taskModel, loadedTaskModel);
    }

    @Test
    public void testLoadTaskQuietly_taskNull() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTaskModel = executor.loadTaskQuietly(taskId);

        assertNull(loadedTaskModel);
    }

    private Instant queueTaskMessagePostponeSecs = Instant.now().plus(10, ChronoUnit.SECONDS);
}