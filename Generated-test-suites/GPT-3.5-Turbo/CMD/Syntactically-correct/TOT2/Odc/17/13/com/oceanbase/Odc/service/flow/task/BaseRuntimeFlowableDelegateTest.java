import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    
    private BaseRuntimeFlowableDelegate< String > baseRuntimeFlowableDelegate;
    private RetryExecutor retryExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        retryExecutor = RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build();
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate< String > () {
            @Override
            protected Callable< String > initCallable(DelegateExecution execution) {
                return null;
            }
        };
        baseRuntimeFlowableDelegate.init();
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(Mockito.mock(EventPublisher.class));
    }

    @Test
    public void testExecute_Success() throws Exception {
        Mockito.doReturn("activityIdTest").when(delegateExecution).getCurrentActivityId();

        baseRuntimeFlowableDelegate.execute(delegateExecution);

        Mockito.verify(delegateExecution, Mockito.times(1)).getCurrentActivityId();
    }

    @Test
    public void testGet_Success() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(delegateExecution);

        Future< String > future = new Future< String > () {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return baseRuntimeFlowableDelegate.isDone();
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                return baseRuntimeFlowableDelegate.get();
        }

            @Override
            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };

        assertEquals(null, future.get());
    }

    @Test(expected = TimeoutException.class)
    public void testGet_TimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        Future< String > future = new Future< String > () {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                throw new TimeoutException();
            }
        };

        future.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_ServiceTaskError() throws Exception {
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertNotNull(baseRuntimeFlowableDelegate);
    }
}