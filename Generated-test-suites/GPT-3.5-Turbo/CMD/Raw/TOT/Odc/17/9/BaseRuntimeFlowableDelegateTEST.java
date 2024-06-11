import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Spy
    private BaseRuntimeFlowableDelegate<TestObject> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Callable<TestObject> callable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitCallable() {
        doReturn(callable).when(baseRuntimeFlowableDelegate).initCallable(any());
        Callable<TestObject> result = baseRuntimeFlowableDelegate.initCallable(execution);
        assertEquals(callable, result);
    }

    @Test(expected = ExecutionException.class)
    public void testGetThrowsExecutionException() throws Exception {
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutThrowsTimeoutException() throws Exception {
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }
}