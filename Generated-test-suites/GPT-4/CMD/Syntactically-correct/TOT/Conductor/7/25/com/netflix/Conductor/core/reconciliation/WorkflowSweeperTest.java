package com.netflix.conductor.core.reconciliation;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

public class WorkflowSweeperTest {

    private WorkflowSweeper sweeper;

    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService repairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        workflowExecutor = mock(WorkflowExecutor.class);
        repairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);


        sweeper  = new WorkflowSweeper(workflowExecutor,Optional.ofNullable(repairService), properties, queueDAO);
    }

    /*
    * Testing sweepAsync
    */
    @Test
    public void testSweepAsync() {
        String workflowId = "test-id";
        sweeper.sweepAsync(workflowId);

        // add more assertions based on expected behavior
    }

    /*
    * Testing sweep
    */
    @Test
    public void testSweep() {
        String workflowId = "test-id";
        sweeper.sweep(workflowId);

        // add more assertions based on expected behavior
    }

    /*
    * Testing unack
    */
    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        long timeout = 100L;
        ReflectionTestUtils.invokeMethod(sweeper, "unack", workflowModel, timeout);

      // add more assertions based on expected behavior
    }

    /*
    * Testing workflowOffsetWithJitter
    */
    @Test
    public void testWorkflowOffsetWithJitter() {
        long timeout = 100L;
        long result = ReflectionTestUtils.invokeMethod(sweeper, "workflowOffsetWithJitter", timeout);

    // add assertions to confirm the result is within the expected range
    }
}