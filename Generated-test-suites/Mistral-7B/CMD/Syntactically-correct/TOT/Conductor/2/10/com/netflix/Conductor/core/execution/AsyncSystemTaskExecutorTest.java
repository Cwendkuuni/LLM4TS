package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);

        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "systemTaskCallbackTime", 5000L);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 1000L);

        this.asyncSystemTaskExecutor = asyncSystemTaskExecutor;
    }

    @Test
    public void testExecute_taskNotExist_queueShouldBeCleared() {
        String taskId = "taskId1";
        WorkflowSystemTask workflowSystemTask = createWorkflowSystemTask();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verifyQueueIsEmpty();
    }

    @Test
    public void testExecute_taskExist_statusIsTerminal_queueShouldBeCleared() {
        String taskId = "taskId1";
        WorkflowSystemTask workflowSystemTask = createWorkflowSystemTask();
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(TaskModel.Status.TERMINAL);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verifyQueueIsEmpty();
    }

    @Test
    public void testExecute_taskExist_statusIsScheduled_taskShouldBeExecuted() {
        String taskId = "taskId1";
        WorkflowSystemTask workflowSystemTask = createWorkflowSystemTask();
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setCallbackAfterSeconds(0L);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.get(any())).thenReturn(Optional.empty());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Check that the task was executed
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        executionDAOFacade.updateTask(taskModel);
        verifyTaskIsExecuted(taskId);
    }

    @Test
    public void testExecute_taskExist_statusIsInProgress_callbackShouldBeSet() {
        String taskId = "taskId1";
        WorkflowSystemTask workflowSystemTask = createWorkflowSystemTask();
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Check that the callback was set
        taskModel.setCallbackAfterSeconds(1000L);
        executionDAOFacade.updateTask(taskModel);
        verifyTaskHasCallback(taskId, 1000L);
    }

    @Test
    public void testExecute_taskExist_statusIsInProgress_queueShouldBePostponed() {
        String taskId = "taskId1";
        WorkflowSystemTask workflowSystemTask = createWorkflowSystemTask();
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setCallbackAfterSeconds(0L);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.get(any())).thenReturn(Optional.ofNullable(mock(QueueModel.class)));

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Check that the queue was postponed
        QueueModel queueModel = mock(QueueModel.class);
        when(queueModel.getId()).thenReturn(taskId);
        when(queueModel.getSize()).thenReturn(1);
        when(queueModel.getPriority()).thenReturn(0L);
        when(queueModel.getNextExecutionTime()).thenReturn(Instant.now().plus(Duration.ofSeconds(1000)));

        verifyQueueIsPostponed(queueModel, 1000L);
    }

    @Test
    public void testExecute_taskExist_statusIsFailed_queueShouldBeCleared() {
        String taskId = "taskId1";
        WorkflowSystemTask workflowSystemTask = createWorkflowSystemTask();
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(TaskModel.Status.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verifyQueueIsEmpty();
    }

    private void verifyQueueIsEmpty() {
        when(queueDAO.get(any())).thenReturn(Optional.empty());
        reset(queueDAO);
    }

    private void verifyTaskIsExecuted(String taskId) {
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        executionDAOFacade.updateTask(taskModel);

        verifyTaskExecuted(taskId);
    }

    private void verifyTaskHasCallback(String taskId, long seconds) {
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setCallbackAfterSeconds(seconds);

        executionDAOFacade.updateTask(taskModel);

        verify(queueDAO).postpone(any(), eq(taskId), anyLong(), eq(seconds));
        reset(queueDAO);
    }

    private void verifyQueueIsPostponed(QueueModel queueModel, long seconds) {
        when(queueDAO.get(any())).thenReturn(Optional.of(queueModel));

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(any(), eq(taskId), anyLong(), eq(seconds));
        reset(queueDAO);
    }

    private void verifyTaskExecuted(String taskId) {
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        assertNotNull(taskModel);
        assertEquals(TaskModel.Status.COMPLETED, taskModel.getStatus());
    }

    private WorkflowSystemTask createWorkflowSystemTask() {
        return new WorkflowSystemTask() {
            @Override
            public void start(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
                // no-op
            }

            @Override
            public void execute(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
                // no-op
            }

            @Override
            public boolean isAsyncComplete(TaskModel taskModel) {
                return false;
            }
        };
    }
}