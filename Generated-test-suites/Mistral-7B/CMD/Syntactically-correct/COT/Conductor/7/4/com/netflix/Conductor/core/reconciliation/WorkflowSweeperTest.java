import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.sweeper.SweeperException;
import com.netflix.conductor.dao.QueueDAOMock;
import com.netflix.conductor.model.TaskDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModelMock;
import com.netflix.conductor.utils.Utils;

public class WorkflowSweeperTest {

    private static final long WORKFLOW_OFFSET_TIMEOUT = 45;
    private static final String WORKFLOW_ID = "workflow-id";

    private WorkflowSweeper sweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        properties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = new QueueDAOMock();
        workflowRepairService = mock(WorkflowRepairService.class);

        when(properties.getAppId()).thenReturn("app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModelMock());
        when(queueDAO.remove(Utils.DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
        when(queueDAO.setUnackTimeout(Utils.DECIDER_QUEUE, WORKFLOW_ID, 0)).thenReturn(true);

        sweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.ofNullable(workflowRepairService),
                properties,
                queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = sweeper.sweepAsync(WORKFLOW_ID);
        future.join();
        // Additional assertions if needed
    }

    @Test
    public void testSweep() {
        sweeper.sweep(WORKFLOW_ID);
    }

    @Test(expected = SweeperException.class)
    public void testSweepNotFoundException() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));
        sweeper.sweep(WORKFLOW_ID);
    }

    @Test(expected = Exception.class)
    public void testSweepOtherException() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new RuntimeException("Other error"));
        sweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModelMock();
        sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        assertEquals(45, sweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT));
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks() {
        WorkflowModel workflowModel = new WorkflowModelMock();
        workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID);

        // Assertions for verification and repairing of tasks
    }
}

class WorkflowModelMock extends WorkflowModel {

    @Override
    public Status getStatus() {
        return Status.RUNNING;
    }

    @Override
    public long getWorkflowId() {
        return 1L;
    }

    @Override
    public String getWorkflowName() {
        return "workflow-name";
    }

    @Override
    public List<TaskModel> getTasks() {
        return Arrays.asList(
                new TaskModel() {
                    @Override
                    public Status getStatus() {
                        return Status.IN_PROGRESS;
                    }

                    @Override
                    public Optional<TaskDef> getTaskDefinition() {
                        return Optional.empty();
                    }

                    @Override
                    public String getTaskId() {
                        return "task-id-1";
                    }

                    @Override
                    public long getTaskIdVersion() {
                        return 1L;
                    }

                    @Override
                    public String getTaskDefinitionName() {
                        return "task-definition-name-1";
                    }

                    @Override
                    public long getStartTime() {
                        return Instant.now().minusSeconds(10).toEpochMilli();
                    }

                    @Override
                    public long getEndTime() {
                        return Instant.now().minusSeconds(5).toEpochMilli();
                    }

                    @Override
                    public long getResponseTimeoutSeconds() {
                        return 10;
                    }

                    @Override
                    public TaskType getTaskType() {
                        return TaskType.TASK_TYPE_STANDARD;
                    }
                },
                new TaskModel() {
                    @Override
                    public Status getStatus() {
                        return Status.SCHEDULED;
                    }

                    @Override
                    public Optional<TaskDef> getTaskDefinition() {
                        return Optional.of(new TaskDef());
                    }

                    @Override
                    public String getTaskId() {
                        return "task-id-2";
                    }

                    @Override
                    public long getTaskIdVersion() {
                        return 1L;
                    }

                    @Override
                    public String getTaskDefinitionName() {
                        return "task-definition-name-2";
                    }

                    @Override
                    public long getStartTime() {
                        return Instant.now().minusSeconds(20).toEpochMilli();
                    }

                    @Override
                    public long getEndTime() {
                        return Instant.now().toEpochMilli();
                    }

                    @Override
                    public long getWaitTimeout() {
                        return 20;
                    }

                    @Override
                    public TaskType getTaskType() {
                        return TaskType.TASK_TYPE_WAIT;
                    }
                }
        );
    }
}

class QueueDAOMock implements QueueDAO {

    @Override
    public boolean remove(String queueName, String key) {
        return true;
    }

    @Override
    public boolean setUnackTimeout(String queueName, String key, long timeout) {
        return true;
    }
}

class WorkflowExecutorMock implements WorkflowExecutor {

    @Override
    public WorkflowModel decide(String workflowId) {
        return new WorkflowModelMock();
    }
}

class WorkflowRepairService {

    public void verifyAndRepairWorkflowTasks(String workflowId) {
        // Implementation for testing
    }
}