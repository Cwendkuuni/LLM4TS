package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.WorkflowModelImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private ConductorProperties conductorProperties;

    private AsyncSystemTaskExecutor systemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        systemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
        ReflectionTestUtils.setFieldValue(systemTaskExecutor, "queueTaskMessagePostponeSecs", 1L);
        ReflectionTestUtils.setFieldValue(systemTaskExecutor, "systemTaskCallbackTime", 2L);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doThrow(new RuntimeException("Task not found"))
                .when(executionDAOFacade)
                .getTaskModel(taskId);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade, times(1))
                .getTaskModel(taskId);
        verifyNoInteractions(queueDAO);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskInTerminalState() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("workflowId");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.COMPLETED);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(0), false)).thenReturn(workflow);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verifyNoInteractions(queueDAO).postpone(ArgumentMatchers.anyString(), ArgumentMatchers.eq(taskId), ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong());
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskInScheduledState_noLimitExceeded() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("workflowId");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(0), false)).thenReturn(workflow);
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskInScheduledState_executionLimitExceeded() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("workflowId");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(0), false)).thenReturn(workflow);
        when(queueDAO.remove(queueName, taskId)).thenReturn(false);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, 0, 1L);
    }

    @Test
    public void testExecute_taskInScheduledState_rateLimitExceeded() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("workflowId");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(0), false)).thenReturn(workflow);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new Object());

        doThrow(new RuntimeException("Rate limit exceeded"))
                .when(executionDAOFacade)
                .exceedsRateLimitPerFrequency(task, ArgumentMatchers.any());

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, 0, 1L);
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("workflowId");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(0), false)).thenReturn(workflow);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, times(1)).execute(workflow, task, workflowExecutor);
    }

    @Test
    public void testExecute_taskCompletion() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("workflowId");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(0), false)).thenReturn(workflow);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskCompletionWithCallback() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("workflowId");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(0), false)).thenReturn(workflow);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, 0, 2L);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setId("taskId");

        when(queueDAO.postpone(queueName, task.getId(), 0, 1L))
                .thenReturn(true);

        systemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(queueName, task.getId(), 0, 1L);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("Task not found"));

        TaskModel task = systemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(task);
    }

    @Test
    public void testLoadTaskQuietly_taskLoaded() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = systemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(task, loadedTask);
    }
}