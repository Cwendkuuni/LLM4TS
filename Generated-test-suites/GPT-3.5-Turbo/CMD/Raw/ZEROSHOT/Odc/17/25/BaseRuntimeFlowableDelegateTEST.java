import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
    }

    @Test
    public void testExecute_Success() {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.execute(execution);
    }

    @Test(expected = ServiceTaskError.class)
    public void testExecute_Exception() {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                throw new RuntimeException("Error");
            }
        };
        baseRuntimeFlowableDelegate.execute(execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.execute(mock(DelegateExecution.class));
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGet_Interrupted() throws InterruptedException, ExecutionException {
        Thread.currentThread().interrupt();
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_Exception() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new RuntimeException("Error");
                };
            }
        };
        baseRuntimeFlowableDelegate.execute(mock(DelegateExecution.class));
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGet_Timeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.get(1L, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        when(taskInstance.getId()).thenReturn(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(eq(baseRuntimeFlowableDelegate));
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(null);
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any());
    }
}