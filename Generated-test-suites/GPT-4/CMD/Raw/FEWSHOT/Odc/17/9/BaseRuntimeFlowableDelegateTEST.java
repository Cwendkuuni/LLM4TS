
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {
  
    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        delegate = mock(BaseRuntimeFlowableDelegate.class);
        execution = mock(DelegateExecution.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);

        when(delegate.getFlowableAdaptor()).thenReturn(flowableAdaptor);
        when(delegate.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);
    }
    
    @Test
    public void testRun() {
        delegate.run(execution);
        assertNotNull(delegate.getActivityId());
        assertTrue(delegate.isDone());
    }

    @Test
    public void testPreHandle() {
        delegate.preHandle(execution);
        assertNotNull(delegate.getStrategyConfig());
    }

    @Test
    public void testExecution() {
        delegate.execute(execution);
        assertTrue(delegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        delegate.bindToFlowTaskInstance(taskInstance);
        assertNotNull(delegate.getFlowTaskInstance());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus status = FlowStatus.SUCCESS;
        delegate.updateFlowInstanceStatus(status);
        Optional<FlowStatus> retrievedStatus = flowInstanceRepository.findById(delegate.getFlowInstanceId()).map(FlowInstance::getStatus);
        assertTrue(retrievedStatus.isPresent());
        assertEquals(status, retrievedStatus.get());
    }
}
