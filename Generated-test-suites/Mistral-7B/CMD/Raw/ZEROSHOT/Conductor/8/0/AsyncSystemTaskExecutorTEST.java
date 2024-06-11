package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetaDataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueueStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowDefinition;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowInstanceState;
import com.netflix.conductor.utils.jackson.JacksonMapper;

@ActiveProfiles("test")
public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetaDataDAO metadataDAO;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private Monitors monitors;

    @Autowired
    private AsyncSystemTaskExecutor systemTaskExecutor;

    private TaskModel task;
    private String taskId;
    private WorkflowSystemTask systemTask;
    private WorkflowDefinition workflowDefinition;
    private WorkflowInstance workflowInstance;

    @Before
    public void setUp() {
        task = new TaskModel();
        task.setId("taskId");
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskType("taskType");
        task.setTaskDefName("taskDefName");
        task.setQueueWaitTime(Duration.ofSeconds(1));

        workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setName("workflowName");
        workflowDefinition.setDefinition("workflowDefinition");

        workflowInstance = new WorkflowInstance();
        workflowInstance.setId("workflowInstanceId");
        workflowInstance.setState(WorkflowInstanceState.STARTED);
        workflowInstance.setDefinition(workflowDefinition);

        systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        when(systemTask.start(workflowInstance, task, workflowExecutor)).thenCallRealMethod();
        when(systemTask.execute(workflowInstance, task, workflowExecutor)).thenCallRealMethod();

        when(executionDAOFacade.getWorkflowModel(workflowInstance.getId(), true))
                .thenReturn(workflowInstance);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(workflowDefinition);
        when(queueDAO.getQueueName(task)).thenReturn("testQueue");
        when(queueDAO.getWorkflowPriority(task.getWorkflowInstanceId()))
                .thenReturn(new Integer(2));
        when(queueDAO.remove(QueueUtils.getQueueName(task), task.getTaskId()))
                .thenReturn(null);
        when(queueDAO.postpone(
                        QueueUtils.getQueueName(task),
                        task.getTaskId(),
                        2,
                        queueTaskMessagePostponeSecs))
                .thenReturn(null);
        when(queueDAO.remove(QueueUtils.getQueueName(task), task.getTaskId()))
                .thenReturn(null);
        when(queueDAO.postpone(
                        QueueUtils.getQueueName(task),
                        task.getTaskId(),
                        2,
                        queueTaskMessagePostponeSecs))
                .thenReturn(null);
        when(queueDAO.postpone(
                        QueueUtils.getQueueName(task),
                        task.getTaskId(),
                        2,
                        System.currentTimeMillis() + queueTaskMessagePostponeSecs))
                .thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task), task.getTaskId()))
                .thenReturn(task);
        when(queueDAO.get(QueueUtils.getQueueName(task), task.getTaskId())).thenReturn(null);
        when(queueDAO.getSize(QueueUtils.getQueueName(task))).thenReturn(1);
    }

    @After
    public void tearDown() {
        task = null;
        taskId = null;
        systemTask = null;
        workflowDefinition = null;
        workflowInstance = null;
    }

    @Test
    public void executeTask_taskIsNotPresent_shouldNotExecuteTask() {
        taskId = "nonExistentTaskId";
        systemTaskExecutor.execute(systemTask, taskId);
        assertNull(task);
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void executeTask_taskIsTerminal_shouldRemoveTaskFromQueue() {
        task.setStatus(TaskModel.Status.COMPLETED);
        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void executeTask_taskIsScheduled_shouldPostponeTaskInQueue() {
        systemTaskExecutor.execute(systemTask, taskId);
        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task), task.getTaskId(), 2, queueTaskMessagePostponeSecs);
    }

    @Test(expected = IllegalStateException.class)
    public void executeTask_concurrentExecutionLimitIsReached_shouldThrowException() {
        task = new TaskModel();
        task.setId("taskId");
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskType("taskType");
        task.setTaskDefName("taskDefName");
        task.setQueueWaitTime(Duration.ofSeconds(1));
        task.setTaskDef(workflowDefinition);

        doThrow(new RuntimeException("Test Exception"))
                .when(executionDAOFacade)
                .exceedsInProgressLimit(task);

        systemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void executeTask_rateLimitIsReached_shouldPostponeTaskInQueue() {
        task = new TaskModel();
        task.setId("taskId");
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskType("taskType");
        task.setTaskDefName("taskDefName");
        task.setQueueWaitTime(Duration.ofSeconds(1));
        task.setTaskDef(workflowDefinition);
        task.setRateLimitPerFrequency(3);

        doThrow(new RuntimeException("Test Exception"))
                .when(executionDAOFacade)
                .exceedsRateLimitPerFrequency(task, workflowDefinition);

        systemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task), task.getTaskId(), 2, queueTaskMessagePostponeSecs);
    }

    @Test
    public void executeTask_taskIsInProgress_shouldCallSystemTaskExecute() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        systemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask).execute(workflowInstance, task, workflowExecutor);
    }

    @Test
    public void executeTask_taskIsAsyncComplete_shouldRemoveTaskFromQueue() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        systemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, never()).execute(workflowInstance, task, workflowExecutor);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }
}