package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
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
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutor.Decision;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO.UnackTimeoutException;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@ComponentScan(basePackages = "com.netflix.conductor.core.reconciliation")
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutorMock;

    @Mock
    private WorkflowRepairService workflowRepairServiceMock;

    @Mock
    private QueueDAO queueDAOMock;

    @Mock
    private ConductorProperties conductorPropertiesMock;

    @Mock
    private Monitors monitorsMock;

    @Mock
    private TaskDef taskDefMock;

    @Mock
    private TaskModel taskModelMock;

    @Mock
    private WorkflowModel workflowModelMock;

    @Mock
    private WorkflowDefinition workflowDefinitionMock;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        when(workflowExecutorMock.decide(anyString())).thenAnswer(
                invocation -> new Decision(Optional.ofNullable(workflowModelMock), Decision.Type.DECIDE));
        when(workflowRepairServiceMock.verifyAndRepairWorkflowTasks(anyString())).thenReturn(null);
    }

    @After
    public void teardownMocks() {
        verifyNoMoreInteractions(workflowExecutorMock, workflowRepairServiceMock, queueDAOMock,
                conductorPropertiesMock, monitorsMock, taskDefMock, taskModelMock, workflowModelMock,
                workflowDefinitionMock);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweepMethodBasicFunctionality() {
        String workflowId = "testWorkflowId";
        doNothing().when(workflowSweeper).sweep(workflowId);
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertTrue(result.join());
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFoundException() {
        String workflowId = "testWorkflowId";
        doThrow(new NotFoundException("Test Not Found Exception")).when(workflowExecutorMock)
                .decide(workflowId);
        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testSweepWithWorkflowModel() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutorMock, times(1)).decide(workflowId);
        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test(expected = UnackTimeoutException.class)
    public void testQueueDAOSetUnackTimeoutException() {
        String workflowId = "testWorkflowId";
        when(queueDAOMock.setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong()))
                .thenThrow(new UnackTimeoutException("Test Unack Timeout Exception"));
        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testUnackMethodBasicFunctionality() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowSweeper.unack(workflowModel, 10L);
        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 11000L);
    }

    @Test
    public void testVerifyTasks() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        verify(workflowRepairServiceMock, times(1)).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testVerifyTasksNoTasks() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Collections.emptyList());
        workflowSweeper.sweep(workflowId);
        verify(workflowRepairServiceMock, times(0)).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30L && result <= 60L);
    }
}