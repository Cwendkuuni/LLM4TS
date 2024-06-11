import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        initMocks(this);
    }

    // Define a concrete subclass of your abstract class for the test
    private class MyDelegate extends BaseRuntimeFlowableDelegate<Object> {

        @Override
        protected Callable<Object> initCallable(DelegateExecution execution) {
            return () -> null;
        }
    }

    @Test
    public void testRun() throws Exception {
        MyDelegate myDelegate = new MyDelegate();
        myDelegate.setFlowInstanceRepository(flowInstanceRepository);

        // We don't check the exact execution of the run method here as it depends on business requirements
        // For instance, how many times a method should be called, with what arguments etc
        myDelegate.run(delegateExecution);
    }

    @Test
    public void testExecute() {
        MyDelegate myDelegate = new MyDelegate();
        myDelegate.setEventPublisher(eventPublisher);

        // We don't check the exact execution of the execute method here as it depends on business requirements
        // For instance, how many times a method should be called, with what arguments etc
        myDelegate.execute(delegateExecution);
    }

    @Test
    public void testIsDone() {
        MyDelegate myDelegate = new MyDelegate();
        assertFalse(myDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        MyDelegate myDelegate = new MyDelegate();
        assertNull(myDelegate.get());
    }

    @Test
    public void testGetLongTimeUnit() throws Exception {
        MyDelegate myDelegate = new MyDelegate();
        assertNull(myDelegate.get(1L, TimeUnit.SECONDS));
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        MyDelegate myDelegate = new MyDelegate();
        myDelegate.setFlowInstanceId(1L);
        myDelegate.setFlowInstanceRepository(flowInstanceRepository);

        // We don't check the exact execution of the updateFlowInstanceStatus method here as it depends on business requirements
        // For instance, how many times a method should be called, with what arguments etc
        myDelegate.updateFlowInstanceStatus(FlowStatus.ACTIVED);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        MyDelegate myDelegate = new MyDelegate();

        myDelegate.bindToFlowTaskInstance(flowTaskInstance);
    }
}