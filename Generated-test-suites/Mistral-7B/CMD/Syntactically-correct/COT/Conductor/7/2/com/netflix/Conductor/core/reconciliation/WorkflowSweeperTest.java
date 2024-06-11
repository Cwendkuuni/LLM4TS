import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = WorkflowSweeper.class)
@ActiveProfiles("test")
public class WorkflowSweeperTest {

    @MockBean
    private ConductorProperties properties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private WorkflowRepairService workflowRepairService;

    @MockBean
    private QueueDAO queueDAO;

    @Autowired
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        Mockito.when(properties.getWorkflowOffsetTimeout())
                .thenReturn(Instant.ofEpochSecond(10));
        Mockito.when(properties.getAppId()).thenReturn("testAppId");
    }

    // Test for sweepAsync method
    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        sweeper.sweepAsync(workflowId);
        verify(workflowExecutor).decide(workflowId);
    }

    // Test for sweep method: basic functionality
    @Test
    public void testSweep_BasicFunctionality() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        sweeper.sweep(workflowId);
        verify(workflowExecutor).decide(workflowId);
    }

    // Test for sweep method: workflowNotFoundException
    @Test(expected = NotFoundException.class)
    public void testSweep_WorkflowNotFoundException() {
        String workflowId = "testWorkflowId";
        Mockito.doThrow(new NotFoundException("Workflow not found."))
                .when(workflowExecutor)
                .decide(workflowId);
        sweeper.sweep(workflowId);
    }

    // Test for sweep method: exception handling
    @Test
    public void testSweeper_ExceptionHandling() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        sweeper.sweep(workflowId);
        verify(Monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    // Test for unack method: normal case
    @Test
    public void testUnack_NormalCase() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        sweeper.unack(workflowModel);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), anyLong());
    }

    // Test for unack method: taskInProgress
    @Test
    public void testUnack_TaskInProgress() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        Mockito.when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        sweeper.unack(workflowModel);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), anyLong());
    }

    // Test for unack method: taskScheduled
    @Test
    public void testUnack_TaskScheduled() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        Mockito.when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        sweeper.unack(workflowModel);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), anyLong());
    }

    // Test for workflowOffsetWithJitter method
    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 1000L;
        long jitter = new Random().nextInt(2 * (workflowOffsetTimeout / 3) + 1) - (workflowOffsetTimeout / 3);
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        Assert.assertEquals(workflowOffsetTimeout + jitter, result);
    }
}