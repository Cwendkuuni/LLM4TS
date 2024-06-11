package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.TaskType;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueId;
import com.netflix.conductor.metrics.Monitors;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask mockWorkflowSystemTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockWorkflowSystemTask = mock(WorkflowSystemTask.class);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(mock(TaskModel.class)), taskId);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(mock(TaskModel.class)), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        task.setWorkflowInstanceId(workflowId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, never()).remove(QueueUtils.getQueueName(mock(TaskModel.class)), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testExecute_taskScheduled() {
        String taskId = "taskId";
        String workflowId = "workflowId";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setWorkflowInstanceId(workflowId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        verify(queueDAO, never()).remove(QueueUtils.getQueueName(mock(TaskModel.class)), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test(expected = Exception.class)
    public void testExecute_queueDAORemoveException() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());

        doThrow(new RuntimeException("Exception")).when(queueDAO).remove(anyString(), eq(taskId));

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        QueueId queueId = new QueueId("queueName", "queueType");

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_nullTaskModel() {
        String taskId = "taskId";

        TaskModel expectedTaskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel actualTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(actualTaskModel);
    }

    @Test
    public void testLoadTaskQuietly_taskModel() {
        String taskId = "taskId";
        TaskModel expectedTaskModel = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(expectedTaskModel);

        TaskModel actualTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(expectedTaskModel, actualTaskModel);
    }

    @Test
    public void testExceedsInProgressLimit() {
        TaskModel task = new TaskModel();
        task.setTaskType(new TaskType("testTaskType"));
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);

        boolean result = asyncSystemTaskExecutor.exceedsInProgressLimit(task);

        assertFalse(result);

        // Simulate a second task with the same type
        TaskModel anotherTask = new TaskModel();
        anotherTask.setTaskType(new TaskType("testTaskType"));
        anotherTask.setTaskId("anotherTaskId");
        when(executionDAOFacade.getTaskModel(anotherTask.getTaskId())).thenReturn(anotherTask);

        result = asyncSystemTaskExecutor.exceedsInProgressLimit(task);

        assertTrue(result);
    }

    @Test
    public void testExceedsRateLimitPerFrequency() {
        TaskModel task = new TaskModel();
        task.setTaskType(new TaskType("testTaskType"));
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setRateLimitPerFrequency(Integer.MAX_VALUE);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(workflowModel);

        boolean result = asyncSystemTaskExecutor.exceedsRateLimitPerFrequency(task, workflowModel);

        assertFalse(result);

        // Simulate a second task with the same type within the frequency limit
        TaskModel anotherTask = new TaskModel();
        anotherTask.setTaskType(new TaskType("testTaskType"));
        anotherTask.setTaskId("anotherTaskId");
        anotherTask.setRateLimitPerFrequency(1);
        anotherTask.setLastExecutionTime(System.currentTimeMillis() - (2 * ConductorProperties.SYSTEM_TASK_WORKER_FREQUENCY.getSeconds()));
        when(executionDAOFacade.getTaskModel(anotherTask.getTaskId())).thenReturn(anotherTask);

        result = asyncSystemTaskExecutor.exceedsRateLimitPerFrequency(task, workflowModel);

        assertTrue(result);
    }
}