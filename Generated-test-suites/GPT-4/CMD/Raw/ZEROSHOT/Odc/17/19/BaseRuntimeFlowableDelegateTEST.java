
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.TaskExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private Callable<Object> callable;

    private final BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Object>() {
        @Override
        protected Callable<Object> initCallable(DelegateExecution execution) {
            return callable;
        }
    };

    @Before
    public void setup() throws Exception {
        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn("id");
        Mockito.when(callable.call()).thenReturn(new Object());
        baseRuntimeFlowableDelegate.run(delegateExecution);
    }

    @Test
    public void testExecute() throws Exception {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
    }

    @Test
    public void testIsDone() {
        Assert.assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        Assert.assertNotNull(baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetException() throws Exception {
        Mockito.when(callable.call()).thenThrow(new Exception());
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        Assert.assertNotNull(baseRuntimeFlowableDelegate.get(1000, java.util.concurrent.TimeUnit.MILLISECONDS));
    }

    @Test(expected = java.util.concurrent.TimeoutException.class)
    public void testGetTimeoutException() throws Exception {
        BaseRuntimeFlowableDelegate<Object> instance = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(2000);
                    return new Object();
                };
            }
        };
        instance.run(delegateExecution);
        instance.get(1000, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGetExceptionWithTimeout() throws Exception {
        Mockito.when(callable.call()).thenThrow(new Exception());
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get(1000, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
