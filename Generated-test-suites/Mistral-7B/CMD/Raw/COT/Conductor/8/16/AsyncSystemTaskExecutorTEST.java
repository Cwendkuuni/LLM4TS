package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueAction;
import com.netflix.conductor.utils.test.TestUtils;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @Autowired
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask systemTask;
    private TaskModel task;
    private WorkflowModel workflow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        systemTask = mock(WorkflowSystemTask.class);
        task = new TaskModel();
        task.setTaskId("taskId");
        task.setStatus(ExecutionStatus.SCHEDULED);
        workflow = new WorkflowModel();
        workflow.setWorkflowInstanceId("workflowId");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(Optional.of(task));
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowInstanceId(), true))
                .thenReturn(workflow);
        when(queueDAO.postpone(anyString(), eq(task.getTaskId()), eq(workflow.getWorkflowPriority()),
                anyLong())).thenReturn(null);
        when(queueDAO.remove(anyString(), eq(task.getTaskId()))).thenReturn(null);
        when(queueDAO.postpone(anyString(), eq(task.getTaskId()), eq(workflow.getWorkflowPriority()),
                eq(queueTaskMessagePostponeSecs)))
                .thenReturn(true);
        when(queueDAO.getQueueSize(anyString())).thenReturn(0L);
        when(metadataDAO.getTaskDef(eq("taskDefName"))).thenReturn(new TaskDefinition());
        when(workflowExecutor.decide(eq("workflowId"))).thenReturn(null);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "nonExistentTaskId";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO, times(0)).postpone(anyString(), eq(taskId), anyLong(), anyLong());
        verify(queueDAO, times(0)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_taskIsTerminal() {
        task.setStatus(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(executionDAOFacade).getTaskModel(task.getTaskId());
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(task.getTaskId()));
    }

    @Test
    public void testExecute_taskIsScheduledAndExceedsInProgressLimit() {
        when(queueDAO.getQueueSize(QueueUtils.getQueueName(task))).thenReturn(1L);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(eq(QueueUtils.getQueueName(task)), eq(task.getTaskId()),
                eq(workflow.getWorkflowPriority()), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_taskIsScheduledAndExceedsRateLimitPerFrequency() {
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new TaskDefinition());
        when(task.getRateLimitPerFrequency()).thenReturn(10L);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(eq(QueueUtils.getQueueName(task)), eq(task.getTaskId()),
                eq(workflow.getWorkflowPriority()), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_taskIsInProgress() {
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(executionDAOFacade).getTaskModel(task.getTaskId());
        verify(systemTask).execute(eq(workflow), eq(task), eq(workflowExecutor));
        verify(queueDAO).postpone(eq(QueueUtils.getQueueName(task)), eq(task.getTaskId()),
                eq(workflow.getWorkflowPriority()), eq(queueTaskMessagePostponeSecs));
    }

    @Test(expected = Exception.class)
    public void testExecute_exceptionThrown() {
        doThrow(new RuntimeException("Test exception")).when(systemTask).execute(workflow, task,
                workflowExecutor);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    }

    @Test
    public void testPostponeQuietly_queueFailure() {
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO).postpone(eq("queueName"), eq(task.getTaskId()), anyLong(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        verify(executionDAOFacade).getTaskModel("taskId");
    }
}

class TaskDefinition {

}