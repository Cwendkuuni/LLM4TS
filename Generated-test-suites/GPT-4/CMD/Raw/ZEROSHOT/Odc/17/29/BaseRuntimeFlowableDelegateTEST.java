
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;


public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution mockExecution;

    @Mock
    private FlowTaskInstance mockTaskInstance;

    @Mock
    private ExecutionStrategyConfig mockStrategyConfig;

    private Callable<String> callable;

    private TestRuntimeFlowableDelegate testRuntimeFlowableDelegate;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        callable = () -> "Test Callable";
        testRuntimeFlowableDelegate = new TestRuntimeFlowableDelegate(callable);
    }

    @Test
    public void testProcessRun() throws Exception {
        testRuntimeFlowableDelegate.run(mockExecution);
        Assert.assertNotNull(testRuntimeFlowableDelegate.get());
    }

    @Test
    public void testExecute() {
        testRuntimeFlowableDelegate.execute(mockExecution);
        verify(mockExecution, times(1)).getCurrentActivityId();
    }

    @Test
    public void testIsDone() throws Exception {
        testRuntimeFlowableDelegate.run(mockExecution);
        Assert.assertTrue(testRuntimeFlowableDelegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGet_Error() throws Exception {
        testRuntimeFlowableDelegate.throwError("Error occurred!");
        testRuntimeFlowableDelegate.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        testRuntimeFlowableDelegate.run(mockExecution);
        Assert.assertEquals("Test Callable", testRuntimeFlowableDelegate.get(3, TimeUnit.SECONDS));
    }

    private class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        Callable<String> testCallable;

        TestRuntimeFlowableDelegate(Callable<String> testCallable) {
            this.testCallable = testCallable;
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return this.testCallable;
        }

        @Override
        public void bindToFlowTaskInstance(@NonNull FlowTaskInstance taskInstance) {
            this.strategyConfig = mockStrategyConfig;
        }

        void throwError(String message) {
            this.thrown = new Exception(message);
            this.done = true;
            this.latch.countDown();
        }
    }

}
