package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Captor
    private ArgumentCaptor<String> workflowIdCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(new Long(60));
        when(properties.getSweperExecutorName()).thenReturn("testExecutorName");
    }

    @Test
    public void testSweep_existingWorkflow_verifiesAndRepairsTasks() {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenReturn(workflowModel);
        when(queueDAO.remove(QueueDAO.DECIDER_QUEUE, workflowModel.getWorkflowId()))
                .thenReturn(null);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowModel.getWorkflowId()))
                .thenReturn(true);

        workflowSweeper.sweep(workflowModel.getWorkflowId());

        verify(queueDAO, times(1)).remove(QueueDAO.DECIDER_QUEUE, workflowModel.getWorkflowId());
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowModel.getWorkflowId());
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(workflowRepairService);
    }

    @Test
    public void testSweep_notFoundWorkflow_removesItFromDeciderQueue() {
        when(workflowExecutor.decide(workflowIdCaptor.capture())).thenThrow(new NotFoundException());
        workflowSweeper.sweep(workflowIdCaptor.getValue());

        verify(queueDAO, times(1)).remove(QueueDAO.DECIDER_QUEUE, workflowIdCaptor.getValue());
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(workflowRepairService);
    }

    @Test
    public void testSweep_exceptionDuringSweep_setsUnackTimeout() {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenThrow(new RuntimeException());

        workflowSweeper.sweep(workflowModel.getWorkflowId());

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO, times(1)).setUnackTimeout(QueueDAO.DECIDER_QUEUE, workflowIdCaptor.capture(), timeoutCaptor.capture());
        Long expectedTimeout = workflowOffsetWithJitter(60L);
        assertEquals(expectedTimeout, timeoutCaptor.getValue() / 1000L);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(workflowRepairService);
    }

    private WorkflowModel createWorkflowModel() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowId("testWorkflowId");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(new TaskModel[] {taskModel});
        workflowModel.setWorkflowId("testWorkflowId");
        return workflowModel;
    }

    private long workflowOffsetWithJitter(long workflowOffsetTimeout) {
        long range = workflowOffsetTimeout / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        return workflowOffsetTimeout + jitter;
    }
}