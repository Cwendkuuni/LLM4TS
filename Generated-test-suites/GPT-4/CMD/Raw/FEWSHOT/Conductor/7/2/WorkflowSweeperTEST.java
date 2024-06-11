package com.netflix.conductor.core.reconciliation;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId="1";
        CompletableFuture<Void> future=workflowSweeper.sweepAsync(workflowId);
        assertTrue(future.isDone());
    }

    @Test
    public void testSweep() {
        String workflowId="1";
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, atLeastOnce()).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitteredOffset = workflowSweeper.workflowOffsetWithJitter(1000);
        assertTrue(jitteredOffset >= 666 && jitteredOffset <= 1333);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId("1");
        TaskModel task = new TaskModel();
        task.setTaskType(TaskType.TASK_TYPE_HUMAN);
        task.setStatus(TaskModel.Status.SCHEDULED);
        TaskDef taskDef = new TaskDef();
        taskDef.setPollTimeoutSeconds(100);
        Optional<TaskDef> optTaskDefinition = Optional.of(taskDef);
        task.setTaskDefinition(optTaskDefinition);
        workflow.getTasks().add(task);
        workflowSweeper.unack(workflow, 1000);
        verify(queueDAO, atLeastOnce()).setUnackTimeout(DECIDER_QUEUE,
                workflow.getWorkflowId(), 101000);
    }
}