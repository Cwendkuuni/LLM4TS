package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "task-1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.empty());

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertTrue(executionDAOFacade.getTaskModel(taskId).isPresent());
        assertFalse(queueDAO.contains(anyString(), eq(taskId)));
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = "queue-1";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowModel.Status.COMPLETED);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(WorkflowModel.Status.COMPLETED, workflow.getStatus());
        assertFalse(queueDAO.contains(anyString(), eq(taskId)));
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_exceedsInProgressLimit() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = "queue-1";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(WorkflowModel.Status.IN_PROGRESS, workflow.getStatus());
        assertFalse(queueDAO.contains(anyString(), eq(taskId)));

        doThrow(new RuntimeException("Test exception")).when(executionDAOFacade).exceedsInProgressLimit(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_exceedsRateLimitPerFrequency() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = "queue-1";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(metadataDAO.getTaskDef(anyString())).thenReturn(new Object());

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenThrow(new RuntimeException("Test exception"));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(WorkflowModel.Status.IN_PROGRESS, workflow.getStatus());
        assertFalse(queueDAO.contains(anyString(), eq(taskId)));
    }

    @Test
    public void testExecute_isTaskAsyncComplete() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = "queue-1";
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(WorkflowModel.Status.IN_PROGRESS, workflow.getStatus());
        assertFalse(queueDAO.contains(anyString(), eq(taskId)));
    }
}