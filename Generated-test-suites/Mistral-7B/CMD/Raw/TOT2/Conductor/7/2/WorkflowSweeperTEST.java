package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow_id";
    private static final String WORKFLOW_NAME = "workflow_name";
    private static final String TASK_TYPE = TaskType.TASK_TYPE_WORKFLOW.name();
    private static final long TIMEOUT_SECONDS = 10;

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutorMock;
    private QueueDAO queueDAOMock;
    private ConductorProperties conductorPropertiesMock;

    @Before
    public void setup() {
        workflowExecutorMock = mock(WorkflowExecutor.class);
        queueDAOMock = mock(QueueDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);

        when(conductorPropertiesMock.getWorkflowOffsetTimeout()).thenReturn(TIMEOUT_SECONDS);

        WorkflowContext workflowContext = new WorkflowContext(null);
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel(WORKFLOW_NAME, Status.RUNNING));
        when(queueDAOMock.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);
        when(queueDAOMock.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
        when(queueDAOMock.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong()))
                .thenReturn(true);

        workflowSweeper = new WorkflowSweeper(workflowExecutorMock, Mockito.mock(WorkflowRepairService.class),
                conductorPropertiesMock, queueDAOMock);
    }

    @Test
    public void testSweepAsync_WorkflowFound() {
        WorkflowModel workflowModel = new WorkflowModel(WORKFLOW_NAME, Status.RUNNING);
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweepAsync(WORKFLOW_ID);

        verify(workflowExecutorMock, times(1)).decide(WORKFLOW_ID);
        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        assertEquals(TaskType.TASK_TYPE_WORKFLOW, workflowModel.getWorkflowDefinition().getTaskType());
    }

    @Test
    public void testSweepAsync_WorkflowNotFound() {
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweepAsync(WORKFLOW_ID);

        verify(queueDAOMock, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync_Exception() {
        WorkflowModel workflowModel = new WorkflowModel(WORKFLOW_NAME, Status.RUNNING);
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        ArgumentCaptor<Long> timeoutArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, timeoutArgumentCaptor.capture());
        assertEquals(TIMEOUT_SECONDS * 1000, timeoutArgumentCaptor.getValue());
    }

    @Test
    public void testUnack() {
        TaskModel taskModel = new TaskModel("taskId", TASK_TYPE, Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel(WORKFLOW_NAME, Status.RUNNING);
        List<TaskModel> tasks = Arrays.asList(taskModel, new TaskModel("task2Id", TASK_TYPE, Status.IN_PROGRESS));
        workflowModel.setTasks(tasks);

        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, TIMEOUT_SECONDS);

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> workflowIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAOMock, times(1)).setUnackTimeout(queueNameArgumentCaptor.capture(), workflowIdArgumentCaptor.capture(), timeoutArgumentCaptor.capture());

        assertEquals(DECIDER_QUEUE, queueNameArgumentCaptor.getValue());
        assertEquals(WORKFLOW_ID, workflowIdArgumentCaptor.getValue());
        assertTrue(timeoutArgumentCaptor.getValue() > (TIMEOUT_SECONDS * 1000) - 1 && timeoutArgumentCaptor.getValue() <= (TIMEOUT_SECONDS * 1000) + 1);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 10;

        long jitter = new Random().nextInt((int) (2 * (workflowOffsetTimeout / 3)) + 1) - (workflowOffsetTimeout / 3);

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (workflowOffsetTimeout - jitter) && result <= (workflowOffsetTimeout + jitter));
    }
}