package com.netflix.conductor.core.execution;

import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.queueDAO;
import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.executionDAOFacade;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.WorkflowInstanceDAO;
import com.netflix.conductor.dao.WorkflowModelDAO;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.workflow.WorkflowDefinition;
import com.netflix.conductor.utils.GUIDGenerator;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowInstanceDAO workflowInstanceDAO;

    @Mock
    private WorkflowModelDAO workflowModelDAO;

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private GUIDGenerator guidGenerator;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask systemTask;

    private TaskModel task;

    private WorkflowModel workflowModel;

    private WorkflowDefinition workflowDefinition;

    private WorkflowInstance workflowInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        systemTask = mock(WorkflowSystemTask.class);
        task = mock(TaskModel.class);
        workflowModel = mock(WorkflowModel.class);
        workflowDefinition = mock(WorkflowDefinition.class);
        workflowInstance = mock(WorkflowInstance.class);
        when(workflowModel.toShortString()).thenReturn("Workflow 1");
        when(workflowInstance.getId()).thenReturn("workflow-1");
        when(workflowInstance.getWorkflowInstanceId()).thenReturn("workflow-instance-1");
        when(workflowInstance.getWorkflowDefinition()).thenReturn(workflowDefinition);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        when(workflowModel.getId()).thenReturn("workflow-model-1");
        when(workflowModelDAO.getWorkflowModel(anyString())).thenReturn(workflowModel);
        when(workflowInstanceDAO.getWorkflowInstance(anyString())).thenReturn(workflowInstance);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("test_queue");
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(2));
        when(guidGenerator.generate()).thenReturn("task-id");

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound_RemoveDeadTaskFromQueue() {
        String taskId = "task-id";
        when(task.getId()).thenReturn(taskId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("test_queue"), eq(taskId));
    }

    @Test
    public void testExecute_TaskFound_ExecuteTask() {
        String taskId = "task-id";
        when(task.getId()).thenReturn(taskId);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("workflow-instance-id");

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, times(1)).decide(eq("workflow-instance-id"));
    }

    @Test
    public void testExecute_TaskNotFound_QueuePostponement() {
        String taskId = "task-id";
        when(task.getId()).thenReturn(taskId);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getWorkflowInstanceId()).thenReturn("workflow-instance-id");

        doThrow(new Exception("Test Exception")).when(workflowExecutor).decide(eq("workflow-instance-id"));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq("test_queue"), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted_RemoveTaskFromQueue() {
        String taskId = "task-id";
        when(task.getId()).thenReturn(taskId);
        when(task.getStatus()).thenReturn(TaskModel.Status.CANCELED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("test_queue"), eq(taskId));
    }

    @Test
    public void testExecute_TaskInProgress_Callback() {
        String taskId = "task-id";
        when(task.getId()).thenReturn(taskId);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(task.getCallbackAfterSeconds()).thenReturn(1L);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq("test_queue"), eq(taskId), anyInt(), eq(1L));
    }

    @Test
    public void testExecute_AsyncTaskComplete_RemoveTaskFromQueue() {
        String taskId = "task-id";
        when(task.getId()).thenReturn(taskId);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(task.getEndTime()).thenReturn(Instant.now().getEpochSecond());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(eq("test_queue"), eq(taskId));
    }

    @Test
    public void testExecute_AsyncTaskComplete_UpdateWorkflow() {
        String taskId = "task-id";
        String workflowInstanceId = "workflow-instance-id";

        when(task.getId()).thenReturn(taskId);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(task.getEndTime()).thenReturn(Instant.now().getEpochSecond());
        when(task.getWorkflowInstanceId()).thenReturn(workflowInstanceId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, times(1)).decide(eq(workflowInstanceId));
    }

    @Test
    public void testExecute_AsyncTaskFailed_ErrorLogging() {
        String taskId = "task-id";
        String workflowInstanceId = "workflow-instance-id";

        when(task.getId()).thenReturn(taskId);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(task.getEndTime()).thenReturn(Instant.now().getEpochSecond());
        when(task.getWorkflowInstanceId()).thenReturn(workflowInstanceId);

        doThrow(new Exception("Test Exception")).when(systemTask).isAsyncComplete(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(Monitors, times(1)).error(eq(AsyncSystemTaskExecutor.class.getName()), eq("executeSystemTask"));
        verify(Logger.class, times(1)).error(
                "Error executing system task - {}, with id: {}"
                        , eq(systemTask), eq(taskId), eq(any(Exception.class)));
    }

    @Test
    public void testExecute_TaskExceedsInProgressLimit_QueuePostponement() {
        String taskId = "task-id";
        String workflowInstanceId = "workflow-instance-id";

        when(task.getId()).thenReturn(taskId);
        when(workflowModel.getId()).thenReturn("workflow-model-id");
        when(workflowModel.getConcurrencyLimit()).thenReturn(1);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq("test_queue"), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskExceedsRateLimit_QueuePostponement() {
        String taskId = "task-id";
        String workflowInstanceId = "workflow-instance-id";

        when(task.getId()).thenReturn(taskId);
        when(workflowModel.getId()).thenReturn("workflow-model-id");
        when(workflowModel.getConcurrencyLimit()).thenReturn(1);
        when(workflowModel.getTaskDefinitions()).thenReturn(new TaskDefinition[]{new TaskDefinition()});
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(task.getRateLimitPerFrequency()).thenReturn(1);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq("test_queue"), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskConcurrentlyExecuting_QueuePostponement() {
        String taskId = "task-id";
        String workflowInstanceId = "workflow-instance-id";

        when(task.getId()).thenReturn(taskId);
        when(workflowModel.getId()).thenReturn("workflow-model-id");
        when(workflowModel.getConcurrencyLimit()).thenReturn(1);
        when(workflowModel.getTaskDefinitions()).thenReturn(new TaskDefinition[]{new TaskDefinition()});
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq("test_queue"), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskNotConcurrentlyExecuting_ExecuteTask() {
        String taskId = "task-id";
        String workflowInstanceId = "workflow-instance-id";

        when(task.getId()).thenReturn(taskId);
        when(workflowModel.getId()).thenReturn("workflow-model-id");
        when(workflowModel.getConcurrencyLimit()).thenReturn(1);
        when(workflowModel.getTaskDefinitions()).thenReturn(new TaskDefinition[]{new TaskDefinition()});
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, times(1)).decide(eq(workflowInstanceId));
    }
}

class TaskDefinition {}