package com.netflix.conductor.core.reconciliation;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;
    
    private WorkflowSweeper sweeper;

    @BeforeEach
    public void setup() {
        sweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
    }

    @Test
    public void whenSweepAsyncIsCalled_thenFutureIsReturned() throws Exception {
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(Status.IN_PROGRESS);
      
        CompletableFuture<Void> future = sweeper.sweepAsync("1");
      
        assertThat(future.get()).isNull();
    }

    @Test
    public void whenSweepIsCalled_andWorkflowIsTerminal_thenWorkflowIsRemovedFromDeciderQueue() {
        String workflowId = "1";
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(Status.COMPLETED);

        sweeper.sweep(workflowId);
      
        verify(queueDAO).remove(eq(WorkflowContext.DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void whenSweepIsCalled_andWorkflowRepairServiceIsNotNull_thenWorkflowIsRepaired() {
        when(workflowRepairService.orElse(null)).thenReturn(new WorkflowRepairService());

        sweeper.sweep("1");
      
        verify(workflowRepairService.get()).verifyAndRepairWorkflowTasks("1");
    }

    // add more tests related to sweep, unack and workflowOffsetWithJitter methods.
}