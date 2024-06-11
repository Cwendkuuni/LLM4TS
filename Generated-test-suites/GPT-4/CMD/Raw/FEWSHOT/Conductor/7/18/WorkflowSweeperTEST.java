package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private TaskDef taskDef;

    @Before
    public void setup() {
        when(properties.getAppId()).thenReturn("testAppId");
        when(workflowModel.getWorkflowId()).thenReturn("testWorkflowId");
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
    }

    @Test
    public void sweepAsyncTest() {
        doNothing().when(workflowSweeper).sweep(anyString());
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("testWorkflowId");
        result.join();
        verify(workflowSweeper).sweep("testWorkflowId");
    }

    @Test
    public void sweepTest() {
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        workflowSweeper.sweep("testWorkflowId");
        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void sweepTest_NotFoundException() {
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Workflow not Found Exception"));
        workflowSweeper.sweep("testWorkflowId");
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void unackTest_IN_PROGRESS() {
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(123);
        workflowSweeper.unack(workflowModel, 123L);
        verify(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long workflowOffsetTimeout = 50L;
        assert(workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout) >= 33);
        assert(workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout) <= 67);
    }
}