package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({AsyncSystemTaskExecutor.class})
@SpringBootTest(classes = {AsyncSystemTaskExecutor.class, ConductorProperties.class})
@ActiveProfiles("test")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowSystemTask workflowSystemTask;
    private TaskModel taskModel;
    private WorkflowModel workflowModel;

    @Autowired
    public AsyncSystemTaskExecutorTest(
            ExecutionDAOFacade executionDAOFacade,
            QueueDAO queueDAO,
            MetadataDAO metadataDAO,
            ConductorProperties conductorProperties) {
        this.executionDAOFacade = executionDAOFacade;
        this.queueDAO = queueDAO;
        this.metadataDAO = metadataDAO;
        this.conductorProperties = conductorProperties;
    }

    @Before
    public void setup() {
        workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        taskModel = Mockito.mock(TaskModel.class);
        workflowModel = Mockito.mock(WorkflowModel.class);
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        Mockito.mock(WorkflowExecutor.class));
        PowerMockito.mockStatic(QueueUtils.class);
    }

    @Test
    public void testExecute_TaskDoesNotExist_LogErrorAndExit() {
        String taskId = "task-1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(LOGGER).error("TaskId: {} could not be found while executing {}", taskId, workflowSystemTask);
    }

    @Test
    public void testExecute_TaskIsTerminal_RemoveFromQueueAndExit() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_TaskIsScheduled_PostponeInQueue() {
        String taskId = "task-1";
        String queueName = "queue-1";
        String workflowId = "workflow-1";
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        when(queueDAO.postpone(queueName, taskId, anyLong(), anyLong())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO).postpone(queueName, taskId, anyLong(), anyLong());
    }

    @Test
    public void testExecute_TaskIsInProgress_ExecuteTask() {
        String taskId = "task-1";
        String queueName = "queue-1";
        String workflowId = "workflow-1";
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        when(queueDAO.postpone(queueName, taskId, anyLong(), anyLong())).thenReturn(false);

        Whitebox.setInternalState(asyncSystemTaskExecutor, "workflowExecutor", Mockito.mock(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(workflowSystemTask).execute(workflowModel, task, Whitebox.getInternalState(asyncSystemTaskExecutor, "workflowExecutor"));
    }

    @Test
    public void testExecute_TaskIsTerminalAfterExecution_RemoveFromQueueAndDecideWorkflow() {
        String taskId = "task-1";
        String queueName = "queue-1";
        String workflowId = "workflow-1";
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        when(queueDAO.postpone(queueName, taskId, anyLong(), anyLong())).thenReturn(false);

        WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        Whitebox.setInternalState(asyncSystemTaskExecutor, "workflowExecutor", workflowExecutor);

        when(workflowExecutor.decide(workflowId)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO).remove(queueName, taskId);
        Mockito.verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_TaskHasExceededInProgressLimit_PostponeAndLogWarning() {
        String taskId = "task-1";
        String queueName = "queue-1";
        String workflowId = "workflow-1";
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(LOGGER).warn("Concurrent Execution limited for {}:{}", taskId, task.getTaskDefName());
        Mockito.verify(queueDAO).postpone(queueName, taskId, anyLong(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskHasExceededRateLimitPerFrequency_PostponeAndLogWarning() {
        String taskId = "task-1";
        String queueName = "queue-1";
        String workflowId = "workflow-1";
        TaskModel task = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.IN_PROGRESS);

        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new Object());
        when(task.getRateLimitPerFrequency()).thenReturn(1);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, new Object())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(LOGGER).warn(
                "RateLimit Execution limited for {}:{}, limit:{}",
                taskId,
                task.getTaskDefName(),
                task.getRateLimitPerFrequency());
        Mockito.verify(queueDAO).postpone(queueName, taskId, anyLong(), queueTaskMessagePostponeSecs);
    }
}