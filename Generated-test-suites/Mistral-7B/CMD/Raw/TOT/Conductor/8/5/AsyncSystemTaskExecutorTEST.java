import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowExecution;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.Queue;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.WorkflowDefinition;
import com.netflix.conductor.dao.WorkflowDefinitionImpl;
import com.netflix.conductor.utils.json.JacksonMapper;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    @Mock
    private Queue queue;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private WorkflowDefinition workflowDefinition;

    @Mock
    private WorkflowSystemTask systemTask;

    @Autowired
    private JacksonMapper mapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(executionDAOFacade.getTaskModel(UUID.randomUUID().toString()))
                .willReturn(task);
        given(queueDAO.getQueue(QueueUtils.getQueueName(task))).willReturn(queue);
        given(queue.getWorkflowPriority()).willReturn(1L);
        given(metadataDAO.getTaskDef(task.getTaskDefName())).willReturn(workflowDefinition);
        given(workflowDefinition.getRateLimitPerFrequency()).willReturn(1L);
        given(conductorProperties.getTaskExecutionPostponeDuration()).willReturn(1000L);
        given(conductorProperties.getSystemTaskWorkerCallbackDuration()).willReturn(1000L);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), systemTask.isTaskRetrievalRequired()))
                .willReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowExecution.Status.RUNNING);
        when(workflowExecutor.decide(workflow.getId())).thenReturn(true);
        given(queueDAO.postpone(queue.getName(), task.getTaskId(), 1L, 1000L)).willAnswer(
                invocationOnMock -> OngoingStubbing.returnValue(null));
        given(executionDAOFacade.updateTask(task)).willAnswer(invocation -> task);
    }

    @Test
    public void execute_taskIsTerminal_queueIsRemoved() {
        // Given
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Then
        verify(queueDAO).remove(queue.getName(), task.getTaskId());
    }

    @Test
    public void execute_taskIsScheduled_queueIsPostponed() {
        // Given
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Then
        verify(queueDAO).postpone(queue.getName(), task.getTaskId(), 1L, 1000L);
    }

    @Test
    public void execute_taskIsInProgress_isTaskAsyncCompleteIsFalse_taskIsExecuted() {
        // Given
        given(task.getStatus()).willReturn(TaskModel.Status.IN_PROGRESS);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Then
        verify(systemTask).start(workflow, task, workflowExecutor);
    }

    @Test
    public void execute_taskIsInProgress_isTaskAsyncCompleteIsTrue_queueIsRemoved() {
        // Given
        given(task.getStatus()).willReturn(TaskModel.Status.IN_PROGRESS);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Then
        verify(queueDAO).remove(queue.getName(), task.getTaskId());
    }

    @Test
    public void execute_taskIsTerminal_rateLimitExceeded_queueIsPostponed() {
        // Given
        given(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(task.getRateLimitPerFrequency()).thenReturn(1L);
        given(executionDAOFacade.exceedsRateLimitPerFrequency(task, workflowDefinition))
                .willReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Then
        verify(queueDAO).postpone(queue.getName(), task.getTaskId(), 1L, 1000L);
    }

    @Test
    public void execute_taskIsTerminal_inProgressLimitExceeded_queueIsPostponed() {
        // Given
        given(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        given(executionDAOFacade.exceedsInProgressLimit(task)).willReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Then
        verify(queueDAO).postpone(queue.getName(), task.getTaskId(), 1L, 1000L);
    }

    @Test
    public void execute_taskIsTerminal_workflowIsCompleted_queueIsRemoved() {
        // Given
        given(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        given(workflow.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Then
        verify(queueDAO).remove(queue.getName(), task.getTaskId());
    }

    @Test
    public void execute_taskIsTerminal_workflowIsTerminal_queueIsRemoved() {
        // Given
        given(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        given(workflow.getStatus()).thenReturn(ExecutionStatus.TERMINATED);

        // When
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        // Then
        verify(queueDAO).remove(queue.getName(), task.getTaskId());
    }
}