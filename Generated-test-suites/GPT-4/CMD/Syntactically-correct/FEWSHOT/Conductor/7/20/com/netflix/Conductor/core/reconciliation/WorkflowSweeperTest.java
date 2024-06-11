package com.netflix.conductor.core.reconciliation;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
    }

    @Test
    public void sweepAsyncTest() throws Exception {

        // initialize the optional
        WorkflowRepairService repairService = new WorkflowRepairService();
        when(workflowRepairService.orElse(null)).thenReturn(repairService);

        // verify that the asynchronous method runs without throwing an exception
        String workflowId = "workflowId";
        workflowSweeper.sweepAsync(workflowId);
        
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void sweep_noExceptions_workflowNotFound() {
        String workflowId = "workflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException());

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void unack_InProgressTask() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_FORK);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        workflowSweeper.unack(workflowModel, 1000L);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    // To test random offset, we use the same seed for two mocked Random objects and check if they return the same result.
    @Test
    public void workflowOffsetWithJitter() {
        Random random = new Random(1234);
        long workflowOffsetTimeout = 45;
        
        long resultWithSeed = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout, random);
        random = new Random(1234);
        long expected = workflowOffsetTimeout + random.nextInt((int)(2 * workflowOffsetTimeout/3 + 1)) - workflowOffsetTimeout/3;
        
        assertEquals(expected, resultWithSeed);
    }
}