package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskMock;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.dao.WorkflowDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.PropertiesUtil;

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

    @Mock
    private QueueUtils queueUtils;

    @Mock
    private WorkflowDAO workflowDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private AsyncSystemTaskExecutor systemUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(PropertiesUtil.duration(5000));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(PropertiesUtil.duration(2000));
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenAnswer(new Answer<WorkflowModel>() {
            @Override
            public WorkflowModel answer(InvocationOnMock invocation) throws Throwable {
                return new WorkflowModel();
            }
        });
        when(workflowDAO.getWorkflowModel(anyString())).thenReturn(new WorkflowModel());
        when(queueDAO.get(anyString(), anyString())).thenAnswer(new Answer<TaskModel>() {
            @Override
            public TaskModel answer(InvocationOnMock invocation) throws Throwable {
                String taskId = invocation.getArgument(1);
                return new TaskModel().withId(taskId).withStatus(ExecutionStatus.SCHEDULED);
            }
        });
        when(queueDAO.postpone(
                eq(QueueUtils.getQueueName(any(TaskModel.class))),
                anyString(),
                anyInt(),
                anyLong()))
                .thenReturn(1);
        when(queueDAO.remove(eq(QueueUtils.getQueueName(any(TaskModel.class))), anyString()))
                .thenReturn(true);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskModel());
        when(queueDAO.remove(eq(QueueUtils.getQueueName(any(TaskModel.class))), anyString()))
                .thenReturn(true);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any()))
                .thenReturn(false);
        doNothing().when(monitors).recordQueueWaitTime(anyString(), anyLong());
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        systemUnderTest = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
        systemUnderTest.queueUtils = queueUtils;
        systemUnderTest.workflowDAO = workflowDAO;
        systemUnderTest.monitors = monitors;
    }

    @Test
    public void testExecute_taskAlreadyCompleted_queueRemoved() {
        String workflowId = "workflowId";
        TaskModel task = new TaskModel().withId("taskId").withWorkflowInstanceId(workflowId);
        String queueName = QueueUtils.getQueueName(task);

        when(executionDAOFacade.getTaskModel(task.getId()))
                .thenReturn(new TaskModel().withId(task.getId()).withStatus(ExecutionStatus.COMPLETED));

        systemUnderTest.execute(any(WorkflowSystemTask.class), task.getId());

        verify(queueDAO).remove(eq(queueName), task.getId());
    }

    @Test
    public void testExecute_taskRateLimitExceeded_queuePostponed() {
        TaskModel task = new TaskModel().withId("taskId").withStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(task.getId()))
                .thenReturn(new TaskModel().withId(task.getId()).withStatus(ExecutionStatus.SCHEDULED));
        when(metadataDAO.getTaskDef(task.getTaskDefName()))
                .thenReturn(new TaskModel().withId(task.getTaskDefName()).withRateLimitPerFrequency(1));

        systemUnderTest.execute(any(WorkflowSystemTask.class), task.getId());

        verify(queueDAO, times(1)).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(task.getId()),
                anyInt(),
                eq(2000));
    }

    @Test
    public void testExecute_taskInProgress_queuePostponed() {
        TaskModel task = new TaskModel().withId("taskId").withStatus(ExecutionStatus.SCHEDULED);

        systemUnderTest.execute(any(WorkflowSystemTask.class), task.getId());

        verify(queueDAO, times(1)).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(task.getId()),
                anyInt(),
                eq(5000));
    }

    @Test
    public void testExecute_workflowCompleted_queueRemoved() {
        String workflowId = "workflowId";
        TaskModel task = new TaskModel().withId("taskId").withWorkflowInstanceId(workflowId);
        String queueName = QueueUtils.getQueueName(task);
        WorkflowModel workflow = new WorkflowModel().withId(workflowId).withStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getWorkflowModel(workflowId, true))
                .thenReturn(workflow);

        systemUnderTest.execute(any(WorkflowSystemTask.class), task.getId());

        verify(queueDAO).remove(eq(queueName), task.getId());
    }

    @Test
    public void testExecute_taskAlreadyPostponed_queueNotChanged() {
        TaskModel task = new TaskModel().withId("taskId").withStatus(ExecutionStatus.SCHEDULED);
        String queueName = QueueUtils.getQueueName(task);

        when(queueDAO.get(eq(queueName), eq(task.getId())))
                .thenReturn(Optional.of(new TaskModel().withId(task.getId()).withStatus(ExecutionStatus.POSTPONED)));

        systemUnderTest.execute(any(WorkflowSystemTask.class), task.getId());

        verify(queueDAO, never()).remove(eq(queueName), task.getId());
        verify(queueDAO, never()).postpone(
                eq(queueName),
                eq(task.getId()),
                anyInt(),
                anyLong());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_taskNotFound() {
        TaskModel task = new TaskModel().withId("taskId").withStatus(ExecutionStatus.SCHEDULED);

        systemUnderTest.execute(any(WorkflowSystemTask.class), task.getId());

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());

        throw new IllegalStateException("Task not found");
    }

    @Test
    public void testExecute_taskExecutionException_queueNotChanged() {
        TaskModel task = new TaskModel().withId("taskId").withStatus(ExecutionStatus.SCHEDULED);
        String queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(eq(queueName), eq(task.getId())))
                .thenReturn(Optional.of(new TaskModel().withId(task.getId()).withStatus(ExecutionStatus.SCHEDULED)));

        WorkflowSystemTaskMock systemTask = mock(WorkflowSystemTask.class);
        doThrow(new RuntimeException("Test exception"))
                .when(systemTask)
                .execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        systemUnderTest.execute(systemTask, task.getId());

        verify(queueDAO, never()).remove(eq(queueName), task.getId());
        verify(queueDAO, never()).postpone(
                eq(queueName),
                eq(task.getId()),
                anyInt(),
                anyLong());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }
}