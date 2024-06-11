package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.test.WorkflowTestUtils;

@ContextConfiguration(classes = { WorkflowSweeper.class })
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "test-workflow-id";
    private static final String WORKFLOW_NAME = "test-workflow-name";
    private static final String TASK_TYPE = "test-task-type";
    private static final String TASK_DEF_NAME = "test-task-def-name";

    private WorkflowSweeper sweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private WorkflowContext workflowContext;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setUp() {
        properties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        sweeper = new WorkflowSweeper(
                workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
        new AnnotatedThreadLocalWorkflowContext(WorkflowContext.class).set(workflowContext);
        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(1L);
    }

    @After
    public void tearDown() {
        new AnnotatedThreadLocalWorkflowContext(WorkflowContext.class).remove();
    }

    @Test
    public void testSweep() throws InterruptedException {
        WorkflowModel workflow = WorkflowTestUtils.createTestWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.empty());

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() throws InterruptedException {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenThrow(new NotFoundException(""));

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testSweepWithWorkflow() throws InterruptedException {
        WorkflowModel workflow = WorkflowTestUtils.createTestWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME);
        TaskModel taskModel = WorkflowTestUtils.createTestTaskModel(TASK_TYPE, TASK_DEF_NAME);
        workflow.getTasks().add(taskModel);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflow));

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepWithTasks() throws InterruptedException {
        WorkflowModel workflow = WorkflowTestUtils.createTestWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME);
        TaskModel taskModel1 = WorkflowTestUtils.createTestTaskModel(TASK_TYPE, TASK_DEF_NAME);
        taskModel1.setStatus(Status.SCHEDULED);
        WorkflowTestUtils.createTestTaskModel(TASK_TYPE, TASK_DEF_NAME);
        workflow.getTasks().addAll(Arrays.asList(taskModel1));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflow));

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepWithWorkflowAndRepairedTasks() throws InterruptedException {
        WorkflowModel workflow = WorkflowTestUtils.createTestWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME);
        TaskModel taskModel1 = WorkflowTestUtils.createTestTaskModel(TASK_TYPE, TASK_DEF_NAME);
        taskModel1.setStatus(Status.FAILED);
        taskModel1.setLastExecutionErrorMessage("Test Error");
        WorkflowTestUtils.createTestTaskModel(TASK_TYPE, TASK_DEF_NAME);
        workflow.getTasks().addAll(Arrays.asList(taskModel1));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(workflow));
        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenCallRealMethod();

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync() throws InterruptedException {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.ofNullable(createTestWorkflowModel()));

        CompletableFuture<Void> result = sweeper.sweepAsync(WORKFLOW_ID);
        result.get(1, TimeUnit.SECONDS);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("test-task-id");
        taskModel.setTaskType(new TaskType());
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.getTasks().add(taskModel);

        sweeper.unack(workflowModel, 1000L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 1001L);
    }

    @Test
    public void testUnackWithJitter() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("test-task-id");
        taskModel.setTaskType(new TaskType());
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.getTasks().add(taskModel);

        sweeper.unack(workflowModel, 1000L);

        Mockito.verify(queueDAO, times(2)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        long jitter = Mockito.argCaptor(IntCapture.class).getValue();
        assertTrue(jitter >= 333 && jitter <= 666);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= 30 && result <= 60);
    }

    private WorkflowModel createTestWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        List<TaskModel> tasks = Collections.singletonList(new TaskModel());
        workflowModel.setTasks(tasks);
        return workflowModel;
    }
}

class IntCapture implements ArgumentCaptor<Integer> {
    public Integer capture(ArgumentCaptor<Integer> intArgumentCaptor) {
        return intArgumentCaptor.getValue();
    }
}