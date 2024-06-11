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
import com.netflix.conductor.dao.QueueNameUtil;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
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

    private TaskModel task;

    private String queueName;

    private Instant currentTime;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        currentTime = Instant.now();
        queueName = QueueNameUtil.getQueueName("queueName", 1L);
        task = new TaskModel();
        task.setTaskId("taskId");
        task.setQueueName(queueName);
        task.setTaskType("taskType");
        task.setWorkflowInstanceId("workflowInstanceId");
        task.setStatus(TaskModel.Status.SCHEDULED);
    }

    @Test
    public void testExecuteNullTask() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, never()).remove("queueName", "taskId");
        verify(queueDAO, never()).postpone(queueName, "taskId", 1L, 0L);
    }

    @Test
    public void testExecuteTaskAlreadyCompleted() {
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("workflowInstanceId", false)).thenReturn(new WorkflowModel());
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).remove("queueName", "taskId");
    }

    @Test
    public void testExecuteTaskInProgress() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("workflowInstanceId", false)).thenReturn(new WorkflowModel());
        when(workflowExecutor.decide("workflowInstanceId")).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).remove("queueName", "taskId");
    }

    @Test
    public void testExecuteTaskPostponed() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("workflowInstanceId", false)).thenReturn(new WorkflowModel());
        when(workflowExecutor.decide("workflowInstanceId")).thenReturn(false);
        doAnswer(invocation -> {
            QueueModel queueModel = invocation.getArgument(0);
            queueModel.setLastExecutionTime(currentTime.plus(Duration.ofSeconds(queueTaskMessagePostponeSecs)));
            return null;
        }).when(queueDAO).postpone(queueName, "taskId", 1L, queueTaskMessagePostponeSecs);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(queueName, "taskId", 1L, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteTaskExceedsInProgressLimit() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("taskDefName");
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(queueName, "taskId", 1L, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteTaskExceedsRateLimitPerFrequency() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("taskDefName");
        task.setRateLimitPerFrequency(10);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(metadataDAO.getTaskDef("taskDefName")).thenReturn(new TaskDefinitionModel());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, new TaskDefinitionModel())).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(queueName, "taskId", 1L, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecuteTaskNotInScheduleOrAsyncCompleteState() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(workflowExecutor.isTaskAsyncComplete("taskId", task)).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).postpone(queueName, "taskId", 1L, systemTaskCallbackTime);
    }

    @Test
    public void testExecuteTaskAsyncCompleteInNonScheduledState() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(workflowExecutor.isTaskAsyncComplete("taskId", task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).remove("queueName", "taskId");
    }

    @Test
    public void testExecuteTaskAsyncCompleteInScheduledState() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        when(workflowExecutor.isTaskAsyncComplete("taskId", task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).remove("queueName", "taskId");
    }

    @Test
    public void testExecuteTaskTerminalState() {
        task.setStatus(TaskModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
        verify(queueDAO, times(1)).remove("queueName", "taskId");
    }
}