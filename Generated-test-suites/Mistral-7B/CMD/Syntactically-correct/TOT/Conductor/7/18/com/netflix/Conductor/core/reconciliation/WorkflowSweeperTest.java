package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.WorkflowExecutionException;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.model.WorkflowModel.WorkflowType;
import com.netflix.conductor.model.workflow.WorkflowDefinition;

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
        when(properties.getAppId()).thenReturn("test-app");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(120L);
        when(queueDAO.remove(Utils.DECIDER_QUEUE, "workflow-id")).thenReturn(true);
        when(queueDAO.setUnackTimeout(Utils.DECIDER_QUEUE, "workflow-id", 1300L)).thenReturn(true);
        when(monitors.recordUnackTime("workflow-name", 1L)).thenReturn(true);
    }

    @After
    public void teardown() {
        reset(properties, workflowExecutor, workflowRepairService, queueDAO, monitors);
    }

    @Test
    public void testSweeperWithValidWorkflow() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow-id");
        workflow.setName("workflow-name");
        workflow.setStatus(WorkflowStatus.SCHEDULED);
        workflow.setType(WorkflowType.WORKFLOW);
        workflow.setWorkflowDefinition(new WorkflowDefinition());
        ReflectionTestUtils.setFieldValue(sweeper, "workflowRepairService", workflowRepairService);

        sweeper.sweep("workflow-id");
        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, "workflow-id", 13300L);
        verify(monitors, times(1)).recordUnackTime("workflow-name", 1L);
    }

    @Test
    public void testSweeperWithNotFoundWorkflow() {
        doThrow(new NotFoundException("")).when(workflowExecutor).decide("workflow-id");

        sweeper.sweeper("workflow-id");
        verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, "workflow-id");
        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, "workflow-id", 1300L);

        verify(monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(log, times(1)).info("Workflow NOT found for id:{}. Removed it from decider queue", "workflow-id");
    }

    @Test(expected = WorkflowExecutionException.class)
    public void testSweeperWithException() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow-id");
        workflow.setName("workflow-name");
        workflow.setStatus(WorkflowStatus.SCHEDULED);
        workflow.setType(WorkflowType.WORKFLOW);
        workflow.setWorkflowDefinition(new WorkflowDefinition());

        doThrow(new RuntimeException("test-exception")).when(workflowExecutor).decide("workflow-id");

        sweeper.sweeper("workflow-id");
        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, "workflow-id", 1300L);
        verify(monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(log, times(1)).error("Error running sweep for workflow-id", new RuntimeException("test-exception"));

        throw new WorkflowExecutionException("test-exception");
    }
}