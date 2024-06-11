package com.netflix.conductor.core.reconciliation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.conductor.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.TaskDef;

import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Test
    public void testSweep() {
        String workflowId = UUID.randomUUID().toString();
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(mockWorkflowModel);
        when(mockWorkflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        workflowSweeper.sweep(workflowId);
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(0)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);
        List<TaskModel> mockTaskList = new ArrayList<>();
        TaskModel mockTask = mock(TaskModel.class);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(mockTask.getTaskType()).thenReturn(TaskType.TASK_TYPE_SIMPLE);
        when(mockTask.getResponseTimeoutSeconds()).thenReturn(0);
        mockTaskList.add(mockTask);
        when(mockWorkflowModel.getTasks()).thenReturn(mockTaskList);
        workflowSweeper.unack(mockWorkflowModel, 60);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, mockWorkflowModel.getWorkflowId(), 60000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitteredValue = workflowSweeper.workflowOffsetWithJitter(60);
        long lower = 60 - 60 / 3;
        long upper = 60 + 60 / 3;
        assertTrue(jitteredValue >= lower && jitteredValue <= upper);
    }
}
