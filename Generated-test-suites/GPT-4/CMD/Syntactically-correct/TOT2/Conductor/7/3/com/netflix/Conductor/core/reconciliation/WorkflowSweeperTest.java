package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.reconciliation.*;
import com.netflix.conductor.model.WorkflowModel;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void sweepAsyncTest() {
        assertNotNull(workflowSweeper.sweepAsync("workflowId"));
    }

    @Test(expected = Exception.class)
    public void sweepTest_ExceptionScenario() {
        when(workflowExecutor.decide(anyString())).thenThrow(new MockNotFoundException());
        workflowSweeper.sweep("workflowId");
    }

    @Test
    public void sweepTest_NormalScenario() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        workflowSweeper.sweep("workflowId");
    }

    @Test
    public void unackTest() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn("workflowId");
        workflowSweeper.unack(workflowModel, 100);
    }

    @Test
    public void workflowOffsetWithJitterTest_RangeCheck() {
        long workflowOffsetTimeout = 45;
        long offsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(offsetWithJitter >=30 && offsetWithJitter <=60);
    }
}

class MockNotFoundException extends RuntimeException {
}

