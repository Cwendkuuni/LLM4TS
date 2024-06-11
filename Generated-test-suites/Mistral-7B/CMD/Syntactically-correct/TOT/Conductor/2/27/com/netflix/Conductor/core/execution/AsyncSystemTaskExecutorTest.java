package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.UndeclaredThrowableException;
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
import com.netflix.conductor.core.execution.tasks.DummyWorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.execution.ExecutionDAOFacadeImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.queue.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.metrics.TestMonitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModelImpl;

@SuppressWarnings("unchecked")
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

    @Mock
    private DummyWorkflowSystemTask dummyWorkflowSystemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private WorkflowInstanceStatus workflowInstanceStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(workflowInstanceStatus);
        when(workflowInstanceStatus.isTerminal()).thenReturn(true);
        when(queueDAO.getQueueName(task)).thenReturn("testQueueName");
        when(queueDAO.remove(anyString(), anyString())).thenAnswer(i -> null);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenAnswer(i -> null);
        when(executionDAOFacade.updateTask(task)).thenAnswer(i -> null);
        when(queueDAO.remove(anyString(), anyString())).thenAnswer(i -> null);
        when(workflowExecutor.decide(anyString())).thenAnswer(i -> null);
        when(workflowExecutor.decide(anyString())).thenAnswer(i -> null);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(null);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(null);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(null);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(false);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(Monitors.recordQueueWaitTime(anyString(), anyLong())).then(invocation -> null);
    }

    @Test
    public void testExecute_TaskNotFoundInDB_RemovesDeadTaskFromQueue() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);
        verify(queueDAO).remove("testQueueName", taskId);
    }

    @Test
    public void testExecute_TaskIsCompleted_RemovesCompletedTaskFromQueue() {
        String workflowId = "workflowId";
        WorkflowModelImpl workflowModel = new WorkflowModelImpl();
        workflowModel.setStatus(WorkflowInstanceStatus.COMPLETED);
        when(workflow.getWorkflowInstanceId()).thenReturn(workflowId);
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, task.getTaskId());
        verify(queueDAO).remove("testQueueName", task.getTaskId());
    }

    @Test
    public void testExecute_TaskIsInProgress_PostponeTaskInQueue() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(queueDAO).postpone("testQueueName", taskId, 1, systemTaskCallbackTime.longValue());
    }

    @Test(expected = UndeclaredThrowableException.class)
    public void testExecute_TaskExecutionError_LogsErrorAndReturns() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModelImpl();
        workflow.setStatus(WorkflowInstanceStatus.RUNNING);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowInstanceId(), false)).thenReturn(workflow);
        when(workflow.getStatus()).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(dummyWorkflowSystemTask, taskId);

        verify(queueDAO, never()).remove("testQueueName", taskId);
    }
}