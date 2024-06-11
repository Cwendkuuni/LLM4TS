package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.QueueDAO.QueueAction;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.test.TestConfig;

@TestConfig
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
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private Queue queue;

    private String queueName;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(metadataDAO.getTaskDef(any())).thenReturn(mock(TaskModel.class));

        queueName = QueueUtils.getQueueName(task);
        when(queueDAO.get(queueName, task.getTaskId())).thenReturn(Optional.of(queue));
        when(queueDAO.postpone(
                anyString(), anyString(), anyInt(), anyLong())).thenAnswer(i -> null);
        when(queueDAO.remove(queueName, task.getTaskId())).thenAnswer(i -> null);

        when(executionDAOFacade.getWorkflowModel(
                anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                task, any())).thenReturn(false);
        when(executionDAOFacade.updateTask(task)).thenAnswer(i -> null);

        when(workflowExecutor.decide(anyString())).thenAnswer(i -> null);
    }

    @Test
    public void executeTask_taskIsAlreadyCompleted_queueIsRemoved() {
        TaskModel completedTask = new TaskModel();
        completedTask.setStatus(TaskModel.Status.COMPLETED);
        completedTask.setTaskDefName("testTaskDef");
        completedTask.setTaskId("testTaskId");
        completedTask.setWorkflowInstanceId("testWorkflowInstanceId");

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(Optional.of(completedTask));

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO).remove(queueName, "testTaskId");
    }

    @Test
    public void executeTask_taskExecutionFails_queueIsNotRemoved() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException("Test Failure"));

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, never()).remove(queueName, "testTaskId");
    }

    @Test
    public void executeTask_taskIsInProgress_queueIsPostponed() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO).postpone(queueName, "testTaskId", 1, systemTaskCallbackTime);
    }

    @Test
    public void executeTask_taskIsScheduled_queueIsPostponed() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO).postpone(queueName, "testTaskId", 1, queueTaskMessagePostponeSecs);
    }

    @Test
    public void executeTask_taskIsTerminal_queueIsRemoved() {
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO).remove(queueName, "testTaskId");
    }

    @Test
    public void executeTask_taskExecutionIsAsyncComplete_queueIsRemoved() {
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO).remove(queueName, "testTaskId");
    }

    @Test
    public void executeTask_workflowCompletion_queueIsDecided() {
        when(task.getWorkflowInstanceId()).thenReturn("testWorkflowInstanceId");

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(workflowExecutor).decide("testWorkflowInstanceId");
    }
}