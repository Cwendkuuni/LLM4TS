package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueueName;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        conductorProperties = mock(ConductorProperties.class);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1L));
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1L));

        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_taskDoesNotExist() {
        String taskId = "nonExistentTask";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(LOGGER, times(1)).error(
                "TaskId: {} could not be found while executing {}", taskId, systemTask);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testExecute_taskIsCompleted() {
        String taskId = "completedTask";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setWorkflowInstanceId("completedWorkflow");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(QueueName.fromString("completedWorkflow"), taskId);
        verify(workflowExecutor, times(1)).decide("completedWorkflow");
    }

    @Test
    public void testExecute_taskIsScheduled() {
        String taskId = "scheduledTask";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO, times(1)).postpone(
                QueueName.fromString("scheduledTaskQueue"),
                eq(taskId),
                anyInt(),
                eq(1L));
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskIsInProgress() {
        String taskId = "inProgressTask";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO, times(1)).execute(eq(taskId), any(Runnable.class));
        verify(workflowExecutor, times(1)).decide("workflowInstanceId");
    }

    @Test
    public void testExecute_taskIsTerminal() {
        String taskId = "terminalTask";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.TERMINAL);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(QueueName.fromString("terminalTaskQueue"), taskId);
        verify(workflowExecutor, times(1)).decide("workflowInstanceId");
    }

    @Test
    public void testExecute_taskHasRateLimit() {
        String taskId = "rateLimitedTask";
        TaskModel task = new TaskModel();
        task.setRateLimitPerFrequency(1);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());
        Monitors mockMonitors = mock(Monitors.class);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(mock(WorkflowModel.Definition.class));
        when(workflowExecutor.decide(anyString())).thenReturn(Optional.empty());

        AsyncSystemTaskExecutor testObject = spy(asyncSystemTaskExecutor);
        doCallRealMethod().when(testObject).execute(any(WorkflowSystemTask.class), eq(taskId));

        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(0L));
        testObject.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(QueueName.fromString("rateLimitedTaskQueue"), taskId);
        verify(workflowExecutor, times(1)).decide("workflowInstanceId");
        verify(monitors, times(1)).error(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecute_taskHasExceededMaxConcurrentLimit() {
        String taskId = "maxConcurrentLimitTask";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("taskDefName");
        task.setExceedsInProgressLimit(true);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(
                QueueName.fromString("maxConcurrentLimitTaskQueue"),
                eq(taskId),
                anyInt(),
                eq(1L));
    }

    @Test
    public void testPostponeQuietly_taskExists() {
        String queueName = "queueName";
        String taskId = "taskId";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(
                eq(queueName),
                eq(taskId),
                anyInt(),
                eq(1L));
    }

    @Test
    public void testPostponeQuietly_taskDoesNotExist() {
        String queueName = "queueName";
        String taskId = "taskId";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskId);

        verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testLoadTaskQuietly_taskExists() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskDoesNotExist() {
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
    }

    @Test
    public void testDecide_workflowIsTerminal() {
        String workflowId = "workflowId";

        when(workflowExecutor.decide(workflowId)).thenReturn(Optional.of(ExecutionStatus.TERMINATED));

        WorkflowInstanceStatus workflowInstanceStatus = asyncSystemTaskExecutor.decide(workflowId);

        assertEquals(ExecutionStatus.TERMINATED, workflowInstanceStatus);
    }

    @Test
    public void testDecide_workflowIsRunning() {
        String workflowId = "workflowId";

        when(workflowExecutor.decide(workflowId)).thenReturn(Optional.empty());

        WorkflowInstanceStatus workflowInstanceStatus = asyncSystemTaskExecutor.decide(workflowId);

        assertNull(workflowInstanceStatus);
    }
}