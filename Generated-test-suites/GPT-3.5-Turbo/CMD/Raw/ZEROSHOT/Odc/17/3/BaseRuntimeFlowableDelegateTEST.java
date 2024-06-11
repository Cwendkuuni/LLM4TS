import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "Test";
                };
            }
        };
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testRun() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L))
                .thenReturn(java.util.Optional.of(new FlowTaskInstance(1L, "activityId", null, new ExecutionStrategyConfig())));
        
        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(baseRuntimeFlowableDelegate, Thread.currentThread()));
    }

    @Test
    public void testPreHandle() throws Exception {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);
      
        // Add your assertion here
    }

    @Test
    public void testIsDone() {
        boolean done = baseRuntimeFlowableDelegate.isDone();
      
        // Add your assertion here
    }

    @Test
    public void testGet() throws Exception {
        // Implement when needed
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        // Implement when needed
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance(1L, "activityId", null, new ExecutionStrategyConfig());
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
      
        // Add your assertion here
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(null);

        // Add your assertion here
    }

}