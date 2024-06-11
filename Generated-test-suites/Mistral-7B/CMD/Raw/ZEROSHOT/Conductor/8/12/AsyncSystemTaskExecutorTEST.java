package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.TestConductorProperties;
import com.netflix.conductor.utils.TestWorkflowExecutor;

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "executionDAOFacade", executionDAOFacade);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "queueDAO", queueDAO);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "metadataDAO", metadataDAO);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "workflowExecutor", workflowExecutor);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "conductorProperties", new TestConductorProperties());
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_queueIsRemoved() {
        // given
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        String queueName = QueueUtils.getQueueName(task);

        // when
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        // then
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskIsInProgress_taskIsUpdated() throws InterruptedException {
        // given
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);

        // when
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // then
        assertNotNull(task.getStartTime());
        assertNotNull(task.getCallbackAfterSeconds());
        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyLong());

        TimeUnit.SECONDS.sleep(asyncSystemTaskExecutor.getSystemTaskCallbackTime() + 1);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // then
        assertNotNull(task.getEndTime());
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_taskIsInProgress_exceptionThrownWhenExecutingTask() {
        // given
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        doThrow(new RuntimeException("Test Exception")).when(workflowSystemTask).execute(any(), any(), any());

        // when
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // then
        // IllegalStateException should be thrown
    }

    @Test
    public void testExecute_taskIsTerminal_queueIsRemoved() {
        // given
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.TERMINATED);

        String queueName = QueueUtils.getQueueName(task);

        // when
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        // then
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_queueSizeLimitIsExceeded_queueIsPostponed() {
        // given
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("test-task");

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        // when
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        // then
        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_rateLimitPerFrequencyIsExceeded_queueIsPostponed() {
        // given
        String taskId = "task-id";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("test-task");
        task.setRateLimitPerFrequency(1);

        WorkflowModel workflowModel = mock(WorkflowModel.class);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        // when
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        // then
        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyLong());
    }
}