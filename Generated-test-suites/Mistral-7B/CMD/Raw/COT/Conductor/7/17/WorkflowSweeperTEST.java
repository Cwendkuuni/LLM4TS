package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowOffsetService;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.test.mocks.TestWorkflowExecutor;
import com.netflix.conductor.test.mocks.TestWorkflowModel;
import com.netflix.conductor.test.mocks.TestWorkflowRepairService;
import com.netflix.conductor.test.mocks.TestWorkflowSweeper;

import java.time.Instant;
import java.util.Optional;

public class WorkflowSweeperTest {

    @MockBean
    private WorkflowRepairService workflowRepairService;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private Monitors monitors;

    @MockBean
    private SchedulerConfiguration schedulerConfiguration;

    @MockBean
    private ConductorProperties conductorProperties;

    @Autowired
    @InjectMocks
    private TestWorkflowSweeper sweeperUnderTest;

    @Autowired
    private TestWorkflowExecutor workflowExecutor;

    @Autowired
    private TestWorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(sweeperUnderTest, "queueDAO", queueDAO);
        ReflectionTestUtils.setFieldValue(sweeperUnderTest, "workflowExecutor", workflowExecutor);
        ReflectionTestUtils.setFieldValue(sweeperUnderTest, "workflowRepairService", workflowRepairService);
        ReflectionTestUtils.setFieldValue(sweeperUnderTest, "properties", conductorProperties);
    }

    @Test
    public void testSweepAsyncMethod() {
        String workflowId = "testWorkflowId";
        sweeperUnderTest.sweepAsync(workflowId);
        assertTrue(sweeperUnderTest.isSweeping());
    }

    @Test
    public void testSweepMethodNormalFlow() {
        String workflowId = "testWorkflowId";
        sweeperUnderTest.sweep(workflowId);

        WorkflowModel workflow = new TestWorkflowModel();
        boolean expectedResult = false;
        ReflectionTestUtils.setFieldValue(sweeperUnderTest, "workflow", workflow);

        sweeperUnderTest.unack(workflow, 1000L);

        boolean actualResult = sweeperUnderTest.isWorkflowUnackNow(workflowId);

        assertTrue(actualResult);
    }

    @Test
    public void testSweepMethodNotFoundException() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = null;

        try {
            sweeperUnderTest.sweep(workflowId);
        } catch (NotFoundException e) {
            workflow = new TestWorkflowModel();
            ReflectionTestUtils.setFieldValue(sweeperUnderTest, "workflow", workflow);
        }

        boolean expectedResult = false;
        boolean actualResult = sweeperUnderTest.isWorkflowUnackNow(workflowId);

        assertFalse(actualResult);
    }

    @Test
    public void testSweepMethodException() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new TestWorkflowModel();

        try {
            sweeperUnderTest.sweep(workflowId);
        } catch (Exception e) {
            workflow.setStatus(Status.ERROR);
            ReflectionTestUtils.setFieldValue(sweeperUnderTest, "workflow", workflow);
        }

        long expectedUnackTimeout = 5000L;
        long actualUnackTimeout = sweeperUnderTest.getUnackTimeout(workflowId);

        assertTrue(actualUnackTimeout == expectedUnackTimeout);
    }

    @Test
    public void testUnackMethodNormalFlow() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new TestWorkflowModel();

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        workflow.setTasks(new TaskModel[]{taskModel});

        ReflectionTestUtils.setFieldValue(sweeperUnderTest, "workflow", workflow);

        long expectedPostponeDuration = 10L;
        long actualPostponeDuration = sweeperUnderTest.getPostponeDuration(workflowId);

        assertTrue(actualPostponeDuration == expectedPostponeDuration);
    }

    @Test
    public void testUnackMethodScheduledTask() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new TestWorkflowModel();

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        workflow.setTasks(new TaskModel[]{taskModel});

        ReflectionTestUtils.setFieldValue(sweeperUnderTest, "workflow", workflow);

        long expectedPostponeDuration = 20L;
        long actualPostponeDuration = sweeperUnderTest.getPostponeDuration(workflowId);

        assertTrue(actualPostponeDuration == expectedPostponeDuration);
    }

    @Test
    public void testUnackMethodWorkflowTimeout() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new TestWorkflowModel();

        ReflectionTestUtils.setFieldValue(sweeperUnderTest, "workflow", workflow);

        long expectedPostponeDuration = 30L;
        long actualPostponeDuration = sweeperUnderTest.getPostponeDuration(workflowId);

        assertTrue(actualPostponeDuration == expectedPostponeDuration);
    }

    @Test
    public void testWorkflowOffsetWithJitterMethod() {
        long workflowOffsetTimeout = 60L;

        long expectedJitterRange = 20L;
        long expectedJitter = new Random().nextInt((int) (2 * expectedJitterRange + 1)) - expectedJitterRange;
        long expectedWorkflowOffset = workflowOffsetTimeout + expectedJitter;

        long actualWorkflowOffset = sweeperUnderTest.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(actualWorkflowOffset == expectedWorkflowOffset);
    }

    @Test(expected = IllegalStateException.class)
    public void testWorkflowOffsetWithJitterMethodInvalidInput() {
        long workflowOffsetTimeout = -10L;
        sweeperUnderTest.workflowOffsetWithJitter(workflowOffsetTimeout);
    }
}