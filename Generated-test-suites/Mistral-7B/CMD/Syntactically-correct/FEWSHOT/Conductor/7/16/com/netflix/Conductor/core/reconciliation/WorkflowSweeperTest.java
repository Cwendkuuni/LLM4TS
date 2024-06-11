package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutorImpl;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModelImpl;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties propertiesMock;

    @Mock
    private WorkflowExecutor workflowExecutorMock;

    @Mock
    private WorkflowRepairService workflowRepairServiceMock;

    @Mock
    private QueueDAO queueDAOMock;

    @Mock
    private Monitors monitorsMock;

    @Mock
    private TaskDef taskDefMock;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(propertiesMock.getAppId()).thenReturn("testAppId");
        when(propertiesMock.getWorkflowOffsetTimeout()).thenReturn(60L);

        WorkflowExecutorImpl workflowExecutorImpl = new WorkflowExecutorImpl();
        when(workflowExecutorMock).thenReturn(workflowExecutorImpl);

        WorkflowRepairService mockWorkflowRepairService = mock(WorkflowRepairService.class);
        when(workflowRepairServiceMock).thenReturn(mockWorkflowRepairService);

        QueueDAOImpl queueDAOImpl = new QueueDAOImpl();
        when(queueDAOMock).thenReturn(queueDAOImpl);

        when(monitorsMock.recordUnackTime(String.valueOf(WorkflowSweeper.class.getSimpleName()), Long.MAX_VALUE))
                .thenReturn(null);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";

        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId(workflowId);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setTaskDefinition(Optional.of(taskDefMock));
        taskModel1.setTaskId("task1");
        workflowModel.setTasks(new ArrayList<>(List.of(taskModel1)));

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.SCHEDULED);
        workflowModel.setTasks(new ArrayList<>(List.of(taskModel1, taskModel2)));

        when(queueDAOMock.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        workflowSweeper.sweep(workflowId);
    }
}