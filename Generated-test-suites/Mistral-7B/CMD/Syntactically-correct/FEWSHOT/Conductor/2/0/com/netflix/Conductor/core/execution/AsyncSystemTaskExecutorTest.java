package com.netflix.conductor.core.execution;

import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.SYSTEM_TASK_QUEUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.workflow.WorkflowDefinition;
import com.netflix.conductor.utils.Json;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(Duration.ofSeconds(5));

        when(executionDAOFacade.getTaskModel(eq("taskId")))
                .thenReturn(createTaskModel());
        when(executionDAOFacade.getWorkflowModel(eq("workflowId"), eq(false)))
                .thenReturn(createWorkflowModel());
        when(queueDAO.getQueueName(eq(createTaskModel())))
                .thenReturn(SYSTEM_TASK_QUEUE);
        when(queueDAO.remove(eq(SYSTEM_TASK_QUEUE), eq("taskId")))
                .thenReturn(true);
        when(queueDAO.postpone(eq(SYSTEM_TASK_QUEUE), eq("taskId"), eq(1), eq(5)))
                .thenReturn(true);
        when(queueDAO.remove(eq(SYSTEM_TASK_QUEUE), eq("taskId")))
                .thenThrow(new RuntimeException("Test Exception"));

        when(workflowExecutor.decide(eq("workflowId")))
                .thenReturn(WorkflowInstanceStatus.SUCCEEDED);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecuteNullTask() {
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), null);

        verify(executionDAOFacade).updateTask(null);
        verify(queueDAO).remove(SYSTEM_TASK_QUEUE, null);
    }

    @Test
    public void testExecuteTerminalTask() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), task.getTaskId());

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(SYSTEM_TASK_QUEUE, task.getTaskId());
        verify(workflowExecutor).decide("workflowId");
    }

    @Test
    public void testExecuteScheduledTaskNoConcurrentLimit() {
        TaskModel task = createTaskModel();

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), task.getTaskId());

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(SYSTEM_TASK_QUEUE, task.getTaskId());
        verify(workflowExecutor).decide("workflowId");
    }

    @Test
    public void testExecuteScheduledTaskConcurrentLimit() {
        TaskModel task = createTaskModel();

        when(executionDAOFacade.exceedsInProgressLimit(task))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), task.getTaskId());

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).postpone(SYSTEM_TASK_QUEUE, task.getTaskId(), 1, 5);
    }

    @Test
    public void testExecuteScheduledTaskRateLimit() {
        TaskModel task = createTaskModel();
        task.setRateLimitPerFrequency(1);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, Json.parse(new StringSource("{}"))))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), task.getTaskId());

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).postpone(SYSTEM_TASK_QUEUE, task.getTaskId(), 1, 5);
    }

    @Test
    public void testExecuteInProgressTask() {
        TaskModel task = createTaskModel();

        asyncSystemTaskExecutor.execute(new WorkflowSystemTaskImpl(task), task.getTaskId());

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(SYSTEM_TASK_QUEUE, task.getTaskId());
        verify(workflowExecutor).decide("workflowId");
    }

    @Test
    public void testExecuteAsyncCompleteTask() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(SYSTEM_TASK_QUEUE, task.getTaskId());
        verify(workflowExecutor).decide("workflowId");
    }

    @Test(expected = RuntimeException.class)
    public void testQueueDAORemoveException() {
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");
    }

    private TaskModel createTaskModel() {
        return new TaskModel() {
            @Override
            public String getTaskId() {
                return "taskId";
            }

            @Override
            public String getTaskType() {
                return "taskType";
            }

            @Override
            public String getTaskDefName() {
                return "taskDefName";
            }

            @Override
            public String getWorkflowInstanceId() {
                return "workflowId";
            }

            @Override
            public ExecutionStatus getStatus() {
                return ExecutionStatus.SCHEDULED;
            }

            @Override
            public long getPriority() {
                return 1;
            }

            @Override
            public long getCallbackAfterSeconds() {
                return 0;
            }

            @Override
            public Instant getStartTime() {
                return null;
            }

            @Override
            public Instant getEndTime() {
                return null;
            }

            @Override
            public String getQueueName() {
                return SYSTEM_TASK_QUEUE;
            }

            @Override
            public Instant getCreatedAt() {
                return Instant.now();
            }

            @Override
            public Instant getUpdatedAt() {
                return Instant.now();
            }

            @Override
            public long getPollCount() {
                return 0;
            }
        };
    }

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel() {
            @Override
            public String getWorkflowId() {
                return "workflowId";
            }

            @Override
            public String getName() {
                return "workflowName";
            }

            @Override
            public String getDefinition() {
                return Json.parse(new StringSource("{}"));
            }

            @Override
            public String getStatus() {
                return WorkflowInstanceStatus.RUNNING.toString();
            }

            @Override
            public ExecutionStatus getExecutionStatus() {
                return ExecutionStatus.RUNNING;
            }

            @Override
            public Optional<WorkflowDefinition> getDefinitionOpt() {
                return Optional.of(new WorkflowDefinition());
            }
        };
    }
}