package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private ConductorProperties conductorProperties;

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        Mockito.when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(new Duration(1000));
        Mockito.when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(new Duration(5000));

        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Mockito.when(workflowModel.getStatus()).thenReturn(Status.RUNNING);

        TaskModel taskModel = Mockito.mock(TaskModel.class);
        Mockito.when(taskModel.getWorkflowInstanceId()).thenReturn("workflowId");
        Mockito.when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        Mockito.when(taskModel.getTaskDefName()).thenReturn("taskDefinition");
        Mockito.when(executionDAOFacade.getWorkflowModel(workflowModel.getId(), false))
                .thenReturn(workflowModel);

        ArgumentCaptor<TaskModel> taskModelArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        Mockito.doAnswer(
                        invocationOnMock -> {
                            TaskModel taskModel = invocationOnMock.getArgument(0);
                            taskModel.setStatus(TaskModel.Status.COMPLETED);
                            return null;
                        })
                .when(executionDAOFacade)
                .updateTask(taskModelArgumentCaptor.capture());

        Mockito.doAnswer(invocationOnMock -> queueDAO.remove(Mockito.anyString(), Mockito.anyString()))
                .when(queueDAO).remove(Mockito.anyString(), Mockito.anyString());
        Mockito.doAnswer(invocationOnMock -> queueDAO.postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong()))
                .when(queueDAO).postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());
        Mockito.doAnswer(invocationOnMock -> queueDAO.getNextTask(Mockito.anyString()))
                .when(queueDAO).getNextTask(Mockito.anyString());

        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        Mockito.when(systemTask.isAsyncComplete(Mockito.any())).thenReturn(true);
        Mockito.when(systemTask.isTaskRetrievalRequired()).thenReturn(false);
        Mockito.when(systemTask.start(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(null);
        Mockito.when(systemTask.execute(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(null);

        Mockito.doAnswer(
                        invocationOnMock -> {
                            Monitors.recordQueueWaitTime("taskType", 1L);
                            return null;
                        })
                .when(Monitors).recordQueueWaitTime(Mockito.anyString(), Mockito.anyLong());

        Mockito.doAnswer(invocationOnMock -> workflowExecutor.decide(Mockito.anyString()))
                .when(workflowExecutor).decide(Mockito.anyString());
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_queueIsRemoved() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        TaskModel taskModel = taskModelArgumentCaptor.getValue();
        assertEquals("COMPLETED", taskModel.getStatus().toString());
        assertNull(queueDAO.getNextTask(QueueUtils.getQueueName(taskModel)));
    }

    @Test
    public void testExecute_taskIsNotScheduled_queueIsPostponed() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        TaskModel taskModel = taskModelArgumentCaptor.getValue();
        assertEquals("SCHEDULED", taskModel.getStatus().toString());
        assertSame(taskId, queueDAO.getNextTask(QueueUtils.getQueueName(taskModel)).getTaskId());
    }

    @Test
    public void testExecute_taskIsInProgress_taskIsExecuted() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        TaskModel taskModel = taskModelArgumentCaptor.getValue();
        assertEquals("IN_PROGRESS", taskModel.getStatus().toString());
        assertFalse(queueDAO.getNextTask(QueueUtils.getQueueName(taskModel)) instanceof TaskModel);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_workflowIsTerminal_queueIsRemoved() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        Mockito.when(taskModel.getWorkflowInstanceId()).thenReturn(workflowId);
        Mockito.when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        Mockito.verify(workflowExecutor).decide(workflowId);
        Mockito.verify(queueDAO).remove(QueueUtils.getQueueName(taskModel), taskId);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_queueSizeLimitIsExceeded_queueIsPostponed() {
        String queueName = "queueName";
        String taskId = "taskId";
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        Mockito.when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        Mockito.when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        Mockito.verify(queueDAO).postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_rateLimitIsExceeded_queueIsPostponed() {
        String queueName = "queueName";
        String taskId = "taskId";
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        Mockito.when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        Mockito.when(taskModel.getRateLimitPerFrequency()).thenReturn(1);
        Mockito.when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(Mockito.mock(WorkflowModel.class));
        Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, Mockito.any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        Mockito.verify(queueDAO).postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testPostponeQuietly_queueIsPostponed() {
        String queueName = "queueName";
        String taskId = "taskId";

        asyncSystemTaskExecutor.postponeQuietly(queueName, Mockito.mock(TaskModel.class));

        Mockito.verify(queueDAO).postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskIsLoaded() {
        String taskId = "taskId";

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertSame(taskId, taskModel.getId());
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound() {
        String taskId = "taskId";

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(taskModel);
    }
}