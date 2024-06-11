/*
 * Copyright 2022 Netflix, Inc.
 */

package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenWorkflowId_whenSweep_thenSuccess() {
        String workflowId = "workflowId";
            
        workflowSweeper.sweep(workflowId);

        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(anyString(), eq(workflowId));
    }

    @Test
    public void givenInvalidWorkflowId_whenSweep_thenError() {
        String invalidWorkflowId = "invalidWorkflowId";
        
        when(workflowExecutor.decide(invalidWorkflowId)).thenThrow(new NoSuchElementException());
        
        assertThatThrownBy(() -> workflowSweeper.sweep(invalidWorkflowId))
                .isInstanceOf(NoSuchElementException.class);
                
        verify(queueDAO).remove(anyString(), eq(invalidWorkflowId));
    }
    
    @Test
    public void givenWorkflowModel_whenUnack_thenSuccess() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        long workflowOffsetTimeout = 1200L;
        
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
    }
    
    @Test
    public void givenWorkflowOffsetTimeout_whenWorkflowOffsetWithJitter_thenSuccess() {
        long workflowOffsetTimeout = 1200L;
        
        workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
    }
}