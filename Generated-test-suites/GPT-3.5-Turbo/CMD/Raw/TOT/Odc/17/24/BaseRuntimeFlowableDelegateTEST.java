import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private Callable<String> callable;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test Callable";
        }
    };

    @Before
    public void setup() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
    }

    @Test
    public void testInitCallable() {
        Callable<String> result = baseRuntimeFlowableDelegate.initCallable(delegateExecution);
        assertNotNull(result);
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        assertEquals("Test Callable", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws Exception {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
    }

}