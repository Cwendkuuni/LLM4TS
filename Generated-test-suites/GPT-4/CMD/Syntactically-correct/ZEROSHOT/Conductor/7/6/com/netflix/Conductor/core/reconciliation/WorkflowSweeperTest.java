package com.netflix.conductor.core.reconciliation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowContext workflowContext;
    
    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper sweeper;

    private String workflowId = "test-workflow-id";

    @Before
    public void setup(){
        when(properties.getAppId()).thenReturn("test-app-id");
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = sweeper.sweepAsync(workflowId);
        verify(sweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        sweeper.sweep(workflowId);
        // Here we expect to call decide method at least once.
        verify(workflowExecutor, atLeastOnce()).decide(workflowId);
        // Also we expect to call setUnackTimeout method at least once.
        verify(queueDAO, atLeastOnce()).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel);
        when(workflowModel.getTasks()).thenReturn(tasks);
        sweeper.unack(workflowModel, 10L);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = sweeper.workflowOffsetWithJitter(45L);
        assertTrue(result >= 30L && result <= 60L);
    }
}