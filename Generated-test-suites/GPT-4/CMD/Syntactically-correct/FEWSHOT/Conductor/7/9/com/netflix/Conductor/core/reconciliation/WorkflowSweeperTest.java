package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {
    
    @Mock
    private QueueDAO queueDAO;
    
    @Mock
    private WorkflowExecutor workflowExecutor;
    
    @Mock
    private ConductorProperties conductorProperties;
    
    @Mock
    private WorkflowRepairService workflowRepairService;
    
    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitteredOffset = workflowSweeper.workflowOffsetWithJitter(60);
        assertTrue(jitteredOffset >= 40 && jitteredOffset <= 80);
    }

    @Test
    public void testSweepWorkflowNotFound() {
        when(workflowExecutor.decide("workflowId")).thenThrow(new NoSuchElementException());
        workflowSweeper.sweep("workflowId");
        verify(queueDAO).remove(anyString(), eq("workflowId"));
    }

    @Test
    public void testSweepWorkflowFoundTerminalStatus() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        when(workflowExecutor.decide("workflowId")).thenReturn(workflowModel);
        workflowSweeper.sweep("workflowId");
        verify(queueDAO).remove(anyString(), eq("workflowId"));
    }

    @Test
    public void testSweepErrorWhileRunningSweep() {
        when(workflowExecutor.decide("workflowId")).thenThrow(new RuntimeException());
        workflowSweeper.sweep("workflowId");
        verify(queueDAO).remove(anyString(), eq("workflowId"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(500);
        ArrayList<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel);
        when(workflowModel.getTasks()).thenReturn(tasks);
        when(workflowModel.getWorkflowId()).thenReturn("workflowId");
        workflowSweeper.unack(workflowModel, 1000);
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).setUnackTimeout(anyString(), anyString(), captor.capture());
        assertTrue(captor.getValue() >= taskModel.getWaitTimeout() * 1000);
    }

    @Test
    public void testSweepWorkflow() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);
        when(workflowExecutor.decide("workflowId")).thenReturn(workflowModel);
        workflowSweeper.sweep("workflowId");
        verify(queueDAO, never()).remove(anyString(), eq("workflowId"));
    }
}