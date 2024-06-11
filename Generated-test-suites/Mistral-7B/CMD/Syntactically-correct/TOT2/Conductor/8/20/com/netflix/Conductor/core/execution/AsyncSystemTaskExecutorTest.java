package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.DummyWorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

@SuppressWarnings("deprecation")
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    @Mock
    private Monitors monitors;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(getDummyTaskModel());
        when(queueDAO.getWorkflowModel(anyString(), anyBoolean())).thenReturn(getDummyWorkflowModel());
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(getDummyTaskModel());
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(queueDAO.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(false);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(getDummyTaskDef());
        when(workflowExecutor.decide(anyString())).thenReturn(true);
    }

    @Test
    public void testExecute_taskIsTerminal_queueIsRemoved() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = new DummyWorkflowSystemTask();
        TaskModel task = getDummyTaskModel();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void testExecute_taskIsScheduled_queueIsNotRemoved() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        WorkflowSystemTask systemTask = new DummyWorkflowSystemTask();
        TaskModel task = getDummyTaskModel();
        WorkflowModel workflow = getDummyWorkflowModel();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(eq("queueName"), eq(taskId));
        verify(queueDAO, times(1)).postpone(eq("queueName"), eq(taskId), eq(workflow.getPriority()),
                eq(systemTaskCallbackTime()));
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_taskIsInProgress_throwsException() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        WorkflowSystemTask systemTask = new DummyWorkflowSystemTask();
        TaskModel task = getDummyTaskModel();
        WorkflowModel workflow = getDummyWorkflowModel();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(eq("queueName"), eq(taskId));
        verify(queueDAO, times(1)).postpone(eq("queueName"), eq(taskId), eq(workflow.getPriority()),
                eq(systemTaskCallbackTime()));

        // This test case should throw an exception as the task is in progress and should not be executed again
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_workflowIsTerminal_queueIsRemoved() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        WorkflowSystemTask systemTask = new DummyWorkflowSystemTask();
        TaskModel task = getDummyTaskModel();
        WorkflowModel workflow = getDummyWorkflowModelWithTerminalStatus();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void testPostponeQuietly_queueIsPostponed() {
        String queueName = "queueName";
        String taskId = "task-id";

        asyncSystemTaskExecutor.postponeQuietly(queueName, getDummyTaskModel());

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskIsLoaded() {
        String taskId = "task-id";

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(task);
    }

    private TaskModel getDummyTaskModel() {
        return new TaskModel()
                .setTaskId("task-id")
                .setTaskType("task-type")
                .setStatus(TaskModel.Status.SCHEDULED)
                .setWorkflowInstanceId("workflow-id")
                .setRateLimitPerFrequency(0)
                .setInProgressLimit(0);
    }

    private WorkflowModel getDummyWorkflowModel() {
        return new WorkflowModel()
                .setId("workflow-id")
                .setName("workflow-name")
                .setStatus(WorkflowStatus.RUNNING)
                .setPriority(1);
    }

    private WorkflowModel getDummyWorkflowModelWithTerminalStatus() {
        return new WorkflowModel()
                .setId("workflow-id")
                .setName("workflow-name")
                .setStatus(WorkflowStatus.COMPLETED);
    }

    private TaskDef getDummyTaskDef() {
        return new TaskDef()
                .setName("task-def-name")
                .setRateLimitPerFrequency(0);
    }

    private long systemTaskCallbackTime() {
        return TimeUnit.SECONDS.toMillis(10);
    }
}