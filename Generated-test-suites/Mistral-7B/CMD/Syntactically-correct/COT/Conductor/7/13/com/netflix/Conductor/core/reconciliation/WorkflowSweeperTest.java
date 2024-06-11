package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.testutils.TestUtils;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    private static final String WORKFLOW_ID = "workflowId";

    private WorkflowSweeper workflowSweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        properties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAO = mock(QueueDAO.class);
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        when(queueDAO.remove(Mockito.eq(DECIDER_QUEUE), Mockito.eq(WORKFLOW_ID)))
                .thenAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });
        when(queueDAO.setUnackTimeout(Mockito.eq(DECIDER_QUEUE), Mockito.eq(WORKFLOW_ID),
                Mockito.anyLong()))
                .thenAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService),
                properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.join();
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep() throws Exception {
        WorkflowModel workflow = TestUtils.createTestWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        workflowSweeper.sweep(WORKFLOW_ID);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> workflowOffsetTimeoutCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), workflowIdCaptor.capture(),
                workflowOffsetTimeoutCaptor.capture());
        assertEquals(DECIDER_QUEUE, queueNameCaptor.getValue());
        assertEquals(WORKFLOW_ID, workflowIdCaptor.getValue());
        assertTrue(workflowOffsetTimeoutCaptor.getValue() > 0);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFoundException() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("NotFound"));

        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = TestUtils.createTestWorkflowModel();
        TaskModel taskModel1 = TestUtils.createTestTaskModel(Status.SCHEDULED);
        TaskModel taskModel2 = TestUtils.createTestTaskModel(Status.IN_PROGRESS);
        List<TaskModel> tasks = Arrays.asList(taskModel1, taskModel2);
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 10000L);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), workflowIdCaptor.capture(),
                timeoutCaptor.capture());
        assertEquals(DECIDER_QUEUE, queueNameCaptor.getValue());
        assertEquals(WORKFLOW_ID, workflowIdCaptor.getValue());
        assertTrue(timeoutCaptor.getValue() > 0);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45000L;

        long jitter = new Random().nextInt(90) - 30;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(workflowOffsetTimeout + jitter, workflowOffsetWithJitter);
    }
}