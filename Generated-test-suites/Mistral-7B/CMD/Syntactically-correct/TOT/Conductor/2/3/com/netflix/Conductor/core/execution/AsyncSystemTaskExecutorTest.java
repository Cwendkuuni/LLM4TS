package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.Queue.Priority;
import com.netflix.conductor.dao.Task;
import com.netflix.conductor.utils.QueueUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.testng.PowerMockTestCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PrepareForTest({AsyncSystemTaskExecutor.class})
public class AsyncSystemTaskExecutorTest extends PowerMockTestCase {

    @Autowired
    private ExecutionDAOFacade executionDAOFacadeMock;

    @Autowired
    private QueueDAO queueDAOMock;

    @Autowired
    private MetadataDAO metadataDAOMock;

    @Autowired
    private WorkflowExecutor workflowExecutorMock;

    private static final String WORKFLOW_ID = "workflowId";
    private static final String QUEUE_NAME = QueueUtils.getQueueName(new TaskModel(1L, "taskType", "taskId"));
    private static final String TASK_ID = "taskId";
    private static final String SYSTEM_TASK_TYPE = "systemTaskType";
    private static final String TASK_DEF_NAME = "taskDefName";
    private static final long WAIT_TIME = 10;

    private WorkflowSystemTask systemTaskMock;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private TaskModel taskModelMock;
    private WorkflowModel workflowModelMock;
    private Queue queueMock;

    @BeforeEach
    public void setup() {
        systemTaskMock = Mockito.mock(WorkflowSystemTask.class);
        taskModelMock = new TaskModel(1L, "taskType", "taskId");
        workflowModelMock = new WorkflowModel();
        queueMock = new Queue(QUEUE_NAME, Priority.HIGH, WAIT_TIME, null, null);

        PowerMockito.mockStatic(QueueUtils.class);
        when(QueueUtils.getQueueName(any(TaskModel.class))).thenReturn(QUEUE_NAME);

        ConductorProperties conductorPropertiesMock = Mockito.mock(ConductorProperties.class);
        when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorPropertiesMock.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(5));

        executionDAOFacadeMock = Mockito.spy(new ExecutionDAOFacade() {
            @Override
            public WorkflowModel getWorkflowModel(String workflowId, boolean isTaskRetrievalRequired) {
                return workflowModelMock;
            }

            @Override
            public TaskModel getTaskModel(String taskId) {
                return taskModelMock;
            }

            @Override
            public void updateTask(TaskModel task) {
                // no-op
            }

            @Override
            public boolean exceedsInProgressLimit(TaskModel task) {
                return false;
            }

            @Override
            public boolean exceedsRateLimitPerFrequency(
                    TaskModel task, WorkflowModel workflowModel) {
                return false;
            }
        });

        queueDAOMock = Mockito.spy(new QueueDAO() {
            @Override
            public void postpone(
                    String queueName, String taskId, Priority priority, long delaySeconds) {
                // no-op
            }

            @Override
            public void remove(String queueName, String taskId) {
                // no-op
            }
        });

        metadataDAOMock = Mockito.mock(MetadataDAO.class);
        workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                Mockito.mock(ConductorProperties.class),
                workflowExecutorMock);

        PowerMockito.doAnswer(
                        (Answer<Void>) invocation -> {
                            workflowModelMock.setStatus(WorkflowModel.Status.COMPLETED);
                            return null;
                        })
                .when(executionDAOFacadeMock)
                .getWorkflowModel(anyString(), anyBoolean());

        PowerMockito.doAnswer(
                        (Answer<Optional<TaskModel>>) invocation -> {
                            TaskModel taskModel = new TaskModel(1L, "taskType", "taskId");
                            taskModel.setStatus(TaskModel.Status.SCHEDULED);
                            return Optional.of(taskModel);
                        })
                .when(executionDAOFacadeMock)
                .getTaskModel(anyString());

        PowerMockito.doAnswer(
                        (Answer<Void>) invocation -> {
                            taskModelMock.setStatus(TaskModel.Status.CANCELED);
                            return null;
                        })
                .when(executionDAOFacadeMock)
                .updateTask(any(TaskModel.class));

        PowerMockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                queueDAOMock.postpone(QUEUE_NAME, TASK_ID, Priority.HIGH, 5L);
                return null;
            }
        }).when(queueDAOMock)
                .postpone(QUEUE_NAME, TASK_ID, Priority.HIGH, anyLong());

        PowerMockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                queueDAOMock.remove(QUEUE_NAME, TASK_ID);
                return null;
            }
        }).when(queueDAOMock)
                .remove(QUEUE_NAME, TASK_ID);

        PowerMockito.doAnswer(new Answer<Optional<WorkflowModel>>() {
            @Override
            public Optional<WorkflowModel> answer(InvocationOnMock invocation) throws Throwable {
                return Optional.ofNullable(workflowModelMock);
            }
        }).when(executionDAOFacadeMock)
                .getWorkflowModel(anyString(), anyBoolean());
    }

    @Test
    public void testExecuteWithTerminalWorkflow() {
        given(workflowModelMock.getStatus()).willReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTaskMock, TASK_ID);

        verify(queueDAOMock, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecuteWithScheduledTask() {
        given(workflowModelMock.getStatus()).willReturn(WorkflowModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTaskMock, TASK_ID);

        verify(executionDAOFacadeMock, times(1)).updateTask(taskModelMock);
        verify(queueDAOMock, times(1)).postpone(QUEUE_NAME, TASK_ID, Priority.HIGH, 5L);
    }

    @Test
    public void testExecuteWithInProgressTask() {
        given(workflowModelMock.getStatus()).willReturn(WorkflowModel.Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTaskMock, TASK_ID);

        verify(executionDAOFacadeMock, times(1)).updateTask(taskModelMock);
        verify(queueDAOMock, times(1)).postpone(QUEUE_NAME, TASK_ID, Priority.HIGH, 10L);
    }

    @Test
    public void testExecuteWithCompletedTask() {
        given(workflowModelMock.getStatus()).willReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTaskMock, TASK_ID);

        verify(queueDAOMock, never()).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecuteWithTaskNotFound() {
        given(executionDAOFacadeMock.getTaskModel(TASK_ID)).willReturn(Optional.empty());

        asyncSystemTaskExecutor.execute(systemTaskMock, TASK_ID);

        verify(queueDAOMock, never()).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testPostponeQuietlyWithTaskNotFound() {
        given(executionDAOFacadeMock.getTaskModel(TASK_ID)).willReturn(Optional.empty());

        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, taskModelMock);

        verify(queueDAOMock, never()).postpone(QUEUE_NAME, TASK_ID, any(Priority.class), anyLong());
    }

    @Test
    public void testLoadTaskQuietlyWithTaskNotFound() {
        given(executionDAOFacadeMock.getTaskModel(TASK_ID)).willReturn(Optional.empty());

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        Assertions.assertNull(loadedTask);
    }
}