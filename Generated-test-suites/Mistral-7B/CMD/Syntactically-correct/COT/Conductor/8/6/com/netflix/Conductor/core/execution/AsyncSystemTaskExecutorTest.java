package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueSettings;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.testutils.TestExecutionStatus;
import com.netflix.conductor.testutils.TestTaskModel;
import com.netflix.conductor.testutils.TestWorkflowModel;
import com.netflix.conductor.testutils.TestWorkflowSystemTask;

@ActiveProfiles("test")
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

    private final String queueName = "testQueue";
    private final String taskId = "testTaskId";
    private final String workflowId = "testWorkflowId";
    private final String taskDefName = "testTaskDefName";
    private final String workflowInstanceId = "testWorkflowInstanceId";

    @Before
    public void initMocks() {
        Monitors mockMonitors = mock(Monitors.class);
        when(executionDAOFacade.getWorkflowModel(any(String.class), any(Boolean.class)))
                .thenReturn(new TestWorkflowModel());
        when(metadataDAO.getTaskDef(any(String.class)))
                .thenReturn(new TestTaskModel(taskDefName, 0));
        when(queueDAO.getQueueSettings(queueName)).thenReturn(new QueueSettings());
        when(workflowExecutor.decide(any(String.class))).thenReturn(null);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(TestExecutionStatus.callbackDuration());
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(TestExecutionStatus.postponeDuration());
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TestTaskModel());
        when(executionDAOFacade.getExecutionStatus(taskId)).thenReturn(TestExecutionStatus.getExecutionStatus());
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class)))
                .thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any(TaskModel.class)))
                .thenReturn(false);
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(new TestTaskModel());
        when(executionDAOFacade.getTaskModel(any(String.class))).thenAnswer(invocation -> null);
        when(executionDAOFacade.getExecutionStatus(any(String.class))).thenAnswer(invocation -> null);
        when(queueDAO.postpone(
                any(String.class),
                any(String.class),
                any(Integer.class),
                any(Long.class))).thenAnswer(invocation -> null);
        when(queueDAO.remove(any(String.class), any(String.class))).thenAnswer(invocation -> null);
        when(queueDAO.getTaskByIdAndQueueName(any(String.class), any(String.class)))
                .thenReturn(Optional.ofNullable(new TestTaskModel()));

        when(workflowExecutor.decide(any(String.class))).thenAnswer(invocation -> null);

        AsyncSystemTaskExecutor testee = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        asyncSystemTaskExecutor = testee;
    }

    @Test
    public void testExecute_taskNotFound() {
        assertNull(asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), taskId));
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        TaskModel task = new TestTaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), taskId);

        // No action should be taken as the task is already completed
    }

    @Test
    public void testExecute_taskInProgress() {
        TaskModel task = new TestTaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new TestWorkflowModel();
        workflow.setStatus(WorkflowStatus.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), taskId);

        // The task status should be updated and the workflow should be evaluated
    }

    @Test
    public void testExecute_taskScheduled() {
        TaskModel task = new TestTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new TestWorkflowModel();
        workflow.setStatus(WorkflowStatus.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), taskId);

        // The task status should be updated and the workflow should be evaluated
    }

    @Test(expected = Exception.class)
    public void testExecute_workflowFailed() {
        TaskModel task = new TestTaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new TestWorkflowModel();
        workflow.setStatus(WorkflowStatus.FAILED);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), taskId);

        // An exception should be thrown as the workflow has failed
    }

    @Test(expected = Exception.class)
    public void testExecute_taskRateLimitExceeded() {
        TaskModel task = new TestTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new TestWorkflowModel();
        workflow.setStatus(WorkflowStatus.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any(TaskModel.class)))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), taskId);

        // An exception should be thrown as the rate limit has been exceeded
    }

    @Test(expected = Exception.class)
    public void testExecute_taskConcurrentLimitExceeded() {
        TaskModel task = new TestTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new TestWorkflowModel();
        workflow.setStatus(WorkflowStatus.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), taskId);

        // An exception should be thrown as the concurrent limit has been exceeded
    }

    @Test(expected = Exception.class)
    public void testExecute_exceptionOnSystemTask() {
        TaskModel task = new TestTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = new TestWorkflowModel();
        workflow.setStatus(WorkflowStatus.RUNNING);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);

        WorkflowSystemTask systemTask = new TestWorkflowSystemTask();
        when(systemTask.isAsyncComplete(task)).thenAnswer(invocation -> false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // An exception should be thrown as there is an exception on the system task
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly(queueName, new TestTaskModel());

        // No action should be taken as the task is already postponed
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        TaskModel task = new TestTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        assertNotNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
}