import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution executionMock;

    @InjectMocks
    private TestRuntimeFlowableDelegate delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitCallable_Success() {
        // Arrange
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");
        doReturn(new TestCallable()).when(delegate).initCallable(any(DelegateExecution.class));

        // Act
        delegate.run(executionMock);

        // Assert
        assertNotNull(delegate.get());
    }

    @Test
    public void testInitCallable_ThrowsException() {
        // Arrange
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");
        doThrow(new RuntimeException()).when(delegate).initCallable(any(DelegateExecution.class));

        // Act and Assert
        assertThrows(ExecutionException.class, () -> delegate.run(executionMock));
    }

    @Test
    public void testGetTimeoutException() throws InterruptedException, ExecutionException {
        // Arrange
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");
        doReturn(new TestCallable()).when(delegate).initCallable(any(DelegateExecution.class));

        // Act and Assert
        assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.MILLISECONDS));
    }

    private class TestCallable implements Callable<Object> {
        @Override
        public Object call() throws Exception {
            return new Object();
        }
    }
}