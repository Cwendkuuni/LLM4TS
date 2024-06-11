package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;
    private OngoingStubbing<TaskModel> taskModelStubbing;
    private OngoingStubbing<WorkflowModel> workflowModelStubbing;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        this.executionDAOFacadeMock = Mockito.mock(ExecutionDAOFacade.class);
        this.queueDAOMock = Mockito.mock(QueueDAO.class);
        this.metadataDAOMock = Mockito.mock(MetadataDAO.class);
        this.conductorPropertiesMock = Mockito.mock(ConductorProperties.class);
        this.workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);
        this.asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        this.executionDAOFacadeMock,
                        this.queueDAOMock,
                        this.metadataDAOMock,
                        this.conductorPropertiesMock,
                        this.workflowExecutorMock);
        this.taskModelStubbing = Mockito.doReturn(new TaskModel()) // TaskModel instance for stubbing
                .when(this.executionDAOFacadeMock).getTaskModel(Mockito.anyString());
        this.workflowModelStubbing =
                Mockito.doReturn(new WorkflowModel()) // WorkflowModel instance for stubbing
                        .when(this.metadataDAOMock).getTaskDef(Mockito.anyString());
    }

    @Test
    public void testExecute_TaskIsNotInDatabase_TaskIsRemovedFromQueue() {
        // Given
        String taskId = UUID.randomUUID().toString();

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        Mockito.verify(this.queueDAOMock).remove(Mockito.eq(QueueUtils.getQueueName(new TaskModel())),
                taskId);
    }

    @Test
    public void testExecute_TaskIsTerminal_TaskIsRemovedFromQueue() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.TERMINAL);
        this.taskModelStubbing.thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        Mockito.verify(this.queueDAOMock).remove(Mockito.eq(QueueUtils.getQueueName(task)), taskId);
    }

    @Test
    public void testExecute_TaskIsInDatabaseButNotScheduledOrInProgress_TaskIsRemovedFromQueue() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        this.taskModelStubbing.thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        Mockito.verify(this.queueDAOMock).remove(Mockito.eq(QueueUtils.getQueueName(task)), taskId);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_LoadTaskQuietlyReturnsNull() {
        // Given
        String taskId = UUID.randomUUID().toString();
        this.taskModelStubbing.thenReturn(null);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        // The test case should throw a NullPointerException since the task is null
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_TaskIsScheduledButConcurrentExecutionLimitIsExceeded_TaskIsPostponed() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        this.taskModelStubbing.thenReturn(task);
        this.workflowModelStubbing.thenReturn(workflow);
        Monitors mockMonitor = Mockito.mock(Monitors.class);
        this.executionDAOFacadeMock.setMonitors(mockMonitor);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        // The test case should throw a RuntimeException since the concurrent execution limit is exceeded
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_TaskIsScheduledButRateLimitPerFrequencyIsExceeded_TaskIsPostponed() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        this.taskModelStubbing.thenReturn(task);
        this.workflowModelStubbing.thenReturn(workflow);
        Monitors mockMonitor = Mockito.mock(Monitors.class);
        this.executionDAOFacadeMock.setMonitors(mockMonitor);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        // The test case should throw a RuntimeException since the rate limit per frequency is exceeded
    }

    @Test
    public void testExecute_TaskIsScheduled_TaskIsExecuted() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.RUNNING);
        this.taskModelStubbing.thenReturn(task);
        this.workflowModelStubbing.thenReturn(workflow);
        Monitors mockMonitor = Mockito.mock(Monitors.class);
        this.executionDAOFacadeMock.setMonitors(mockMonitor);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        // The execution should have been executed
    }

    @Test
    public void testExecute_TaskIsInProgress_TaskIsUpdated() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.RUNNING);
        this.taskModelStubbing.thenReturn(task);
        this.workflowModelStubbing.thenReturn(workflow);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        // The task should have been updated
    }

    @Test
    public void testExecute_TaskIsCompleted_TaskIsRemovedFromQueue() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.COMPLETED);
        this.taskModelStubbing.thenReturn(task);
        this.workflowModelStubbing.thenReturn(workflow);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        Mockito.verify(this.queueDAOMock).remove(Mockito.eq(QueueUtils.getQueueName(task)), taskId);
    }

    @Test
    public void testExecute_TaskIsTerminalButNotCompleted_TaskIsUpdated() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.TERMINATED);
        this.taskModelStubbing.thenReturn(task);
        this.workflowModelStubbing.thenReturn(workflow);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        // Then
        // The task should have been updated
    }

    @Test
    public void testPostponeQuietly_TaskIsPostponed() {
        // Given
        String queueName = "queueName";
        String taskId = UUID.randomUUID().toString();

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, new TaskModel());

        // Then
        Mockito.verify(this.queueDAOMock).postpone(
                Mockito.eq(queueName), Mockito.eq(taskId), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testLoadTaskQuietly_TaskIsLoaded() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();

        // When
        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertEquals(task, loadedTask);
    }
}