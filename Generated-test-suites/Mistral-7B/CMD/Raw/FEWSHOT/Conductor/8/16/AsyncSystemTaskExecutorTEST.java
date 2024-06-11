package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO.QueueAction;
import com.netflix.conductor.utils.QueueUtils;

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
    private AsyncSystemTaskExecutor systemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(5000L);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(1000L);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskCompleted() {
        String taskId = "123";
        String workflowId = "12345";
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(executionDAOFacade).getWorkflowModel(workflowId, false);
        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskRateLimitExceeded() {
        String taskId = "123";
        String taskDefName = "testTask";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskDefName(taskDefName);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowModel workflow = mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(metadataDAO.getTaskDef(taskDefName)).thenReturn(workflow);
        when(workflow.getRateLimitPerFrequency()).thenReturn(1L);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, workflow)).thenReturn(true);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), taskId, anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "123";
        String workflowId = "12345";
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), taskId, anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskTerminal() {
        String taskId = "123";
        String workflowId = "12345";
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.TERMINAL);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(queueDAO.getQueueSize(anyString())).thenReturn(1);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskAlreadyInProgress() {
        String taskId = "123";
        String workflowId = "12345";
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        systemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).postpone(anyString(), taskId, anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskInQueue() {
        String taskId = "123";
        String queueName = QueueUtils.getQueueName("queueName");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        when(queueDAO.getQueueSize(queueName)).thenReturn(0);

        systemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(queueName, taskId, anyInt(), anyLong());

        when(queueDAO.getQueueSize(queueName)).thenReturn(1);

        systemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, never()).postpone(queueName, taskId, anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskInProgressLimitExceeded() {
        String taskId = "123";
        String queueName = QueueUtils.getQueueName("queueName");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.getQueueSize(queueName)).thenReturn(10);

        systemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(queueName, taskId, anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setId("taskId");

        systemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getId(), task.getWorkflowPriority(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "123";

        assertNull(systemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = systemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }
}