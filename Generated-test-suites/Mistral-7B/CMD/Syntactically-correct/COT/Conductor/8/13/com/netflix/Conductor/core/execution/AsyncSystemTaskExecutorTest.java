package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.Queue;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueDAOException;
import com.netflix.conductor.dao.TaskDAO.TaskDAOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    private ArgumentCaptor<TaskModel> taskArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(queueDAO.get(anyString(), anyString())).thenReturn(queue);
        taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        when(workflowExecutor.decide(anyString())).thenReturn(true);
    }

    @After
    public void teardown() {
        reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void testExecute_nullTaskId() {
        asyncSystemTaskExecutor.execute(systemTask, null);
        verify(executionDAOFacade, never()).getTaskModel(anyString());
    }

    @Test
    public void testExecute_taskNotFound() {
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(executionDAOFacade, never()).updateTask(task);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_terminalWorkflow() {
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(eq(queue.getName()), eq(task.getTaskId()), anyInt(), anyLong());
    }

    @Test
    public void testExecute_scheduledTask() throws Exception {
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, workflowModel)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, times(1)).remove(anyString(), eq(task.getTaskId()));
        verify(queueDAO, times(1)).postpone(eq(queue.getName()), eq(task.getTaskId()), anyInt(), eq(queueTaskMessagePostponeSecs));
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(workflowModel.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_concurrencyLimit() throws Exception {
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), eq(task.getTaskId()));
        verify(queueDAO, times(1)).postpone(eq(queue.getName()), eq(task.getTaskId()), anyInt(), eq(queueTaskMessagePostponeSecs));
        verify(executionDAOFacade, never()).updateTask(task);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test(expected = QueueDAOException.class)
    public void testExecute_queueDAOException() throws Exception {
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(queueDAO.get(anyString(), anyString())).thenThrow(new QueueDAOException("Test QueueDAOException"));

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), eq(task.getTaskId()));
        verify(queueDAO, times(1)).postpone(eq(queue.getName()), eq(task.getTaskId()), anyInt(), eq(queueTaskMessagePostponeSecs));
        verify(executionDAOFacade, never()).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowModel.getWorkflowInstanceId());
    }

    @Test(expected = TaskDAOException.class)
    public void testExecute_taskDAOException() throws Exception {
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.getTaskModel(task.getId())).thenThrow(new TaskDAOException("Test TaskDAOException"));

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), eq(task.getTaskId()));
        verify(queueDAO, never()).postpone(anyString(), eq(task.getTaskId()), anyInt(), anyLong());
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, never()).decide(workflowModel.getWorkflowInstanceId());
    }

    @Test
    public void testPostponeQuietly() throws Exception {
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO, times(1)).postpone(eq("queueName"), eq(task.getTaskId()), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test(expected = QueueDAOException.class)
    public void testPostponeQuietly_queueDAOException() throws Exception {
        when(queueDAO.get(anyString(), anyString())).thenThrow(new QueueDAOException("Test QueueDAOException"));

        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO, never()).postpone(eq("queueName"), eq(task.getTaskId()), anyInt(), anyLong());
    }

    @Test(expected = TaskDAOException.class)
    public void testLoadTaskQuietly_taskDAOException() throws Exception {
        when(executionDAOFacade.getTaskModel(task.getId())).thenThrow(new TaskDAOException("Test TaskDAOException"));

        asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        verify(executionDAOFacade, never()).getTaskModel(task.getId());
    }
}