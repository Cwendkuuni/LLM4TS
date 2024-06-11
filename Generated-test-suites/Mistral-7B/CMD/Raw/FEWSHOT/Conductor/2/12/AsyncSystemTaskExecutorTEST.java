package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueueName;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String TASK_ID = "taskId";
    private static final String TASK_TYPE = "taskType";
    private static final String WORKFLOW_DEF_NAME = "workflowDefName";
    private static final String QUEUE_NAME = "queueName";
    private static final long SYSTEM_TASK_CALLBACK_TIME = 1000L;
    private static final long TASK_EXECUTION_POSTPONE_DURATION = 5000L;
    private static final WorkflowStatus WORKFLOW_STATUS = WorkflowStatus.COMPLETED;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade mockExecutionDAOFacade;
    private QueueDAO mockQueueDAO;
    private MetadataDAO mockMetadataDAO;
    private ConductorProperties mockConductorProperties;
    private WorkflowExecutor mockWorkflowExecutor;

    @Before
    public void setUp() {
        mockConductorProperties = mock(ConductorProperties.class);
        when(mockConductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(new Duration(SYSTEM_TASK_CALLBACK_TIME, TimeUnit.SECONDS));
        when(mockConductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(new Duration(TASK_EXECUTION_POSTPONE_DURATION, TimeUnit.SECONDS));

        mockExecutionDAOFacade = mock(ExecutionDAOFacade.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockMetadataDAO = mock(MetadataDAO.class);
        mockWorkflowExecutor = mock(WorkflowExecutor.class);

        AsyncSystemTaskExecutorTest.this.asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        mockExecutionDAOFacade,
                        mockQueueDAO,
                        mockMetadataDAO,
                        mockConductorProperties,
                        mockWorkflowExecutor);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_whenTaskIsNotFound_shouldRemoveTaskFromQueue() {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(taskModel.getId()).thenReturn(TASK_ID);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(mockQueueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_whenWorkflowIsCompleted_shouldRemoveTaskFromQueue() {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(mockExecutionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowModel);
        when(taskModel.getId()).thenReturn(TASK_ID);
        when(workflowModel.getStatus()).thenReturn(WORKFLOW_STATUS);
        when(workflowModel.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(mockQueueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_whenExceedsConcurrentExecutionLimit_shouldPostponeTask() {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(taskModel.getId()).thenReturn(TASK_ID);
        when(taskModel.getTaskDefName()).thenReturn(WORKFLOW_DEF_NAME);
        when(mockExecutionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(mockQueueDAO).postpone(QUEUE_NAME, taskModelCaptor.capture(), anyInt(), anyLong());
        TaskModel capturedTaskModel = taskModelCaptor.getValue();
        assertEquals(TASK_ID, capturedTaskModel.getId());
    }

    @Test
    public void testExecute_whenExceedsRateLimitPerFrequency_shouldPostponeTask() {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(taskModel.getId()).thenReturn(TASK_ID);
        when(taskModel.getTaskDefName()).thenReturn(WORKFLOW_DEF_NAME);
        when(mockExecutionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(mockQueueDAO).postpone(QUEUE_NAME, taskModelCaptor.capture(), anyInt(), anyLong());
        TaskModel capturedTaskModel = taskModelCaptor.getValue();
        assertEquals(TASK_ID, capturedTaskModel.getId());
    }

    @Test
    public void testExecute_whenTaskIsScheduled_shouldExecuteTaskAndUpdateTaskStatus() {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(mockExecutionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowModel);
        when(taskModel.getId()).thenReturn(TASK_ID);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(workflowModel.getStatus()).thenReturn(WorkflowStatus.RUNNING);

        ArgumentCaptor<WorkflowSystemTask> workflowSystemTaskCaptor =
                ArgumentCaptor.forClass(WorkflowSystemTask.class);
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(workflowSystemTask.start(any(), any(), any()))
                .thenAnswer(i -> new Runnable() {
                    @Override
                    public void run() {
                        taskModelCaptor.getValue().setStatus(TaskModel.Status.IN_PROGRESS);
                    }
                });

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(workflowSystemTask).start(workflowModel, taskModel, mockWorkflowExecutor);
        verify(taskModel).setStatus(TaskModel.Status.IN_PROGRESS);
        verify(mockQueueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_whenTaskIsInProgress_shouldExecuteTaskAndUpdateTaskStatus() {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(mockExecutionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowModel);
        when(taskModel.getId()).thenReturn(TASK_ID);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(taskModel.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(workflowModel.getStatus()).thenReturn(WorkflowStatus.RUNNING);

        ArgumentCaptor<WorkflowSystemTask> workflowSystemTaskCaptor =
                ArgumentCaptor.forClass(WorkflowSystemTask.class);
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        when(workflowSystemTask.execute(any(), any(), any()))
                .thenAnswer(i -> new Runnable() {
                    @Override
                    public void run() {
                        taskModelCaptor.getValue().setStatus(TaskModel.Status.COMPLETED);
                    }
                });

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(workflowSystemTask).execute(workflowModel, taskModel, mockWorkflowExecutor);
        verify(taskModel).setStatus(TaskModel.Status.COMPLETED);
        verify(mockQueueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_whenTaskIsTerminal_shouldUpdateTaskStatusAndDecideWorkflow() {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(mockExecutionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowModel);
        when(taskModel.getId()).thenReturn(TASK_ID);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.TERMINAL);
        when(taskModel.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(workflowModel.getStatus()).thenReturn(WorkflowStatus.RUNNING);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_whenTaskHasCompleted_shouldUpdateTaskStatusAndDecideWorkflow() {
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(mockExecutionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflowModel);
        when(taskModel.getId()).thenReturn(TASK_ID);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(taskModel.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);
        when(workflowModel.getStatus()).thenReturn(WorkflowStatus.RUNNING);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testPostponeQuietly_shouldPostponeTask() {
        TaskModel taskModel = mock(TaskModel.class);
        String queueName = QueueUtils.getQueueName(taskModel);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(mockQueueDAO).postpone(queueName, taskModel.getId(), taskModel.getWorkflowPriority(), TASK_EXECUTION_POSTPONE_DURATION);
    }

    @Test
    public void testLoadTaskQuietly_shouldReturnNull() {
        when(mockExecutionDAOFacade.getTaskModel(TASK_ID)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertNull(result);
    }
}