import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.ServiceTaskError;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> runtimeFlowableDelegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        runtimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
        runtimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        runtimeFlowableDelegate.setEventPublisher(eventPublisher);
        runtimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    // Test for successful task execution
    @Test
    public void testRun_Success() throws Exception {
        runtimeFlowableDelegate.execute(execution);

        assertTrue(runtimeFlowableDelegate.isDone());
        assertEquals("Test", runtimeFlowableDelegate.get());
    }

    // Test for service task error
    @Test(expected = ServiceTaskError.class)
    public void testRun_Exception() throws Exception {
        runtimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                throw new RuntimeException("Error");
            }
        };
        runtimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        runtimeFlowableDelegate.setEventPublisher(eventPublisher);
        runtimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
        runtimeFlowableDelegate.execute(execution);
    }

    // Test for timeout when calling get() method
    @Test(expected = TimeoutException.class)
    public void testGet_Timeout() throws InterruptedException, ExecutionException, TimeoutException {
        runtimeFlowableDelegate.execute(execution);
        runtimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
    }

    // Test for updating flow instance status
    @Test
    public void testUpdateFlowInstanceStatus() {
        runtimeFlowableDelegate.setFlowInstanceId(1L);
        when(flowInstanceRepository.updateStatusById(eq(1L), any(FlowStatus.class))).thenReturn(true);
        
        runtimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        
        verify(flowInstanceRepository, times(1)).updateStatusById(eq(1L), eq(FlowStatus.COMPLETED));
    }

    // Additional test cases for other methods and scenarios can be added here

}