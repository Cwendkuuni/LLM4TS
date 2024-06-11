package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.MetadataDAOImpl;
import com.netflix.conductor.dao.ExecutionDAOFacadeImpl;
import com.netflix.conductor.dao.WorkflowExecutorMock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel("nonExistentTaskId")).thenReturn(null);

        String taskId = "nonExistentTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove("", taskId);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel("completedTaskId")).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, "completedTaskId");

        verify(queueDAO, never()).remove("", "completedTaskId");
    }

    @Test
    public void testExecute_TaskWithInProgressLimitExceeded() {
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel("inProgressTaskId")).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, "inProgressTaskId");

        verify(queueDAO, never()).postpone("", "inProgressTaskId", anyInt(), anyInt());
        verify(queueDAO).remove("", "inProgressTaskId");
    }

    @Test
    public void testExecute_TaskWithRateLimitPerFrequencyExceeded() {
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel("rateLimitedTaskId")).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowModel());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, "rateLimitedTaskId");

        verify(queueDAO, never()).postpone("", "rateLimitedTaskId", anyInt(), anyInt());
        verify(queueDAO).remove("", "rateLimitedTaskId");
    }

    @Test
    public void testExecute_TaskWithWorkflowAlreadyCompleted() {
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel("workflowCompletedTaskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("workflowId", anyBoolean())).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, "workflowCompletedTaskId");

        verify(queueDAO, never()).postpone("", "workflowCompletedTaskId", anyInt(), anyInt());
        verify(queueDAO).remove("", "workflowCompletedTaskId");
    }

    @Test
    public void testExecute_TaskWithSystemTaskCallbackTime() {
        TaskModel task = new TaskModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.getTaskDefName()).thenReturn("systemTaskDefName");
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(executionDAOFacade.getTaskModel("taskIdToBeExecuted")).thenReturn(task);

        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getWorkflowModel("workflowId", anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "taskIdToBeExecuted");

        verify(workflowExecutor).decide("workflowId");
        verify(queueDAO).postpone("", "taskIdToBeExecuted", anyInt(), anyInt());
    }

    @Test
    public void testExecute_TaskWithTaskExecutionAlreadyInProgress() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel("inProgressTaskId")).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, "inProgressTaskId");

        verify(queueDAO, never()).postpone("", "inProgressTaskId", anyInt(), anyInt());
        verify(queueDAO).remove("", "inProgressTaskId");
    }

    @Test
    public void testExecute_TaskWithAsyncCompletion() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel("asyncCompletedTaskId")).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "asyncCompletedTaskId");

        verify(queueDAO).remove("", "asyncCompletedTaskId");
    }

    @Test
    public void testExecute_TaskWithPostponedExecution() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel("postponedTaskId")).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, "postponedTaskId");

        verify(queueDAO).postpone("", "postponedTaskId", anyInt(), anyInt());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueueName";
        TaskModel task = new TaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        when(executionDAOFacade.getTaskModel("nonExistentTaskId")).thenReturn(null);

        String taskId = "nonExistentTaskId";

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(task);
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel("taskIdToBeLoaded")).thenReturn(task);

        String taskId = "taskIdToBeLoaded";

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }
}