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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

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
    private BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> {
                return "Test";
            };
        }
    };

    @Before
    public void setup() {
        when(execution.getCurrentActivityId()).thenReturn("ActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);
        flowTaskInstance.setTaskType(TaskType.TYPE1);
        flowTaskInstance.setStrategyConfig(new ExecutionStrategyConfig());
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(flowTaskInstance));
    }

    @Test
    public void testRun_SuccessfulExecution() throws Exception {
        delegate.run(execution);
        assertTrue(delegate.isDone());
        assertEquals("Test", delegate.get());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_ExceptionThrownDuringExecution() throws Exception {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new Exception("Simulated error");
                };
            }
        };
        delegate.run(execution);
    }

    @Test
    public void testGet_TimeoutException() throws InterruptedException, ExecutionException {
        delegate.run(execution);
        assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        taskInstance.setTaskType(TaskType.TYPE1);
        taskInstance.setStrategyConfig(new ExecutionStrategyConfig());
        delegate.setTargetTaskInstanceId(1L);
        delegate.bindToFlowTaskInstance(taskInstance);
        assertNotNull(taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.setFlowInstanceId(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testPreHandle() {
        // Test implementation of preHandle method if any
    }

    @Test
    public void testInitCallable() {
        // Test implementation of initCallable method if any
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        // Test implementation of initTargetTaskInstanceId method if any
    }
}