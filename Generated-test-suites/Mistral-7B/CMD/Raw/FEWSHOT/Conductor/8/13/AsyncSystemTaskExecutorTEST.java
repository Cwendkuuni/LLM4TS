package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

/**
 * Unit tests for AsyncSystemTaskExecutor class.
 */
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private ConductorProperties conductorProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.conductorProperties = new ConductorProperties();
        when(executionDAOFacade.getTaskModel(anyString())).thenAnswer(invocation -> {
            String taskId = invocation.getArgument(0);
            return Optional.ofNullable(new TaskModel().withId(taskId));
        });
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt())).thenReturn(true);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.get(anyString(), anyString())).thenReturn(null);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO,
                conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "non-existent-task-id";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, never()).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(queueDAO, never()).postpone(anyString(), taskId, anyInt(), anyInt());
    }

    @Test
    public void testExecute_taskTerminal() {
        String taskId = "terminal-task-id";
        TaskModel task = new TaskModel().withId(taskId).withStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = new WorkflowModel().withId("workflow-id");
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.ofNullable(task));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getWorkflowModel("workflow-id", false);
        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor, never()).decide(eq("workflow-id"));
    }

    @Test
    public void testExecute_taskScheduled() {
        String taskId = "scheduled-task-id";
        TaskModel task = new TaskModel().withId(taskId).withStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel().withId("workflow-id");
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.ofNullable(task));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getWorkflowModel("workflow-id", false);
        verify(queueDAO).postpone(anyString(), taskId, anyInt(), eq(queueTaskMessagePostponeSecs));
        verify(workflowExecutor).decide("workflow-id");
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "in-progress-task-id";
        TaskModel task = new TaskModel().withId(taskId).withStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel().withId("workflow-id");
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.ofNullable(task));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getWorkflowModel("workflow-id", false);
        verify(systemTask).execute(workflow, task, workflowExecutor);
        verify(queueDAO, never()).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskCompleted() {
        String taskId = "completed-task-id";
        TaskModel task = new TaskModel().withId(taskId).withStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = new WorkflowModel().withId("workflow-id");
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.ofNullable(task));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getWorkflowModel("workflow-id", false);
        verify(systemTask, never()).execute(workflow, task, workflowExecutor);
        verify(queueDAO).remove(anyString(), taskId);
        verify(workflowExecutor, never()).decide("workflow-id");
    }
}