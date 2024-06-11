package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("deprecation")
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
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = null;

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskInTerminalState() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_TaskInScheduledState_RateLimitExceeded() {
        String taskId = "taskId";
        String taskDefName = "taskDefName";
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(metadataDAO.getTaskDef(taskDefName)).thenReturn(mock(TaskDefinition.class));
        when(task.getTaskExecutionPostponeDuration()).thenReturn(1L);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), eq(1L));
    }

    @Test
    public void testExecute_TaskInScheduledState_InProgressLimitExceeded() {
        String taskId = "taskId";
        String taskDefName = "taskDefName";
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), eq(1L));
    }

    @Test
    public void testExecute_TaskInScheduledState_ExecuteTask() {
        String taskId = "taskId";
        String taskDefName = "taskDefName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefinitionName(taskDefName);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        doNothing().when(systemTask).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), eq(1L));
        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void testExecute_TaskInProgressState_ExecuteTask() {
        String taskId = "taskId";
        String taskDefName = "taskDefName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskDefinitionName(taskDefName);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        doNothing().when(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), eq(1L));
        verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void testExecute_TaskInProgressState_AsyncComplete() {
        String taskId = "taskId";
        String taskDefName = "taskDefName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskDefinitionName(taskDefName);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_TaskInProgressState_AsyncCompleteAndNoQueue() {
        String taskId = "taskId";
        String taskDefName = "taskDefName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskDefinitionName(taskDefName);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        QueueDAO queueDAO = mock(QueueDAO.class);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(systemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void testExecute_TaskTerminalState() {
        String taskId = "taskId";
        String taskDefName = "taskDefName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.FAILED);
        task.setTaskDefinitionName(taskDefName);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_TaskTerminalState_WorkflowTerminal() {
        String taskId = "taskId";
        String taskDefName = "taskDefName";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.FAILED);
        task.setTaskDefinitionName(taskDefName);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(workflowExecutor, times(1)).decide(workflow.getId());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, eq(task.getTaskId()), anyInt(), eq(1L));
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertNull(loadedTask);
    }
}