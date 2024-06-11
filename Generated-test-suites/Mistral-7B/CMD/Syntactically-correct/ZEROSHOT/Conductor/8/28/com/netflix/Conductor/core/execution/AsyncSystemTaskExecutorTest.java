package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO.QueueName;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.serialization.Json;

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

    @Mock
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(mock(Duration.class));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(mock(Duration.class));
        when(metadataDAO.getTaskDef(anyString()))
                .thenReturn(mock(WorkflowModel.class));
    }

    @After
    public void teardown() {
        reset(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_NullTaskId_ShouldReturn() {
        asyncSystemTaskExecutor.execute(systemTask, null);
        verify(executionDAOFacade, never()).getTaskModel(null);
    }

    @Test
    public void testExecute_NullSystemTask_ShouldReturn() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(null, taskId);
        verify(executionDAOFacade, never()).getTaskModel(taskId);
    }

    @Test
    public void testExecute_TaskNotFound_ShouldReturn() {
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO, never()).remove(QueueName.SYSTEM, taskId);
    }

    @Test
    public void testExecute_TerminalTask_ShouldRemoveTaskFromQueue() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId));
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskInProgress_ShouldUpdateTaskAndCallbackAfterSeconds() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId));
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).postpone(queueName, taskId, 0, anyLong());
    }

    @Test
    public void testExecute_TaskScheduled_ShouldPostponeTask() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId));
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).postpone(queueName, taskId, 0, anyLong());
    }

    @Test
    public void testExecute_TaskCompletedAsynchronously_ShouldRemoveTaskFromQueue() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId));
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskRateLimitExceeded_ShouldPostponeTask() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId));
        String taskDefName = "taskDefName";
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.SCHEDULED);
        when(task.getRateLimitPerFrequency()).thenReturn(1L);
        when(metadataDAO.getTaskDef(taskDefName)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).postpone(queueName, taskId, 0, anyLong());
    }

    @Test
    public void testExecute_TaskExecutionLimitExceeded_ShouldPostponeTask() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId));
        String taskDefName = "taskDefName";
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.SCHEDULED);
        when(task.getConcurrentExecutionLimit()).thenReturn(1L);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).postpone(queueName, taskId, 0, anyLong());
    }

    @Test
    public void testExecute_AsyncTaskExecutionCompleted_ShouldDecideWorkflowState() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId));

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(workflowExecutor).decide(workflowId);
    }
}