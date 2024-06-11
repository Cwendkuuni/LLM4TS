package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.workflow.WorkflowTask;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private ConductorProperties conductorProperties;

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        Monitors mockMonitors = mock(Monitors.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(createWorkflowModel());
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(true);
        when(queueDAO.remove(anyString(), anyString()))
                .thenReturn(true);
        when(executionDAOFacade.getTaskModel(anyString()))
                .thenReturn(createTaskModel());
        when(executionDAOFacade.updateTask(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(executionDAOFacade.exceedsInProgressLimit(any()))
                .thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
                .thenReturn(false);
        when(workflowExecutor.decide(anyString()))
                .thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenCallRealMethod();
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(60));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(Duration.ofSeconds(10));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                (MetadataDAOImpl) metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @After
    public void tearDown() {
        reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor, conductorProperties);
    }

    @Test
    public void testExecute_TaskDoesNotExist_RemovesDeadMessageFromQueue() throws Exception {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("task1");
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskIsAlreadyCompleted_RemovesTaskFromQueue() throws Exception {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("task1");
        TaskModel task = createTaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskIsScheduled_AndNoLimitIsReached_ExecutesTask() throws Exception {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("task1");
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).start(any(), any(), any());
    }

    @Test
    public void testExecute_TaskIsInProgress_AndRateLimitIsReached_PostponesTask() throws Exception {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("task1");
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(queueName, taskId, 1, 10);
    }

    @Test
    public void testExecute_TaskIsScheduled_AndInProgressLimitIsReached_PostponesTask() throws Exception {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("task1");
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(queueName, taskId, 1, 10);
    }

    @Test
    public void testExecute_TaskIsCompleted_AndWorkflowIsCompleted_DecidesWorkflow() throws Exception {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("task1");
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel();
        WorkflowModel workflow = createWorkflowModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test
    public void testExecute_TaskIsCompleted_AndWorkflowIsNotCompleted_DoesNotDecideWorkflow() throws Exception {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("task1");
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel();
        WorkflowModel workflow = createWorkflowModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false))
                .thenReturn(workflow);

        doCallRealMethod().when(workflowExecutor).decide(workflow.getId());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, never()).decide(workflow.getId());
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("1");
        workflow.setName("Workflow1");
        workflow.setStatus(WorkflowInstanceStatus.RUNNING);
        return workflow;
    }

    private TaskModel createTaskModel() {
        TaskModel task = new TaskModel();
        task.setId("1");
        task.setTaskDefName("testTaskDef");
        task.setTaskType("testTaskType");
        task.setStatus(TaskModel.Status.SCHEDULED);
        return task;
    }
}