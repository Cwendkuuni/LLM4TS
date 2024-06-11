package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
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
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowOffset;
import com.netflix.conductor.utils.Utils;

@SuppressWarnings("deprecation")
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
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(properties.getSweeperExecutorName()).thenReturn("sweeperExecutorName");
        when(queueDAO.remove(Utils.DECIDER_QUEUE, "workflowId1")).thenReturn(true);
        when(workflowExecutor.decide("workflowId1")).thenReturn(new WorkflowModel());
    }

    @Test
    public void testSweepAsync() throws Exception {
        CompletableFuture<Void> future = sweeper.sweepAsync("workflowId1");
        future.get();
    }

    @Test
    public void testSweep() throws Exception {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId1");
        workflowModel.setWorkflowName("workflowName1");
        workflowModel.setTasks(
                Arrays.asList(
                        new TaskModel(), new TaskModel(), new TaskModel(), new TaskModel(), new TaskModel()));

        when(workflowExecutor.decide("workflowId1")).thenReturn(workflowModel);

        sweeper.sweep("workflowId1");

        verifyUnackCalledWithExpectedTimeout(workflowModel.getWorkflowId(), 6L);
    }

    @Test
    public void testSweepNotFound() throws Exception {
        when(workflowExecutor.decide("workflowId1")).thenThrow(new NotFoundException(""));

        sweeper.sweep("workflowId1");

        verifyQueueDAORemoved("workflowId1");
    }

    @Test
    public void testSweepException() throws Exception {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId1");
        workflowModel.setWorkflowName("workflowName1");
        workflowModel.setTasks(Arrays.asList(new TaskModel()));

        when(workflowExecutor.decide("workflowId1")).thenReturn(workflowModel);
        when(monitors.error(WorkflowSweeper.class.getSimpleName(), "sweep")).thenReturn(true);

        sweeper.sweep("workflowId1");

        verifyQueueDAORemoved("workflowId1");
    }

    @Test
    public void testUnack() throws Exception {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId1");
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setResponseTimeoutSeconds(3L);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId1");
        workflowModel.setTasks(Arrays.asList(taskModel));

        sweeper.unack(workflowModel, 5L);

        verifyQueueDAOSetUnackTimeout(Utils.DECIDER_QUEUE, workflowModel.getWorkflowId(), 8L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        assertEquals(7L, sweeper.workflowOffsetWithJitter(5L));
    }

    private void verifyQueueDAORemoved(String workflowId) {
        when(queueDAO.remove(Utils.DECIDER_QUEUE, workflowId)).thenReturn(true);
        sweeper.sweep(workflowId);
        verify(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);
    }

    private void verifyUnackCalledWithExpectedTimeout(String workflowId, long expectedTimeout) {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);

        sweeper.sweep(workflowId);
        sweeper.unack(workflowModel, expectedTimeout);
        verify(queueDAO).setUnackTimeout(Utils.DECIDER_QUEUE, workflowId, expectedTimeout * 1000);
    }

    private void verifyQueueDAOSetUnackTimeout(String queueName, String workflowId, long timeout) {
        sweeper.sweep(workflowId);
        sweeper.unack(new WorkflowModel(), timeout);
        verify(queueDAO).setUnackTimeout(queueName, workflowId, timeout * 1000);
    }
}