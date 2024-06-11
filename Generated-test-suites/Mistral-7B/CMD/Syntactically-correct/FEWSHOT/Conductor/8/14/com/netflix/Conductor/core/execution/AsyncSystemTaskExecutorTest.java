package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

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
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    private String queueName;
    private String taskId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        queueName = QueueUtils.getQueueName(taskModel);
        taskId = taskModel.getId();
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verifyNoInteractions(queueDAO);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_terminalTask() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_scheduledTaskWithExecutableTaskDef() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(workflowModel.getId(), false)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        when(queueDAO.getQueueSize(queueName)).thenReturn(1L);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verify(workflowExecutor, times(1)).decide(workflowModel.getId());
    }

    @Test
    public void testExecute_scheduledTaskWithInExecutableTaskDef() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(workflowModel.getId(), false)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        when(queueDAO.getQueueSize(queueName)).thenReturn(1L);

        doThrow(new RuntimeException("Task execution failed")).when(workflowSystemTask)
                .execute(workflowModel, taskModel, workflowExecutor);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(),
                asyncSystemTaskExecutor.getSystemTaskCallbackTime() + TimeUnit.SECONDS.toMillis(10));
    }

    @Test
    public void testExecute_inProgressTask() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskLimitReached() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_rateLimitReached() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_workflowTerminal() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(workflowModel.getId(), false)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.TERMINATED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_asyncTaskCompleted() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_asyncTaskInProgress() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
        verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(),
                asyncSystemTaskExecutor.getSystemTaskCallbackTime());
    }

    @Test
    public void testExecute_asyncTaskTerminal() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.TERMINATED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
        verify(workflowExecutor, times(1)).decide(workflowModel.getId());
    }

    @Test
    public void testExecute_systemTaskCallbackTime() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
        ConductorProperties conductorPropertiesSpy = spy(conductorProperties);
        doReturn(2000L).when(conductorPropertiesSpy).getSystemTaskWorkerCallbackDuration();
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorPropertiesSpy,
                workflowExecutor);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(), 2000);
    }

    @Test
    public void testPostponeQuietly() {
        when(queueDAO.getQueueSize(queueName)).thenReturn(1L);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskModel, result);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}