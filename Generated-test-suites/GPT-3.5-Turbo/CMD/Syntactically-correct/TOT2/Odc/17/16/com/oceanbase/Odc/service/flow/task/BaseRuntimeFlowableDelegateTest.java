import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.CountDownLatch;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution execution;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @PostConstruct
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitCallable() {
        Callable<Object> callable = baseRuntimeFlowableDelegate.initCallable(execution);
        assertNotNull(callable);
    }

    @Test
    public void testRun() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(execution);
        Object result = baseRuntimeFlowableDelegate.get();
        assertNotNull(result);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(execution);
        Object result = baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
        assertNotNull(result);
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(execution);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
    }

    @Test
    public void testPreHandle() {
        baseRuntimeFlowableDelegate.preHandle(execution);
    }
}