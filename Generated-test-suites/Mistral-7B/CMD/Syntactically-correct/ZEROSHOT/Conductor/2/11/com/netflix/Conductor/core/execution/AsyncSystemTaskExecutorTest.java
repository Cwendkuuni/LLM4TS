package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

import static com.netflix.conductor.common.metadata.tasks.TaskDefs.WORKFLOW_SYSTEM_TASK_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@DisplayName("AsyncSystemTaskExecutor Test")
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

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .willReturn(getWorkflowModel());
    }

    @Test
    @DisplayName("Test execute method with valid WorkflowSystemTask and Task Model")
    public void testExecuteValidWorkflowSystemTaskAndTaskModel() {
        // Given
        String taskId = "taskId";
        WorkflowSystemTask systemTask = new WorkflowSystemTask("taskType", "taskName");
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean());
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(true));
        verify(executionDAOFacade, times(1)).updateTask(eq(task));
        verifyNoInteractions(queueDAO);

        // When - verify Workflow is evaluated
        verify(workflowExecutor, times(1)).decide(eq(task.getWorkflowInstanceId()));
    }

    @Test
    @DisplayName("Test execute method with null Task Id")
    public void testExecuteWithNullTaskId() {
        // Given
        WorkflowSystemTask systemTask = new WorkflowSystemTask("taskType", "taskName");

        // When & Then
        assertThrows(NullPointerException.class, () -> asyncSystemTaskExecutor.execute(systemTask, null));
        verifyNoInteractions(executionDAOFacade);
        verifyNoInteractions(queueDAO);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test execute method with existing Task")
    public void testExecuteWithExistingTask() {
        // Given
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(taskId);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);
        given(queueDAO.getSize(queueName)).willReturn(1);

        // When
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskName"), taskId);

        // Then
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean());
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(true));
        verify(executionDAOFacade, times(1)).updateTask(eq(task));
        verify(queueDAO, times(1)).remove(eq(queueName), eq(taskId));
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test execute method with non-existent Task")
    public void testExecuteWithNonExistentTask() {
        // Given
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(taskId);

        // When
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskName"), taskId);

        // Then
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(eq(queueName), eq(taskId));
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test execute method with task in progress")
    public void testExecuteWithTaskInProgress() {
        // Given
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(taskId);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        // When
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskName"), taskId);

        // Then
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean());
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(true));
        verify(executionDAOFacade, times(1)).updateTask(eq(task));
        verifyNoInteractions(queueDAO);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test execute method with task in completed state")
    public void testExecuteWithTaskInCompletedState() {
        // Given
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(taskId);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        // When
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskName"), taskId);

        // Then
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean());
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(true));
        verify(executionDAOFacade, times(1)).updateTask(eq(task));
        verifyNoInteractions(queueDAO);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test execute method with task in failed state")
    public void testExecuteWithTaskInFailedState() {
        // Given
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(taskId);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        // When
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskName"), taskId);

        // Then
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean());
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(true));
        verify(executionDAOFacade, times(1)).updateTask(eq(task));
        verifyNoInteractions(queueDAO);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test execute method with task in terminated state")
    public void testExecuteWithTaskInTerminatedState() {
        // Given
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(taskId);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.TERMINATED);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);

        // When
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskName"), taskId);

        // Then
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean());
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(true));
        verify(executionDAOFacade, times(1)).updateTask(eq(task));
        verifyNoInteractions(queueDAO);
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test execute method with queue size exceeding maximum limit")
    public void testExecuteWithQueueSizeExceedingMaxLimit() {
        // Given
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(taskId);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);
        given(queueDAO.getSize(queueName)).willReturn(100);
        given(executionDAOFacade.exceedsInProgressLimit(task)).willReturn(true);

        // When
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskName"), taskId);

        // Then
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean());
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(true));
        verify(queueDAO, times(1)).postpone(
                eq(queueName),
                eq(taskId),
                anyInt(),
                anyInt());
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test execute method with rate limit per frequency exceeded")
    public void testExecuteWithRateLimitPerFrequencyExceeded() {
        // Given
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(taskId);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflowId");
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);
        given(metadataDAO.getTaskDef(task.getTaskDefName())).willReturn(getTaskDef());
        given(queueDAO.getSize(queueName)).willReturn(0);

        // When
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("taskType", "taskName"), taskId);

        // Then
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), anyBoolean());
        verify(executionDAOFacade, times(1)).getWorkflowModel(eq(task.getWorkflowInstanceId()), eq(true));
        verify(queueDAO, times(1)).postpone(
                eq(queueName),
                eq(taskId),
                anyInt(),
                anyInt());
        verifyNoInteractions(workflowExecutor);
    }

    @Test
    @DisplayName("Test postponeQuietly method with queue name and task model")
    public void testPostponeQuietlyWithQueueNameAndTaskModel() {
        // Given
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setId("taskId");

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Then
        verify(queueDAO, times(1)).postpone(
                eq(queueName),
                eq(task.getId()),
                anyInt(),
                anyInt());
    }

    @Test
    @DisplayName("Test loadTaskQuietly method with task Id")
    public void testLoadTaskQuietlyWithTaskId() {
        // Given
        String taskId = "taskId";

        // When
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertNull(task);
        verifyNoInteractions(executionDAOFacade);
    }

    private WorkflowModel getWorkflowModel() {
        return new WorkflowModel();
    }

    private TaskDef getTaskDef() {
        return new TaskDef();
    }
}

class TaskDef {
    private final int rateLimitPerFrequency = 1;

    public int getRateLimitPerFrequency() {
        return rateLimitPerFrequency;
    }
}