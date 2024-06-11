package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(properties.getWorkflowOffsetTimeout()).thenReturn(10L);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(null);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync("workflowId");
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setStatus(Status.RUNNING);
        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenReturn(workflowModel);

        workflowSweeper.sweep("workflowId");

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), timeoutArgumentCaptor.capture());
        long timeout = timeoutArgumentCaptor.getValue();
        assertEquals(11L, timeout);
    }

    @Test
    public void testSweepNotFoundException() {
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException());

        workflowSweeper.sweep("workflowId");

        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq("workflowId"));
    }

    @Test
    public void testSweepException() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenThrow(new Exception());

        workflowSweeper.sweep("workflowId");

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), timeoutArgumentCaptor.capture());
        long timeout = timeoutArgumentCaptor.getValue();
        assertEquals(11L, timeout);
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setStatus(Status.RUNNING);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setWorkflowId("workflowId");
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setTaskType(TaskType.TASK_TYPE_WAIT);

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setWorkflowId("workflowId");
        taskModel2.setStatus(Status.SCHEDULED);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel1);
        tasks.add(taskModel2);
        workflowModel.setTasks(tasks);

        ArgumentCaptor<WorkflowModel> workflowModelArgumentCaptor = ArgumentCaptor.forClass(WorkflowModel.class);

        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel);

        ArgumentCaptor<String> queueNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> workflowIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), timeoutArgumentCaptor.capture());
        long timeout = timeoutArgumentCaptor.getValue();
        assertEquals(11L, timeout);
        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq("workflowId"));
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), timeoutArgumentCaptor.capture());
        long timeout2 = timeoutArgumentCaptor.getValue();
        assertEquals(11L, timeout2);
        verify(workflowExecutor, times(1)).decide(workflowIdArgumentCaptor.capture());
        String capturedWorkflowId = workflowIdArgumentCaptor.getValue();
        assertEquals("workflowId", capturedWorkflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 10L;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (workflowOffsetTimeout - 3) && result <= (workflowOffsetTimeout + 3));
    }
}