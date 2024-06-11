package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.common.data.TaskStatus;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueueMetadata;
import com.netflix.conductor.core.model.Task;
import com.netflix.conductor.core.model.WorkflowInstance;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.test.ConductorTestRunner;
import com.netflix.conductor.test.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private Monitors monitors;

    @Autowired
    private AsyncSystemTaskExecutor systemTaskExecutor;

    @Autowired
    private ConductorProperties conductorProperties;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    private WorkflowSystemTask systemTask;

    private Task task;

    private WorkflowInstance workflowInstance;

    @Before
    public void setUp() {
        systemTask = Mockito.spy(new WorkflowSystemTask() {
            @Override
            public boolean isAsyncComplete(Task task) {
                return true;
            }
        });
        task = new Task();
        workflowInstance = new WorkflowInstance();
        workflowInstance.setWorkflowInstanceId("test-workflow-id");
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean()))
                .thenReturn(workflowInstance);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(TestUtils.createTaskDefinition("testTaskDef"));
    }

    @Test
    public void testExecute_taskIsCompleted_removesTaskFromQueue() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_taskIsInProgress_executesSystemTask() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(systemTask).start(workflowInstance, task, workflowExecutor);
    }

    @Test
    public void testExecute_taskIsTerminal_removesTaskFromQueue() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.COMPLETED);
        task.setTaskDefName("testTaskDef");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_queueIsFull_postponesTask() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskHasRateLimit_postponesTask() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskIsNotFound_doesNotChangeQueue() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testExecute_workflowIsTerminal_removesTaskFromQueue() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        workflowInstance.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_workflowIsRunning_doesNotChangeQueue() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        workflowInstance.setStatus(ExecutionStatus.RUNNING);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testExecute_workflowIsPending_doesNotChangeQueue() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        workflowInstance.setStatus(ExecutionStatus.PENDING);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testExecute_workflowHasError_removesTaskFromQueue() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        workflowInstance.setStatus(ExecutionStatus.ERROR);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testPostponeQuietly_taskIsNotFound_doesNotChangeQueue() {
        task.setTaskId("test-task-id");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());

        systemTaskExecutor.postponeQuietly(QueueUtils.getQueueName(task), task);

        Mockito.verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testPostponeQuietly_taskIsNotPostponed_doesNotChangeQueue() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        workflowInstance.setWorkflowInstanceId("test-workflow-id");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());
        when(queueDAO.postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                anyLong())).thenReturn(task.getTaskId());

        systemTaskExecutor.postponeQuietly(QueueUtils.getQueueName(task), task);

        Mockito.verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testPostponeQuietly_taskIsPostponed_doesNotChangeQueueAfterSuccess() {
        task.setTaskId("test-task-id");
        task.setStatus(TaskStatus.SCHEDULED);
        workflowInstance.setWorkflowInstanceId("test-workflow-id");
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task))).thenReturn(new QueueMetadata());
        when(queueDAO.postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                anyLong())).thenThrow(new RuntimeException());

        systemTaskExecutor.postponeQuietly(QueueUtils.getQueueName(task), task);

        Mockito.verify(queueDAO).postpone(
                QueueUtils.getQueueName(task),
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound_returnsNull() {
        when(executionDAOFacade.getTaskModel("test-task-id")).thenReturn(null);

        Task returnedTask = systemTaskExecutor.loadTaskQuietly("test-task-id");

        assertNull(returnedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskIsLoaded_returnsTask() {
        task.setTaskId("test-task-id");
        when(executionDAOFacade.getTaskModel("test-task-id")).thenReturn(task);

        Task returnedTask = systemTaskExecutor.loadTaskQuietly("test-task-id");

        assertSame(task, returnedTask);
    }
}