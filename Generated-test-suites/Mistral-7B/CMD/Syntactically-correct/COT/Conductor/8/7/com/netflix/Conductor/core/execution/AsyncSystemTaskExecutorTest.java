package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.WorkflowInstanceNotFoundException;
import com.netflix.conductor.metrics.Monitors;

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

    private TaskModel task;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(anyString())).thenAnswer(new Answer<TaskModel>() {
            @Override
            public TaskModel answer(InvocationOnMock invocation) throws Throwable {
                return task;
            }
        });
        when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("queueName");
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition("taskDefName"));
        when(workflowExecutor.decide(anyString())).thenReturn(true);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskIsNull() {
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_taskIsCompleted() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskIsTerminal() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).remove(eq("queueName"), eq("taskId"));
    }

    @Test
    public void testExecute_taskIsScheduledAndConcurrentLimitIsExceeded() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO).postpone(eq("queueName"), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsScheduledAndRateLimitIsExceeded() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getWorkflowModel(eq("workflowId"), anyBoolean())).thenReturn(workflow);
        when(workflow.getRateLimitPerFrequency()).thenReturn(1L);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, workflow)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO).postpone(eq("queueName"), eq("taskId"), anyInt(), anyLong());
    }

    @Test(expected = WorkflowInstanceNotFoundException.class)
    public void testExecute_workflowNotFound() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getWorkflowModel(eq("workflowId"), anyBoolean())).thenThrow(new WorkflowInstanceNotFoundException("workflowId"));
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
    }

    @Test
    public void testExecute_taskIsInProgress() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(systemTask).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void testExecute_taskIsInProgressAndAsyncComplete() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).remove(eq("queueName"), eq("taskId"));
    }

    @Test
    public void testExecute_taskIsInProgressAndNotAsyncComplete() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).postpone(eq("queueName"), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testExecute_taskIsTerminalAndHasBeenUpdated() {
        task = new TaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(queueDAO).remove(eq("queueName"), eq("taskId"));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testPostponeQuietly_queueIsNull() {
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly_queueIsNotEmpty() {
        when(queueDAO.getSize(anyString())).thenReturn(1);
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);
        verify(queueDAO).postpone(eq("queueName"), eq("taskId"), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound() {
        asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        verify(executionDAOFacade, never()).getTaskModel(anyString());
    }

    @Test
    public void testLoadTaskQuietly_taskIsFound() {
        task = new TaskModel();
        asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        verify(executionDAOFacade).getTaskModel("taskId");
    }
}

class TaskDefinition {
    private String taskDefName;

    public TaskDefinition(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    public String getTaskDefName() {
        return taskDefName;
    }
}