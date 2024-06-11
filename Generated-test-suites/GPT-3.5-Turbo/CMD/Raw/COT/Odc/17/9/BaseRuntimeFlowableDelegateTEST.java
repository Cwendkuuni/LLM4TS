
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<TestObject> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<TestObject>() {
        @Override
        protected Callable<TestObject> initCallable(DelegateExecution execution) {
            return () -> new TestObject();
        }
    };

    @Test
    public void testInitTargetTaskInstanceId() {
        // Test initialization of target task instance ID
        // Mocking necessary behavior
        when(execution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(Optional.of(1L)));
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(flowTaskInstance));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);

        // Verifying the flow task instance ID
        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId().longValue());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        // Test binding to flow task instance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);

        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(1L);
        baseRuntimeFlowableDelegate.setActivityId("testActivityId");

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        // Verifying that the task instance is bound correctly
        assertEquals(baseRuntimeFlowableDelegate, flowTaskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Test updating flow instance status
        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Verifying that the flow instance status is updated successfully
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        // Test getting the result
        baseRuntimeFlowableDelegate.setReturnObject(new TestObject());
        Future<TestObject> future = baseRuntimeFlowableDelegate;

        TestObject result = future.get();

        // Verifying the returned object
        assertEquals(baseRuntimeFlowableDelegate.getReturnObject(), result);
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        // Test getting the result with a timeout exception
        baseRuntimeFlowableDelegate.get(100, TimeUnit.MILLISECONDS);
    }

    private static class TestObject {}
}
