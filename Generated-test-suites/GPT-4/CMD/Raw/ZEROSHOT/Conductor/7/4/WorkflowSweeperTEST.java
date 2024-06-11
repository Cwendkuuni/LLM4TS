
package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.workflowSweeper = new WorkflowSweeper(workflowExecutor,
                Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void sweepAsyncTest() {
        String workflowId = "workflowId";

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        verify(workflowSweeper, times(1)).sweepAsync(workflowId);
        assert(result.isDone());
    }

    @Test
    public void sweepTest() {
        String workflowId = "workflowId";

        doNothing().when(queueDAO).remove(anyString(), anyString());
        when(properties.getAppId()).thenReturn("appId");
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        when(workflowModelMock.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(2)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void unackTest() {
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        TaskModel taskModelMock = mock(TaskModel.class);
        when(workflowModelMock.getTasks()).thenReturn(Collections.singletonList(taskModelMock));
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(taskModelMock.getTaskType()).thenReturn(TaskType.TASK_TYPE_HUMAN);
        when(taskModelMock.getWaitTimeout()).thenReturn(0L);

        workflowSweeper.unack(workflowModelMock, 30L);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long result = workflowSweeper.workflowOffsetWithJitter(30L);
        assertTrue(result >= 20 && result <= 40);
    }
}
