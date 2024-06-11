package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);
    private static final String WORKFLOW_ID = "workflow123";

    private static final WorkflowSweeper workflowSweeper = new WorkflowSweeper(
            mock(WorkflowExecutor.class),
            mock(WorkflowRepairService.class),
            mock(ConductorProperties.class),
            mock(QueueDAO.class)
    );
    private static final QueueDAO queueDAO = mock(QueueDAO.class);
    private static final Monitors monitors = mock(Monitors.class);

    private WorkflowModel workflowModel;
    private List<TaskModel> tasks;
    private TaskModel firstTask;
    private TaskModel secondTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(queueDAO.get(anyString(), anyString())).thenAnswer(new Answer<WorkflowModel>() {
            @Override
            public WorkflowModel answer(InvocationOnMock invocation) throws Throwable {
                return workflowModel;
            }
        });
        when(queueDAO.remove(anyString(), anyString())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName("testWorkflow");
        workflowModel.setStatus(Status.RUNNING);

        tasks = Arrays.asList(
                new TaskModel(),
                new TaskModel()
        );

        firstTask = tasks.get(0);
        firstTask.setWorkflowId(WORKFLOW_ID);
        firstTask.setStatus(Status.IN_PROGRESS);
        firstTask.setTaskType(TaskType.TASK_TYPE_WAIT);
        firstTask.setWaitTimeout(60L);

        secondTask = tasks.get(1);
        secondTask.setWorkflowId(WORKFLOW_ID);
        secondTask.setStatus(Status.IN_PROGRESS);
        secondTask.setTaskType(TaskType.TASK_TYPE_HUMAN);
        secondTask.setResponseTimeoutSeconds(45);

        when(workflowModel.getTasks()).thenReturn(tasks);
    }

    @After
    public void tearDown() {
        WorkflowContext.clear();
    }

    @Test
    public void testSweepAsync() throws Exception {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get(10, TimeUnit.SECONDS);
    }

    @Test
    public void testSweep() throws Exception {
        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        when(queueDAO.get(anyString(), anyString())).thenThrow(new NotFoundException("Not found"));

        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test(expected = Exception.class)
    public void testSweepException() throws Exception {
        when(queueDAO.get(anyString(), anyString())).thenThrow(new Exception());

        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testUnack() throws Exception {
        workflowSweeper.unack(workflowModel, 5000L);
        assertEquals(5001L, workflowModel.getUnackTimeout());
    }

    @Test
    public void testUnackWithWaitTask() throws Exception {
        workflowSweeper.unack(workflowModel, 5000L);
        assertEquals(60001L, firstTask.getUnackTimeout());
    }

    @Test
    public void testUnackWithHumanTask() throws Exception {
        workflowSweeper.unack(workflowModel, 5000L);
        assertEquals(45001L, secondTask.getUnackTimeout());
    }

    @Test
    public void testUnackWithTimeoutWorkflow() throws Exception {
        workflowModel.setTimeoutSeconds(100L);
        workflowSweeper.unack(workflowModel, 5000L);
        assertEquals(110001L, workflowModel.getUnackTimeout());
    }

    @Test
    public void testSweeperWithEmptyWorkflow() throws Exception {
        tasks.clear();
        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweeperWithNullWorkflow() throws Exception {
        workflowModel = null;
        workflowSweeper.sweep(WORKFLOW_ID);
        assertNull(workflowModel);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(30, workflowOffsetWithJitter % 60);
    }
}