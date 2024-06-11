import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegateTest;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<TestObject> delegate;
    private DelegateExecution execution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        delegate = new TestRuntimeFlowableDelegate();
        execution = mock(DelegateExecution.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);

        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testInitCallable() {
        Callable<TestObject> callable = delegate.initCallable(execution);
        assertNotNull(callable);
    }

    @Test
    public void testRun() throws Exception {
        TestRuntimeFlowableDelegate testDelegate = new TestRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        testDelegate.run(execution);
        assertEquals(true, testDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        // Assuming the task runs successfully and returns a TestObject
        Callable<TestObject> callable = delegate.initCallable(execution);
        TestObject testObject = callable.call();
        delegate.get();

        // Assert that the returned object is not null
        assertNotNull(testObject);
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        // Simulate a task that takes longer than the timeout specified
        Callable<TestObject> callable = delegate.initCallable(execution);
        TestObject testObject = callable.call();
        delegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Assuming the flow instance status is updated successfully
        Mockito.when(flowInstanceRepository.updateStatusById(Mockito.anyLong(), Mockito.any(FlowStatus.class)))
                .thenReturn(true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }
}

class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<TestObject> {

    @Autowired
    private ExecutionStrategyConfig executionStrategyConfig;

    @Override
    protected Callable<TestObject> initCallable(DelegateExecution execution) {
        return () -> {
            // Task logic implementation for testing
            return new TestObject();
        };
    }

    public void setExecutionStrategyConfig(ExecutionStrategyConfig executionStrategyConfig) {
        this.executionStrategyConfig = executionStrategyConfig;
    }
}

class TestObject {
    // Test class for the return object of the Callable
}