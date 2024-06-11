package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.quality.Strictness;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueNameGenerator;
import com.netflix.conductor.utils.TestWorkflowExecutor;
import com.netflix.conductor.utils.TestWorkflowModel;
import com.netflix.conductor.utils.TestExecutionDAOFacade;
import com.netflix.conductor.utils.TestMetadataDAO;
import com.netflix.conductor.utils.TestQueueDAO;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AsyncSystemTaskExecutorTest {

    @Rule
    public MockitoJUnit mockitoRule = new MockitoJUnit(Strictness.STRICT_STUBS);

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private TestWorkflowModel workflowModel;

    @Mock
    private TestExecutionDAOFacade testExecutionDAOFacade;

    @Mock
    private TestMetadataDAO testMetadataDAO;

    @Mock
    private TestQueueDAO testQueueDAO;

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(TestUtils.duration(10, TimeUnit.SECONDS));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(TestUtils.duration(1, TimeUnit.MINUTES));

        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(anyString()))
                .willReturn(TestUtils.createTestTaskModel());
        given(executionDAOFacade.updateTask(any(TaskModel.class)))
                .willReturn(TestUtils.createTestTaskModel());

        given(queueDAO.get(anyString(), anyString()))
                .willReturn(Optional.ofNullable(TestUtils.createTestQueueModel()));
        given(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .willReturn(true);
        given(queueDAO.remove(anyString(), anyString()))
                .willReturn(true);

        given(metadataDAO.getTaskDef(anyString()))
                .willReturn(TestUtils.createTestTaskDefinition());
        given(metadataDAO.getTaskDefVersion(anyString(), anyLong()))
                .willReturn(Optional.ofNullable(TestUtils.createTestTaskDefinitionVersion()));

        given(testQueueDAO.getQueueName(any(TaskModel.class)))
                .willReturn("testQueueName");

        given(workflowExecutor.decide(anyString()))
                .willReturn(Collections.emptyList());

        doCallRealMethod().when(asyncSystemTaskExecutor).execute(any(WorkflowSystemTask.class), anyString());
    }

    @Test
    public void testExecute_taskIsNotFound() {
        String taskId = "taskId123";
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(LOGGER).error("TaskId: {} could not be found while executing {}", taskId, workflowSystemTask);
        verify(queueDAO).remove("testQueueName", taskId);
    }

    @Test
    public void testExecute_taskIsTerminal() {
        String taskId = "taskId123";
        TaskModel task = TestUtils.createTestTaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove("testQueueName", taskId);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_taskIsScheduled() {
        String taskId = "taskId123";
        TaskModel task = TestUtils.createTestTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone("testQueueName", taskId, 1, 10 * 1000L);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_taskIsInProgress() {
        String taskId = "taskId123";
        TaskModel task = TestUtils.createTestTaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask).execute(workflowModel, task, asyncSystemTaskExecutor);
        verify(queueDAO).remove("testQueueName", taskId);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_taskIsAsyncComplete() {
        String taskId = "taskId123";
        TaskModel task = TestUtils.createTestTaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        given(workflowSystemTask.isAsyncComplete(task)).willReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove("testQueueName", taskId);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_taskIsAsyncCompleteAndNotScheduled() {
        String taskId = "taskId123";
        TaskModel task = TestUtils.createTestTaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        given(workflowSystemTask.isAsyncComplete(task)).willReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).remove("testQueueName", taskId);
        verify(queueDAO, never()).postpone("testQueueName", taskId, anyInt(), anyLong());
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_workflowIsTerminal() {
        String workflowInstanceId = "workflowInstanceId123";
        TaskModel task = TestUtils.createTestTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId(workflowInstanceId);

        given(executionDAOFacade.getTaskModel(anyString())).willReturn(task);

        given(workflowModel.getStatus())
                .willReturn(WorkflowModel.Status.CANCELED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, task.getTaskId());

        verify(LOGGER).info("Workflow {} has been completed for {}", workflowInstanceId,
                workflowSystemTask);
        verify(queueDAO).remove("testQueueName", task.getTaskId());
    }

    @Test
    public void testExecute_concurrentExecutionLimitIsReached() {
        String taskId = "taskId123";
        TaskModel task1 = TestUtils.createTestTaskModel();
        task1.setStatus(TaskModel.Status.SCHEDULED);

        TaskModel task2 = TestUtils.createTestTaskModel();
        task2.setStatus(TaskModel.Status.SCHEDULED);

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task1);
        given(executionDAOFacade.getTaskModel(anyString())).willReturn(task2);

        given(executionDAOFacade.exceedsInProgressLimit(task1)).willReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone("testQueueName", taskId, 1, 10 * 1000L);
        verify(workflowExecutor).decide(task1.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_rateLimitIsReached() {
        String taskId = "taskId123";
        TaskModel task = TestUtils.createTestTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        given(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .willReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone("testQueueName", taskId, 1, 10 * 1000L);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound() {
        String taskId = "taskId123";

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(null);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskIsPresent() {
        String taskId = "taskId123";
        TaskModel task = TestUtils.createTestTaskModel();

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }

    @Test
    public void testPostponeQuietly_queueNameIsNotFound() {
        String queueName = "queueName123";
        String taskId = "taskId123";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskId);

        verify(queueDAO, never()).postpone(queueName, taskId, anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly_queueNameIsPresent() {
        String queueName = "queueName123";
        String taskId = "taskId123";

        given(queueDAO.get(queueName, taskId)).willReturn(Optional.ofNullable(TestUtils.createTestQueueModel()));

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskId);

        verify(queueDAO).postpone(queueName, taskId, 1, 10 * 1000L);
    }
}