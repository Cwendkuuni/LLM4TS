import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

public class FlowTaskInstanceTest {

    @Test
    public void testBindServiceTask() {
        // Mock objects
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);

        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.bindServiceTask(mockDelegate);

        // Validation
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testCancel() {
        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();

        // Validation
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.cancel(true));
    }

    @Test
    public void testIsDone() {
        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();

        // Validation
        assertThrows(UnsupportedOperationException.class, flowTaskInstance::isDone);
    }

    @Test
    public void testGetResult() {
        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();

        // Validation
        assertThrows(UnsupportedOperationException.class, flowTaskInstance::getResult);
    }

    @Test
    public void testGetResultWithTimeout() {
        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();

        // Validation
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testCompleteWithAborted() {
        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();

        // Validation
        assertThrows(UnsupportedOperationException.class, flowTaskInstance::abort);
    }

}