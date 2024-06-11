package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.MetricName;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.Queue;
import com.netflix.conductor.dao.metadata.Metadata;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private Queue queue;

    @Mock
    private Metadata taskDef;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskIsNull() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, times(1)).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_taskIsTerminal() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINALED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, times(1)).remove("queueName", "taskId");
    }

    @Test
    public void testExecute_taskIsScheduled_inProgressLimitExceeded() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, times(1)).postpone("queueName", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsScheduled_rateLimitPerFrequencyExceeded() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, taskDef)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, times(1)).postpone("queueName", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsScheduled_workflowIsTerminal() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(Status.TERMINALED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, times(1)).remove("queueName", "taskId");
    }

    @Test(expected = Exception.class)
    public void testExecute_taskIsInProgress_workflowIsTerminal() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(Status.TERMINALED);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
    }

    @Test
    public void testExecute_taskIsInProgress() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, times(1)).postpone("queueName", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsTerminal_workflowIsNotTerminal() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINALED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, times(1)).remove("queueName", "taskId");
        verify(workflowExecutor, times(1)).decide(workflow.getId());
    }

    @Test
    public void testPostponeQuietly() {
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(queue);

        asyncSystemTaskExecutor.postponeQuietly("queueName", task);

        verify(queueDAO, times(1)).postpone("queueName", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskIsNull() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotNull() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        verifyNoInteractions(queueDAO);
    }
}