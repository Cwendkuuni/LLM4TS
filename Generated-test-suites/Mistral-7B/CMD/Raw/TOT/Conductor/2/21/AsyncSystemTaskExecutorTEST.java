package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.utils.Json;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteNullTask() {
        when(executionDAOFacade.getTaskModel("nonExistentTaskId"))
                .thenReturn(null);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "nonExistentTaskId");

        verify(queueDAO, times(1)).remove(eq("taskQueue"), eq("nonExistentTaskId"));
    }

    @Test
    public void testExecuteTerminalTask() {
        TaskModel terminalTask = createTaskModel("terminalTaskId", true);
        when(executionDAOFacade.getTaskModel("terminalTaskId")).thenReturn(terminalTask);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "terminalTaskId");

        verify(queueDAO, times(1)).remove(eq("taskQueue"), eq("terminalTaskId"));
    }

    @Test
    public void testExecuteWorkflowCompleted() {
        TaskModel task = createTaskModel("workflowCompletedTaskId", false);
        WorkflowModel workflow = createWorkflowModel("workflowCompletedTaskId");

        when(executionDAOFacade.getTaskModel("workflowCompletedTaskId"))
                .thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(eq("workflowId"), anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "workflowCompletedTaskId");

        verify(queueDAO, times(1)).remove("taskQueue", "workflowCompletedTaskId");
        verify(workflowExecutor, times(1)).decide("workflowId");
    }

    @Test
    public void testExecuteWorkflowNotCompleted() {
        TaskModel task = createTaskModel("workflowNotCompletedTaskId", false);
        WorkflowModel workflow = createWorkflowModel("workflowNotCompletedTaskId");

        when(executionDAOFacade.getTaskModel("workflowNotCompletedTaskId"))
                .thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(eq("workflowId"), anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "workflowNotCompletedTaskId");

        verify(queueDAO, times(1)).postpone("taskQueue", "workflowNotCompletedTaskId", anyInt(), anyLong());
    }

    @Test
    public void testExecuteWorkflowExecutionLimit() {
        TaskModel task = createTaskModel("workflowExecutionLimitTaskId", false);
        when(executionDAOFacade.getTaskModel("workflowExecutionLimitTaskId"))
                .thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "workflowExecutionLimitTaskId");

        verify(queueDAO, times(1)).postpone("taskQueue", "workflowExecutionLimitTaskId", anyInt(), anyLong());
    }

    @Test
    public void testExecuteWorkflowRateLimit() {
        TaskModel task = createTaskModel("workflowRateLimitTaskId", false);
        WorkflowModel workflow = createWorkflowModel("workflowRateLimitTaskId");

        when(executionDAOFacade.getTaskModel("workflowRateLimitTaskId"))
                .thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, eq(workflow)))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "workflowRateLimitTaskId");

        verify(queueDAO, times(1)).postpone("taskQueue", "workflowRateLimitTaskId", anyInt(), anyLong());
    }

    private TaskModel createTaskModel(String taskId, boolean isTerminal) {
        return new TaskModel()
                .setTaskId(taskId)
                .setTaskDefName("taskDefName")
                .setStatus(isTerminal ? TaskModel.Status.COMPLETED : TaskModel.Status.SCHEDULED)
                .setWorkflowInstanceId("workflowId")
                .setWorkflowPriority(1)
                .setCallbackAfterSeconds(0);
    }

    private WorkflowModel createWorkflowModel(String taskId) {
        return new WorkflowModel()
                .setId("workflowId")
                .setName("workflowName")
                .setStatus(WorkflowModel.Status.RUNNING)
                .setStartTime(Instant.now())
                .setEndTime(null);
    }

    @Test
    public void testPostponeTask() {
        String queueName = "taskQueue";
        TaskModel task = createTaskModel("postponedTaskId", false);

        when(executionDAOFacade.getTaskModel("postponedTaskId"))
                .thenReturn(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, eq("postponedTaskId"), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testLoadTaskNull() {
        when(executionDAOFacade.getTaskModel("nonExistentTaskId"))
                .thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("nonExistentTaskId");

        assertNull(loadedTask);
    }
}