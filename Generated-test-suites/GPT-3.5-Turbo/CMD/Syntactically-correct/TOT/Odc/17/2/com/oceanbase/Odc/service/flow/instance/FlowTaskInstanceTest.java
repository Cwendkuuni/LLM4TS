import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance();
    }

    @Test
    public void testBindServiceTask() {
        // Typical use case
        BaseRuntimeFlowableDelegate<?> targetTask = new BaseRuntimeFlowableDelegate<>();
        flowTaskInstance.bindServiceTask(targetTask);

        assertEquals(targetTask, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        // Typical use case
        flowTaskInstance.confirmExecute();

        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        // Typical use case
        flowTaskInstance.abort();

        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        // Typical use case
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<>());
        flowTaskInstance.cancel(false);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() {
        // Typical use case
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<>());
        assertNotNull(flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() {
        // Typical use case
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<>());
        assertNotNull(flowTaskInstance.getResult(5, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        // Typical use case
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

}