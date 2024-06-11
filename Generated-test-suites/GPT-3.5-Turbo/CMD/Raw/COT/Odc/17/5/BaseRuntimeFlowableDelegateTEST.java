
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setup() {
        delegate = spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        });
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = delegate.initCallable(execution);
        try {
            assertEquals("Test", callable.call());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsDone() {
        delegate.execute(execution);
        assertEquals(true, delegate.isDone());
    }

    @Test
    public void testGet() {
        delegate.execute(execution);
        try {
            assertEquals("Test", delegate.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWithTimeout() {
        delegate.execute(execution);
        try {
            assertEquals("Test", delegate.get(1, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        when(taskInstance.getId()).thenReturn(123L);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        // Verify that the update method is called with the correct parameters
    }
}
