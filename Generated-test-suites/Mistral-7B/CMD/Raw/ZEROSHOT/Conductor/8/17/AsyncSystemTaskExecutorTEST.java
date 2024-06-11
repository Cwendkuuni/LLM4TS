package com.netflix.conductor.core.execution;

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
import com.netflix.conductor.core.model.execution.WorkflowExecutionModel;
import com.netflix.conductor.core.model.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAOImpl;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModelImpl;
import com.netflix.conductor.dao.WorkflowModelRepository;
import com.netflix.conductor.execution.tasks.SleeperSystemTask;
import com.netflix.conductor.execution.workflow.WorkflowExecutor;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private WorkflowModelRepository workflowModelRepository;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Collections.singletonList(1000L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Collections.singletonList(500L));
        when(workflowModelRepository.getWorkflowModel(anyString())).thenReturn(new WorkflowModelImpl());
        when(workflowModelRepository.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModelImpl());

        doReturn(new SleeperSystemTask()).when(asyncSystemTaskExecutor).createSystemTask(any(String.class), anyString());
        doReturn(new TaskModel()).when(executionDAOFacade).getTaskModel(anyString());
        doReturn(new Optional<WorkflowExecutionModel>()).when(executionDAOFacade).getWorkflowExecutionModel(anyString(), anyString());
        doReturn(new Optional<WorkflowModel>()).when(workflowModelRepository).findById(anyString());
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.getQueueSize(anyString())).thenReturn(1);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new WorkflowModelImpl());
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "task-1";
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(executionDAOFacade, never()).getTaskModel(taskId);
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        WorkflowModel workflowModel = new WorkflowModelImpl().setId("workflow-1");
        TaskModel taskModel = new TaskModel().setId(taskId).setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskScheduled() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        WorkflowModel workflowModel = new WorkflowModelImpl().setId("workflow-1").setStatus(WorkflowModel.Status.RUNNING);
        TaskModel taskModel = new TaskModel().setId(taskId).setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        when(queueDAO.getQueueSize(queueName)).thenReturn(0);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).postpone(queueName, taskId, 0, 500L);
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        WorkflowModel workflowModel = new WorkflowModelImpl().setId("workflow-1").setStatus(WorkflowModel.Status.RUNNING);
        TaskModel taskModel = new TaskModel().setId(taskId).setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO, never()).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskTerminal() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        WorkflowModel workflowModel = new WorkflowModelImpl().setId("workflow-1").setStatus(WorkflowModel.Status.COMPLETED);
        TaskModel taskModel = new TaskModel().setId(taskId).setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        when(queueDAO.getQueueSize(queueName)).thenReturn(1);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskExceedsInProgressLimit() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel taskModel = new TaskModel().setId(taskId).setStatus(TaskModel.Status.SCHEDULED).setTaskDefName("testTaskDef");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO).postponeQuietly(queueName, taskModel);
    }

    @Test
    public void testExecute_TaskExceedsRateLimitPerFrequency() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel taskModel = new TaskModel().setId(taskId).setStatus(TaskModel.Status.SCHEDULED).setTaskDefName("testTaskDef");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any(MetadataDAO.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO).postponeQuietly(queueName, taskModel);
    }

    @Test
    public void testExecute_TaskUpdate() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel taskModel = new TaskModel().setId(taskId).setStatus(TaskModel.Status.IN_PROGRESS).setEndTime(System.currentTimeMillis());
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.getQueueSize(queueName)).thenReturn(0);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(executionDAOFacade).updateTask(taskModel);
    }

    @Test
    public void testExecute_WorkflowDecide() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        WorkflowModel workflowModel = new WorkflowModelImpl().setId("workflow-1").setStatus(WorkflowModel.Status.RUNNING);
        TaskModel taskModel = new TaskModel().setId(taskId).setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel taskModel = new TaskModel().setId("task-1").setStatus(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).remove(queueName, taskModel.getId());
    }
}