package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final String TASK_DEF_NAME = "taskDefName";
    private static final String TASK_TYPE_WAIT = "TaskTypeWait";
    private static final String TASK_TYPE_HUMAN = "TaskTypeHuman";
    private static final String TASK_TYPE_OTHER = "TaskTypeOther";
    private static final String TASK_MODEL_ID = "taskModelId";
    private static final long WORKFLOW_OFFSET_TIMEOUT = 60;

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private Monitors monitors;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);
        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(null),
                Utils.getConductorProperties(),
                queueDAO);
    }

    @Test
    public void testSweep_workflowExists_sweepsWorkflowAndUnacksTasks() {
        // Given
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verifyTasksUnacked(workflowModel);
        verifyWorkflowRemovedFromDeciderQueue(workflowModel);
        assertEquals(Status.COMPLETED, workflowModel.getStatus());
    }

    @Test
    public void testSweep_workflowNotFound_removesWorkflowFromDeciderQueue() {
        // Given
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException());

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verifyWorkflowRemovedFromDeciderQueue(null);
    }

    @Test
    public void testSweep_workflowError_logsErrorAndDoesNotRemoveWorkflowFromDeciderQueue() {
        // Given
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        when(monitors.error(Mockito.eq(WorkflowSweeper.class.getSimpleName()), Mockito.eq("sweep")))
                .thenReturn(new AsyncResult<>(null));

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verifyTasksNotUnacked(workflowModel);
        verifyWorkflowStillInDeciderQueue(workflowModel);
    }

    @Test
    public void testUnack_taskInProgress_unacksTask() {
        // Given
        TaskModel taskModel = createTaskModel(Status.IN_PROGRESS);
        WorkflowModel workflowModel = createWorkflowModel(taskModel);

        // When
        workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        // Then
        long unackTimeout = workflowModel.getUnackTimeout();
        assertEquals(WORKFLOW_OFFSET_TIMEOUT * 1000, unackTimeout);
    }

    @Test
    public void testUnack_taskScheduled_unacksTask() {
        // Given
        TaskModel taskModel = createTaskModel(Status.SCHEDULED);
        WorkflowModel workflowModel = createWorkflowModel(taskModel);

        // When
        workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        // Then
        long unackTimeout = workflowModel.getUnackTimeout();
        assertEquals(WORKFLOW_OFFSET_TIMEOUT * 1000, unackTimeout);
    }

    @Test
    public void testUnack_workflowNotExists_doesNotUnackTasks() {
        // Given
        WorkflowModel workflowModel = createWorkflowModel();
        when(queueDAO.getWorkflowById(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);

        // When
        workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        // Then
        assertEquals(0, workflowModel.getUnackTimeout());
    }

    private WorkflowModel createWorkflowModel() {
        return createWorkflowModel(Collections.emptyList());
    }

    private WorkflowModel createWorkflowModel(TaskModel... tasks) {
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setName(WORKFLOW_NAME);
        workflowDefinition.setTimeoutSeconds(30);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_ID);
        workflowModel.setName(WORKFLOW_NAME);
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setWorkflowDefinition(workflowDefinition);
        workflowModel.setTasks(Arrays.asList(tasks));
        return workflowModel;
    }

    private TaskModel createTaskModel(Status status) {
        return new TaskModel()
                .setId(TASK_MODEL_ID)
                .setStatus(status)
                .setTaskDef(new TaskDef().setName(TASK_DEF_NAME))
                .setTaskType(status == Status.IN_PROGRESS ? TASK_TYPE_WAIT : TASK_TYPE_OTHER);
    }

    private void verifyTasksUnacked(WorkflowModel workflowModel) {
        List<TaskModel> tasks = workflowModel.getTasks();
        for (TaskModel taskModel : tasks) {
            if (taskModel.getStatus() == Status.IN_PROGRESS) {
                assertEquals(0, queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowModel.getId(),
                        taskModel.getId()));
            } else if (taskModel.getStatus() == Status.SCHEDULED) {
                assertEquals(0, queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowModel.getId(),
                        taskModel.getId()));
            }
        }
    }

    private void verifyTasksNotUnacked(WorkflowModel workflowModel) {
        List<TaskModel> tasks = workflowModel.getTasks();
        for (TaskModel taskModel : tasks) {
            if (taskModel.getStatus() == Status.IN_PROGRESS) {
                assertEquals(-1, queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowModel.getId(),
                        taskModel.getId()));
            } else if (taskModel.getStatus() == Status.SCHEDULED) {
                assertEquals(-1, queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowModel.getId(),
                        taskModel.getId()));
            }
        }
    }

    private void verifyWorkflowRemovedFromDeciderQueue(WorkflowModel workflowModel) {
        if (workflowModel != null) {
            assertEquals(-1, queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowModel.getId(), null));
        }
    }

    private void verifyWorkflowStillInDeciderQueue(WorkflowModel workflowModel) {
        if (workflowModel != null) {
            long unackTimeout = queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowModel.getId(), null);
            assertEquals(WORKFLOW_OFFSET_TIMEOUT * 1000, unackTimeout);
        }
    }
}