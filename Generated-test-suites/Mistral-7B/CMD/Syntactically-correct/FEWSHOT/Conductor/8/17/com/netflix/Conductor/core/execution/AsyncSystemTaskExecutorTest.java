package com.netflix.conductor.core.execution;

import static com.netflix.conductor.core.execution.AsyncSystemTaskExecutor.LOGGER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ActiveProfiles;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.model.queue.TaskQueueMessage;
import com.netflix.conductor.model.queue.TaskQueueMessage.MessageType;
import com.netflix.conductor.testutils.TestExecutionDAOFacade;
import com.netflix.conductor.testutils.TestQueueDAO;
import com.netflix.conductor.testutils.TestWorkflowExecutor;
import com.netflix.conductor.testutils.TestWorkflowModel;

@SpringBootTest
@ActiveProfiles("test")
@MockBeans(value = { TestExecutionDAOFacade.class, TestQueueDAO.class, TestWorkflowExecutor.class })
public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        task = new TaskModel();
        task.setTaskId("taskId");
        task.setTaskType("taskType");
        task.setWorkflowInstanceId("workflowId");
        task.setQueueName(QueueUtils.getQueueName(task));
        task.setStatus(TaskModel.Status.SCHEDULED);

        workflow = new TestWorkflowModel();
        workflow.setId("workflowId");

        systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(0), false)).thenReturn(workflow);
    }

    @After
    public void teardown() {
        Mockito.reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void testExecute_taskNotExist() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(queueDAO).remove(queueNameCaptor.capture(queueName -> queueName), taskId);
        assertEquals("taskId", taskId);
        LOGGER.error("TaskId: {} could not be found while executing {}", taskId, systemTask);
    }

    @Test
    public void testExecute_taskIsTerminal() {
        task.setStatus(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(queueNameCaptor.capture(queueName -> queueName), task.getTaskId());
        assertEquals("taskId", task.getTaskId());
        workflow.setStatus(WorkflowStatus.COMPLETED);
        LOGGER.info("Task {}/{} was already completed.", task.getTaskType(), task.getTaskId());
    }

    @Test
    public void testExecute_exceedsInProgressLimit() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).postpone(
                queueNameCaptor.capture(queueName -> queueName),
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        assertEquals("taskId", task.getTaskId());
        LOGGER.warn(
                "Concurrent Execution limited for {}:{}, limit:{}",
                task.getTaskId(),
                task.getTaskDefName(),
                task.getMaxConcurrentInstances());
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        workflow.setRateLimitPerFrequency(1);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, metadataDAO.getTaskDef(task.getTaskDefName())))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).postpone(
                queueNameCaptor.capture(queueName -> queueName),
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        assertEquals("taskId", task.getTaskId());
        LOGGER.warn(
                "RateLimit Execution limited for {}:{}, limit:{}",
                task.getTaskId(),
                task.getTaskDefName(),
                task.getRateLimitPerFrequency());
    }

    @Test
    public void testExecute_workflowIsTerminal() {
        workflow.setStatus(WorkflowStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(queueNameCaptor.capture(queueName -> queueName), task.getTaskId());
        assertEquals("taskId", task.getTaskId());
        LOGGER.info("Workflow {} has been completed for {}/{}", workflow.toShortString(), systemTask, task.getTaskId());
    }

    @Test
    public void testExecute_taskIsRunning() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO, Mockito.never()).remove(queueNameCaptor.capture(queueName -> queueName), task.getTaskId());
        systemTask.execute(workflow, task, workflowExecutor);
    }

    @Test
    public void testExecute_taskIsScheduled() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).postpone(
                queueNameCaptor.capture(queueName -> queueName),
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        assertEquals("taskId", task.getTaskId());
        task.incrementPollCount();
        task.setStartTime(System.currentTimeMillis());
        LOGGER.debug("Executing {}/{} in {} state", task.getTaskType(), task.getTaskId(), task.getStatus());
    }

    @Test
    public void testExecute_taskIsCompleted()
            throws InterruptedException {
        task.setStatus(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(queueNameCaptor.capture(queueName -> queueName), task.getTaskId());
        assertEquals("taskId", task.getTaskId());
        workflow.setStatus(WorkflowStatus.COMPLETED);
        LOGGER.debug("{} removed from queue: {}", task, queueNameCaptor.getValue());
    }

    @Test
    public void testExecute_taskExecutionError() {
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(workflowExecutor.decide(workflow.getId())).thenThrow(new RuntimeException("Test error"));

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO, Mockito.never()).remove(queueNameCaptor.capture(queueName -> queueName), task.getTaskId());
        Monitors.error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
        LOGGER.error("Error executing system task - {}, with id: {}", systemTask, task.getTaskId());
    }

    @Test
    public void testExecute_queueIsEmpty() {
        when(queueDAO.getTaskQueueMessages(queueNameCaptor.capture(queueName -> queueName)))
                .thenReturn(new TaskQueueMessage[0]);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO, Mockito.never()).remove(queueNameCaptor.capture(queueName -> queueName), task.getTaskId());
    }

    @Test
    public void testExecute_queueHasMessages() {
        TaskQueueMessage message = new TaskQueueMessage();
        message.setMessageType(MessageType.TASK);
        message.setTaskId("taskId");
        message.setTaskQueueName(queueNameCaptor.capture(queueName -> queueName));

        when(queueDAO.getTaskQueueMessages(queueNameCaptor.capture(queueName -> queueName)))
                .thenReturn(new TaskQueueMessage[] { message });

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(queueNameCaptor.capture(queueName -> queueName), task.getTaskId());
        assertEquals(queueNameCaptor.getValue(), message.getTaskQueueName());
    }
}