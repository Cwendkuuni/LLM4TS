import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("testWorkflowId");
        assertNull(result.join());
    }

    @Test
    public void testSweep() {
        WorkflowModel mockWorkflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowExecutor.decide("testWorkflowId")).thenReturn(mockWorkflowModel);

        workflowSweeper.sweep("testWorkflowId");

        // Add assertions here
    }

    @Test
    public void testUnack() {
        WorkflowModel mockWorkflowModel = Mockito.mock(WorkflowModel.class);
        workflowSweeper.unack(mockWorkflowModel, 60);

        // Add assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);

        // Add assertions here
    }

    // Add more test cases for edge cases and error scenarios

}