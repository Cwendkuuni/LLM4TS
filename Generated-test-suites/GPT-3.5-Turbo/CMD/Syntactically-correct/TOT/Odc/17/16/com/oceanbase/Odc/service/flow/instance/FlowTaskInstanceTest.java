import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        // Initialize the FlowTaskInstance object for testing
        flowTaskInstance = new FlowTaskInstance(/* pass necessary arguments */);
    }

    @Test
    public void testBindServiceTask() {
        // Test binding a service task to the flow task instance
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTask);
        assertEquals(flowTaskInstance.getTargetTaskHandle(), targetTask);
    }

    @Test
    public void testConfirmExecute() {
        // Test confirming the execution of the flow task instance
        flowTaskInstance.confirmExecute();
        // Add assertions here
    }

    @Test
    public void testAbort() {
        // Test aborting the flow task instance
        flowTaskInstance.abort();
        // Add assertions here
    }

    @Test
    public void testCancel() {
        // Test cancelling the flow task instance
        // Add assertions here
    }

    @Test
    public void testIsDone() {
        // Test checking if the flow task instance is done
        // Add assertions here
    }

    @Test
    public void testGetResult() {
        // Test getting the result of the flow task instance
        // Add assertions here
    }

    @Test
    public void testGetResultWithTimeout() {
        // Test getting the result of the flow task instance with timeout
        // Add assertions here
    }

    @Test
    public void testResourceType() {
        // Test getting the resource type of the flow task instance
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        // Test getting the target delegate class of the flow task instance
        // Add assertions here
    }

    // Add more test cases for other methods in FlowTaskInstance class

}