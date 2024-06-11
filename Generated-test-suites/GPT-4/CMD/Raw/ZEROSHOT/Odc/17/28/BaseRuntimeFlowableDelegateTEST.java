
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private Callable<Object> mockCallable;

    @Mock
    private DelegateExecution mockExecution;

    private TestableBaseRuntimeFlowableDelegate delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        delegate = new TestableBaseRuntimeFlowableDelegate();
        when(mockExecution.getCurrentActivityId()).thenReturn("0001");
    }

    @Test
    public void testExecute_isDone_AfterExecution() throws Exception {
        when(mockCallable.call()).thenReturn(new Object());
        delegate.run(mockExecution);

        assertTrue(delegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGet_throwsException_WhenCallableHasException() throws Exception {
        when(mockCallable.call()).thenThrow(Exception.class);
        delegate.run(mockExecution);

        delegate.get();
    }

    @Test
    public void testGet_returnsCallableObject_WhenNoException() throws Exception {
        Object expected = new Object();
        when(mockCallable.call()).thenReturn(expected);
        delegate.run(mockExecution);

        Object actual = delegate.get();

        assertEquals(expected, actual);
    }

    private class TestableBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Object> {
        @Override
        protected Callable<Object> initCallable(DelegateExecution execution) {
            return mockCallable;
        }

        @Override
        protected void updateFlowInstanceStatus(@NonNull FlowStatus flowStatus) {
            // No implementation for test class.
        }
        
        @Override
        protected void preHandle(DelegateExecution execution) {
            // No implementation for test class.
        }
    }
}
