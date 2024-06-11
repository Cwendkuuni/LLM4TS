package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.metadata.model.TaskDefinition;
import com.netflix.conductor.dao.metadata.model.WorkflowDefinition;
import com.netflix.conductor.dao.queue.model.QueueMessage;
import com.netflix.conductor.dao.queue.model.QueueName;
import com.netflix.conductor.execution.WorkflowExecutor;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;

    @Before
    public void setup() {
        this.executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
        this.queueDAO = Mockito.mock(QueueDAO.class);
        this.metadataDAO = Mockito.mock(MetadataDAO.class);
        this.workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        this.conductorProperties = Mockito.mock(ConductorProperties.class);

        when(executionDAOFacade.getTaskModel(anyString())).thenCallRealMethod();
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenCallRealMethod();
        when(metadataDAO.getTaskDef(anyString())).thenCallRealMethod();
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new TaskDefinition());

        this.asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_taskNotFound_taskRemovedFromQueue() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = null;

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(QueueName.fromString(QueueUtils.getQueueName(task)), taskId);
    }

    @Test
    public void testExecute_taskFound_taskStatusUpdatedAndWorkflowDecided() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        String workflowId = "workflowId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_executionLimitExceeded() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        String queueName = QueueName.fromString(QueueUtils.getQueueName(task));

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException()).when(executionDAOFacade).exceedsInProgressLimit(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_rateLimitExceeded() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowDefinition workflowDefinition = Mockito.mock(WorkflowDefinition.class);
        long rateLimitPerFrequency = 10L;

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(workflowDefinition);
        when(workflowDefinition.getRateLimitPerFrequency()).thenReturn(rateLimitPerFrequency);
        doThrow(new RuntimeException()).when(executionDAOFacade).exceedsRateLimitPerFrequency(task, workflowDefinition);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_queueSizeExceeded() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        String queueName = QueueName.fromString(QueueUtils.getQueueName(task));

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doNothing().when(queueDAO).remove(queueName, taskId);
        doThrow(new RuntimeException("Queue size exceeded")).when(queueDAO).postpone(queueName, taskId, 0L, 0L);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        String queueName = QueueName.fromString(QueueUtils.getQueueName(task));

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), systemTaskCallbackTime);
    }

    @Test
    public void testExecute_taskCompleted() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        String queueName = QueueName.fromString(QueueUtils.getQueueName(task));

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskTerminal() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        String queueName = QueueName.fromString(QueueUtils.getQueueName(task));

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.FAILED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskInProgressWithCallbackSet() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        String queueName = QueueName.fromString(QueueUtils.getQueueName(task));

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(systemTask.getEvaluationOffset(task, systemTaskCallbackTime)).thenReturn(Optional.of(1000L));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), 1000L);
    }

    @Test
    public void testPostponeQuietly_taskFound() {
        String queueName = QueueName.fromString("queueName");
        TaskModel task = Mockito.mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testPostponeQuietly_taskNotFound() {
        String queueName = QueueName.fromString("queueName");
        TaskModel task = Mockito.mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, never()).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "taskId";
        TaskModel task = Mockito.mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel taskLoaded = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, taskLoaded);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel taskLoaded = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(taskLoaded);
    }
}