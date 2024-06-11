import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<TestObject> delegate;

    @Before
    public void setUp() {
        delegate = spy(new BaseRuntimeFlowableDelegate<TestObject>() {
            @Override
            protected Callable<TestObject> initCallable(DelegateExecution execution) {
                return () -> new TestObject();
            }
        });
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.run(execution);

        verify(delegate, times(1)).initTargetTaskInstanceId(execution);
        verify(delegate, times(1)).initCallable(execution);
    }
}

class TestObject {
    // Test object
}