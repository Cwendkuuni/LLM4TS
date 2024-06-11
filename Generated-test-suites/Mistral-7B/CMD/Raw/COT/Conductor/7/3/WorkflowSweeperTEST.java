import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private WorkflowRepairService workflowRepairService;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private ConductorProperties properties;

    @MockBean
    private Monitors monitors;

    @Autowired
    private Random random;

    private static final String WORKFLOW_ID = "1234567890";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync(WORKFLOW_ID).join();
        // Add assertions here if necessary
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(Status.RUNNING);
        when(workflowModel.getWorkflowId()).thenReturn(WORKFLOW_ID);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(workflowModel));

        doAnswer(invocation -> {
            // Add any logic to be mocked here
            return null;
        }).when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep(WORKFLOW_ID);

        // Add assertions here if necessary
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.empty());
        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test(expected = Exception.class)
    public void testSweepException() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(Status.RUNNING);
        when(workflowModel.getWorkflowId()).thenReturn(WORKFLOW_ID);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(workflowModel));
        doThrow(new RuntimeException("Exception occurred")).when(workflowExecutor).decide(anyString());
        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getTaskDefinition()).thenReturn(Optional.empty());
        when(taskModel.getResponseTimeoutSeconds()).thenReturn(10L);
        when(taskModel.getWaitTimeout()).thenReturn(20L);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));

        workflowSweeper.unack(mock(WorkflowModel.class), 30L);

        long expectedUnackTimeout = 31L;
        // Add assertions here if necessary
        Assert.assertEquals(expectedUnackTimeout, queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long expectedJitterRange = 15L;
        long expectedJitterValue = random.nextInt((int) (2 * expectedJitterRange + 1)) - expectedJitterRange;

        long actualWorkflowOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Add assertions here if necessary
        Assert.assertTrue(actualWorkflowOffset >= workflowOffsetTimeout - expectedJitterRange);
        Assert.assertTrue(actualWorkflowOffset <= workflowOffsetTimeout + expectedJitterRange);
    }
}