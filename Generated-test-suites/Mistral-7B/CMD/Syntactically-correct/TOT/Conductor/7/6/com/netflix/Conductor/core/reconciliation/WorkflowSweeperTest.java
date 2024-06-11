package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.mockito. ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WorkflowSweeperTest {

    private ApplicationContext applicationContext;
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowSweeper workflowSweeper;
    private QueueDAO queueDAOMock;
    private WorkflowRepairService workflowRepairServiceMock;

    @Before
    public void setup() {
        applicationContext = mock(ApplicationContext.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);
        queueDAOMock = mock(QueueDAO.class);
        workflowRepairServiceMock = mock(WorkflowRepairService.class);

        doReturn(workflowExecutorMock).when(applicationContext).getBean(WorkflowExecutor.class);
        doReturn(queueDAOMock).when(applicationContext).getBean(QueueDAO.class);
        doReturn(workflowRepairServiceMock).when(applicationContext).getBean(WorkflowRepairService.class);

        workflowSweeper = new WorkflowSweeper(
                applicationContext, Optional.of(workflowRepairServiceMock), Mockito.mock(ConductorProperties.class),
                queueDAOMock);
    }

    @Test
    public void testSweepSuccess() {
        WorkflowModel workflowModel = createWorkflowModel();
        doReturn(workflowModel).when(workflowExecutorMock).decide(ArgumentMatchers.anyString());

        workflowSweeper.sweep("testWorkflowId");

        verify(queueDAOMock, times(1)).setUnackTimeout(anyString(), ArgumentMatchers.eq("testWorkflowId"),
                anyLong());
        assertEquals(Status.IN_PROGRESS, workflowModel.getTasks().get(0).getStatus());
    }

    @Test
    public void testSweepNotFound() {
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutorMock).decide(anyString());

        workflowSweeper.sweep("testWorkflowId");

        verify(queueDAOMock, times(1)).remove(anyString(), "testWorkflowId");
    }

    @Test
    public void testSweepError() {
        WorkflowModel workflowModel = createWorkflowModel();
        doThrow(new RuntimeException("Test error")).when(workflowExecutorMock).decide(anyString());

        workflowSweeper.sweep("testWorkflowId");

        verify(queueDAOMock, times(1)).setUnackTimeout(anyString(), ArgumentMatchers.eq("testWorkflowId"),
                anyLong());
        Monitors.error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = createWorkflowModel();
        doReturn(workflowModel).when(workflowExecutorMock).decide(ArgumentMatchers.anyString());

        workflowSweeper.unack(workflowModel, 10000L);

        verify(queueDAOMock, times(1)).setUnackTimeout(anyString(), ArgumentMatchers.eq(workflowModel.getWorkflowId()),
                10000L);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setName("testWorkflowName");
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setWorkflowId("testWorkflowId");
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(new TaskModel());
        workflowModel.setTasks(tasks);
        return workflowModel;
    }
}