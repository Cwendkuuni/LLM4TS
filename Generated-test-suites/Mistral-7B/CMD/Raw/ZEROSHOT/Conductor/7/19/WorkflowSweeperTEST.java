package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
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
import com.netflix.conductor.core.decider.Decision;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors.MonitorType;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.testutils.TestUtils;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testApp");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(properties.getSweeperExecutorName()).thenReturn("sweeperExecutor");
        when(queueDAO.getWorkflowModel(anyString())).then(invocation -> createWorkflowModel());
        when(monitors.error(anyString(), anyString())).thenReturn(null);
        when(workflowExecutor.decide(anyString())).thenReturn(createDecision());
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        // Assertions for async tests are tricky, you might want to test the CompletableFuture's completion
        // or exception handling, depending on your requirements
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel();
        when(queueDAO.getWorkflowModel(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        assertThat(workflowModel.getStatus(), is(Status.COMPLETED));
        verifyNoUnackTimeoutSet();
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        String workflowId = "testWorkflowId";
        when(queueDAO.getWorkflowModel(workflowId)).thenThrow(new NotFoundException("testException"));

        workflowSweeper.sweep(workflowId);

        assertNull(queueDAO.getWorkflowModel(workflowId));
        verifyNoUnackTimeoutSet();
    }

    @Test
    public void testSweepException() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel();
        when(queueDAO.getWorkflowModel(workflowId)).thenReturn(workflowModel);
        when(workflowExecutor.decide(workflowId)).thenThrow(new Exception("testException"));

        workflowSweeper.sweep(workflowId);

        Monitors monitor = monitors.getMonitor(MonitorType.UNACK_TIME, workflowModel.getWorkflowName());
        assertFalse(monitor.isMonitoring());
        verifyUnackTimeoutSet(workflowModel.getWorkflowId(), 5000L);
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel();
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        when(queueDAO.getWorkflowModel(workflowId)).thenReturn(workflowModel);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(true);
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
        workflowSweeper.sweep(workflowId);
        // Assertions, e.g. verify that tasks were repaired
    }

    @Test
    public void testUnack() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel();
        long workflowOffsetTimeout = 10L;

        when(queueDAO.getWorkflowModel(workflowId)).thenReturn(workflowModel);
        when(workflowModel.getTasks()).thenReturn(createTaskModels());

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verifyUnackTimeoutSet(workflowModel.getWorkflowId(), 11000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long expected = workflowOffsetTimeout + jitter;
        long actual = 45L + new Random().nextInt((int) (2 * workflowOffsetTimeout / 3 + 1)) - workflowOffsetTimeout / 3;
        assertThat(jitter, is(actual - workflowOffsetTimeout));
        assertThat(expected, is(actual));
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName("testWorkflowName");
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(Status.IN_PROGRESS);
        return workflowModel;
    }

    private Decision createDecision() {
        return TestUtils.createDecision("testDecisionId", "testDecisionName", null);
    }

    private List<TaskModel> createTaskModels() {
        return Arrays.asList(
                new TaskModel("task1Id", "task1Name", Status.IN_PROGRESS, null, null, null),
                new TaskModel("task2Id", "task2Name", Status.SCHEDULED, null, null, null),
                new TaskModel("task3Id", "task3Name", Status.COMPLETED, null, null, null)
        );
    }

    private void verifyNoUnackTimeoutSet() {
        Monitors monitor = monitors.getMonitor(MonitorType.UNACK_TIME, "testWorkflowName");
        assertFalse(monitor.isMonitoring());
    }

    private void verifyUnackTimeoutSet(String workflowId, long timeout) {
        Monitors monitor = monitors.getMonitor(MonitorType.UNACK_TIME, "testWorkflowName");
        assertTrue(monitor.isMonitoring());
        assertEquals(timeout, monitor.getMonitoringValue());
    }
}