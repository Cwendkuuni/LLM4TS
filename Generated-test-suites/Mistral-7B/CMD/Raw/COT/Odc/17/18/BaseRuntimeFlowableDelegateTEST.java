import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> testObject;

    @Before
    public void setUp() {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
    }

    @Test
    public void testInit() {
        testObject = new BaseRuntimeFlowableDelegate<>();
        assertNotNull(testObject.getRetryExecutor());
        assertNotNull(testObject.getLatch());
        assertNull(testObject.getReturnObject());
        assertNull(testObject.getThrown());
        assertFalse(testObject.isDone());
    }

    @Test
    public void testRun_Success() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn("returnValue");

        testObject = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return callable;
            }
        };

        testObject.run(execution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, times(1)).call();
        assertEquals("returnValue", testObject.getReturnObject());
        assertTrue(testObject.isDone());
        assertSame(callable, testObject.getCallable());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_Failure() {
        DelegateExecution execution = mock(DelegateExecution.class);

        testObject = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                throw new RuntimeException("Exception in initCallable");
            }
        };

        testObject.run(execution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(testObject, times(1)).initTargetTaskInstanceId(execution);
        verifyNoMoreInteractions(callable);
        assertNull(testObject.getReturnObject());
        assertNotNull(testObject.getThrown());
        assertTrue(testObject.isDone());
    }

    @Test
    public void testPreHandle() {
        DelegateExecution execution = mock(DelegateExecution.class);
        testObject.preHandle(execution);
        verifyNoInteractions(testObject.getCallable());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        testObject.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(testObject);
        verify(testObject, times(1)).updateFlowInstanceStatus(FlowStatus.RUNNING);
    }

    @Test
    public void testUpdateFlowInstanceStatus_Success() {
        testObject.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(testObject.getFlowInstanceId(), FlowStatus.COMPLETED);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateFlowInstanceStatus_Failure() {
        testObject.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(testObject.getFlowInstanceId(), FlowStatus.COMPLETED);
        verify(testObject, times(1)).retryExecutor;
    }

}