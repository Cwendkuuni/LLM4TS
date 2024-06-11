package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties conductorProperties;

    private WorkflowModel workflowModel;
    private TaskModel taskModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(45L);
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(true);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenReturn(true);
    }

    @Test
    public void testSweepWorkflowFoundWithTerminalStatus() {
        workflowModel = new WorkflowModel();
        workflowModel.setId("workflowId");
        workflowModel.setStatus(Status.TERMINAL);

        workflowSweeper.sweep("workflowId");

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).remove(queueNameCaptor.capture(), eq("workflowId"));
        assertEquals(DECIDER_QUEUE, queueNameCaptor.getValue());
    }

    @Test
    public void testSweepWorkflowNotFound() {
        doThrow(new NotFoundException("")).when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep("workflowId");

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), timeoutCaptor.capture());
        assertEquals(workflowOffsetWithJitter(45L), timeoutCaptor.getValue());
    }

    @Test
    public void testSweepWorkflowWithPendingTasks() {
        workflowModel = new WorkflowModel();
        workflowModel.setId("workflowId");
        workflowModel.setStatus(Status.CREATED);

        taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(30L);

        workflowModel.setTasks(Collections.singletonList(taskModel));

        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));

        workflowSweeper.sweep("workflowId");

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), timeoutCaptor.capture());
        assertEquals(workflowOffsetWithJitter(45L), timeoutCaptor.getValue());
    }

    @Test
    public void testSweepWorkflowWithInProgressTasks() {
        workflowModel = new WorkflowModel();
        workflowModel.setId("workflowId");
        workflowModel.setStatus(Status.CREATED);

        taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_EXECUTOR);
        taskModel.setResponseTimeoutSeconds(30L);

        workflowModel.setTasks(Collections.singletonList(taskModel));

        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));

        workflowSweeper.sweep("workflowId");

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), timeoutCaptor.capture());
        assertEquals(workflowOffsetWithJitter(45L), timeoutCaptor.getValue());
    }

    @Test
    public void testUnack() throws InterruptedException {
        workflowModel = new WorkflowModel();
        workflowModel.setId("workflowId");
        workflowModel.setStatus(Status.CREATED);

        List<TaskModel> tasks =
                Collections.singletonList(
                        new TaskModel()
                                .setTaskId("taskId")
                                .setStatus(Status.IN_PROGRESS)
                                .setTaskType(TaskType.TASK_TYPE_EXECUTOR));
        workflowModel.setTasks(tasks);

        when(workflowModel.getTasks()).thenReturn(tasks);

        workflowSweeper.unack(workflowModel, 45L);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), timeoutCaptor.capture());
        assertEquals(workflowOffsetWithJitter(45L), timeoutCaptor.getValue());

        Thread.sleep(50L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        long jitter = workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(36, ChronoUnit.SECONDS.between(Instant.now(), Instant.now().plus(jitter)));
        assertEquals(55, ChronoUnit.SECONDS.between(Instant.now(), Instant.now().plus(workflowOffsetTimeout + jitter)));
    }
}