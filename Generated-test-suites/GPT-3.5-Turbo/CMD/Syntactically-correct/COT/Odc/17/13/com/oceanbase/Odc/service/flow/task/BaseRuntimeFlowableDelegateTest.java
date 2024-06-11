
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);
        String result;
        try {
            result = callable.call();
        } catch (Exception e) {
            result = "Error";
        }

        assertEquals("Test", result);
    }

    @Test
    public void testGet() throws InterruptedException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(1000);
                    return "Delayed Test";
                };
            }
        };

        new Thread(() -> baseRuntimeFlowableDelegate.execute(delegateExecution)).start();

        String result = baseRuntimeFlowableDelegate.get(1500, TimeUnit.MILLISECONDS);
        assertEquals("Delayed Test", result);
    }
}
