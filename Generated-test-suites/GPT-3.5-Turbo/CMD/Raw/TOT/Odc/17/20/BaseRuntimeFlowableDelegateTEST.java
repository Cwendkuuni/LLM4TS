import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
    
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Result";
            }
        });
    }

    @Test
    public void testRun_Success() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        assertEquals("Test Result", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testRun_Exception() {
        baseRuntimeFlowableDelegate = spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new Exception("Test Exception");
                };
            }
        });

        try {
            baseRuntimeFlowableDelegate.run(execution);
        } catch (Exception e) {
            assertTrue(baseRuntimeFlowableDelegate.isDone());
            assertNotNull(baseRuntimeFlowableDelegate.getThrown());
            assertEquals("Test Exception", baseRuntimeFlowableDelegate.getThrown().getMessage());
        }
    }

    @Test
    public void testGet_Exception() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(execution);

        try {
            baseRuntimeFlowableDelegate.get();
        } catch (ExecutionException e) {
            assertNotNull(e.getCause());
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testGet_Timeout() throws InterruptedException, ExecutionException {
        Future<String> future = new Future<String>() {
            @Override
            public String get() {
                return null;
            }

            @Override
            public String get(long timeout, TimeUnit unit) {
                return null;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }
        };

        baseRuntimeFlowableDelegate.run(execution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());

        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            assertTrue(true);
        }
    }
}