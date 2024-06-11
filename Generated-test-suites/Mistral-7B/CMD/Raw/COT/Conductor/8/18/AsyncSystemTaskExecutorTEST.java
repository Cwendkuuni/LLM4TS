package com.netflix.conductor.core.execution.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAOImpl queueDAOImpl;
    private Monitors monitors;

    @Before
    public void setup() {
        this.executionDAOFacade = mock(ExecutionDAOFacade.class);
        this.queueDAO = mock(QueueDAO.class);
        this.metadataDAO = mock(MetadataDAO.class);
        this.conductorProperties = mock(ConductorProperties.class);
        this.workflowExecutor = mock(WorkflowExecutor.class);
        this.queueDAOImpl = new QueueDAOImpl();
        this.monitors = mock(Monitors.class);

        when(this.conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                Mockito.mock(org.springframework.core.io.support.EncodedResource.class));
        when(this.conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                Mockito.mock(org.springframework.core.io.support.EncodedResource.class));

        this.executor = new AsyncSystemTaskExecutor(
                this.executionDAOFacade,
                this.queueDAO,
                this.metadataDAO,
                this.conductorProperties,
                this.workflowExecutor);
    }

    @Test
    public void testExecute_taskDoesNotExist() {
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "12345";
        this.executor.execute(systemTask, taskId);

        verify(this.executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(this.queueDAO, never()).postpone(anyString(), anyString(), anyLong(), anyLong());
        verify(this.queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskExists_inProgress() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "12345";
        when(this.executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(this.executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        this.executor.execute(systemTask, taskId);

        verify(this.executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(this.queueDAO, never()).postpone(anyString(), anyString(), anyLong(), anyLong());
        verify(this.queueDAO, never()).remove(anyString(), anyString());

        verify(this.task, times(1)).setStatus(TaskModel.Status.IN_PROGRESS);
    }

    @Test
    public void testExecute_taskExists_completed() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "12345";
        when(this.executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(this.executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        this.executor.execute(systemTask, taskId);

        verify(this.executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(this.queueDAO, never()).postpone(anyString(), anyString(), anyLong(), anyLong());
        verify(this.queueDAO, times(1)).remove(anyString(), taskId);

        verify(this.task, times(1)).setStatus(TaskModel.Status.TERMINAL);
    }

    @Test(expected = Exception.class)
    public void testExecute_workflowCompleted_exceptionOnExecute()
            throws Exception {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "12345";
        when(this.executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(this.executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(systemTask.execute(workflow, task, this.workflowExecutor)).thenThrow(new Exception());

        this.executor.execute(systemTask, taskId);

        verify(this.executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(this.queueDAO, times(1)).remove(anyString(), taskId);
        verify(this.executionDAOFacade, times(1)).updateTask(task);
        verify(this.workflowExecutor, times(1)).decide(workflow.getId());
    }

    @Test
    public void testExecute_taskExists_rateLimit() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "12345";
        when(this.executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        when(task.getRateLimitPerFrequency()).thenReturn(10L);
        when(this.metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowModel());

        this.executor.execute(systemTask, taskId);

        verify(this.executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(this.queueDAO, times(1)).postpone(anyString(), taskId, anyLong(), anyLong());

        verify(this.task, never()).setStatus(TaskModel.Status.IN_PROGRESS);
        verify(this.task, never()).setStatus(TaskModel.Status.TERMINAL);
    }

    @Test
    public void testExecute_taskExists_concurrentLimit() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "12345";
        when(this.executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        when(task.getConcurrentLimit()).thenReturn(1L);
        when(this.executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        this.executor.execute(systemTask, taskId);

        verify(this.executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(this.queueDAO, times(1)).postpone(anyString(), taskId, anyLong(), anyLong());

        verify(this.task, never()).setStatus(TaskModel.Status.IN_PROGRESS);
        verify(this.task, never()).setStatus(TaskModel.Status.TERMINAL);
    }

    @Test
    public void testPostponeQuietly_queueExists() {
        String queueName = "testQueue";
        TaskModel task = Mockito.mock(TaskModel.class);

        this.executor.postponeQuietly(queueName, task);

        verify(this.queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(),
                this.queueTaskMessagePostponeSecs);
    }

    @Test
    public void testPostponeQuietly_queueDoesNotExist() {
        String queueName = "testQueue";
        TaskModel task = Mockito.mock(TaskModel.class);

        this.executor.postponeQuietly(queueName, task);

        verify(this.queueDAO, never()).postpone(queueName, task.getTaskId(), anyLong(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskExists() {
        String taskId = "12345";
        TaskModel task = Mockito.mock(TaskModel.class);

        when(this.executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = this.executor.loadTaskQuietly(taskId);

        verify(this.executionDAOFacade, times(1)).getTaskModel(taskId);
        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskDoesNotExist() {
        String taskId = "12345";

        when(this.executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTask = this.executor.loadTaskQuietly(taskId);

        verify(this.executionDAOFacade, times(1)).getTaskModel(taskId);
        assertNull(loadedTask);
    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(this.executionDAOFacade);
        verifyNoMoreInteractions(this.queueDAO);
        verifyNoMoreInteractions(this.metadataDAO);
        verifyNoMoreInteractions(this.workflowExecutor);
        verifyNoMoreInteractions(this.monitors);
    }
}