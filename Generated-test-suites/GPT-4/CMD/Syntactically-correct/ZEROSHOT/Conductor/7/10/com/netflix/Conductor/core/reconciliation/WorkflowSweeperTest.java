package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.dao.QueueDAO;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private static final String SWEEPER_EXECUTOR_NAME = SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(sweeper, "workflowRepairService", workflowRepairService);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "test-workflow-id";
        sweeper.sweepAsync(workflowId);
        verify(sweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "test-workflow-id";
        when(workflowExecutor.decide(workflowId)).thenThrow(NotFoundException.class);
        sweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = sweeper.workflowOffsetWithJitter(3);
        assert(result >= 0 && result <= 6);
    }

    @Test
    public void testSweepWithNullWorkflowRepairService() {
        ReflectionTestUtils.setField(sweeper, "workflowRepairService", null);
        String workflowId = "test-workflow-id";
        sweeper.sweep(workflowId);
        verify(sweeper, never()).verifyAndRepairWorkflowTasks(workflowId);
        //Do needed assertions for other calls
    }

    //Add additional tests for other methods and cases
}
