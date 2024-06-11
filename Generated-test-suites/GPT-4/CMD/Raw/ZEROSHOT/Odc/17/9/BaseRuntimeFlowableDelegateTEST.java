
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import static org.mockito.Mockito.*;
import java.util.concurrent.Callable;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution mockExecution;
    
    @Mock
    private FlowableAdaptor mockAdaptor;
    
    @Mock
    private EventPublisher mockEventPublisher;

    @Mock
    private FlowInstanceRepository mockFlowInstanceRepository;

    @Mock
    private Callable<?> mockCallable;

    @Test(expected= ServiceTaskError.class)
    public void testRunWithExceptionInInitialization() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = createBaseRuntimeFlowableDelegate();
        when(delegate.initCallable(mockExecution)).thenThrow(new RuntimeException());
        delegate.run(mockExecution);
    }

    @Test(expected= Exception.class)
    public void testRunWithExecutionException() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = createBaseRuntimeFlowableDelegate();
        when(mockCallable.call()).thenThrow(new Exception());
        delegate.run(mockExecution);
    }

    @Test
    public void testIsDone() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = createBaseRuntimeFlowableDelegate();
        assertFalse(delegate.isDone());
        delegate.run(mockExecution);
        assertTrue(delegate.isDone());
    }

    @Test(expected= ExecutionException.class)
    public void testGetWithInterruptedException() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = createBaseRuntimeFlowableDelegate();
        Throwable exception = new Exception();
        when(mockCallable.call()).thenThrow(exception);
        delegate.run(mockExecution);
        delegate.get();
    }

    @Test(expected= TimeoutException.class)
    public void testGetWithTimeoutException() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = createBaseRuntimeFlowableDelegate();
        delegate.get(1, TimeUnit.SECONDS);
    }

    // Additional tests can be here for other methods...

    private BaseRuntimeFlowableDelegate<String> createBaseRuntimeFlowableDelegate(){
        return new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return mockCallable;
            }
        };
    }
}
