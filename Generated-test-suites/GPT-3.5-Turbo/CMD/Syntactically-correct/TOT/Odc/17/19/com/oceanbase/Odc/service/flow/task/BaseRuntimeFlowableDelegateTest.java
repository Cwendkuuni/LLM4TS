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
import org.springframework.context.ApplicationEventPublisher;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> testDelegate;

    @Before
    public void setUp() {
        testDelegate = spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test result";
            }
        });
        testDelegate.setFlowInstanceRepository(flowInstanceRepository);
        testDelegate.setFlowableAdaptor(flowableAdaptor);
    }

    @Test
    public void testRun_Success() throws Exception {
        testDelegate.run(execution);
        assertTrue(testDelegate.isDone());
    }

    @Test
    public void testGet_Success() throws Exception {
        testDelegate.run(execution);
        assertEquals("Test result", testDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGet_WithException() throws Exception {
        testDelegate.run(execution);
        testDelegate.setThrown(new Exception("Test Exception"));
        testDelegate.get();
    }

    @Test
    public void testGet_WithTimeout_Success() throws Exception {
        testDelegate.run(execution);
        assertEquals("Test result", testDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGet_WithTimeout_Failure() throws Exception {
        testDelegate.run(execution);
        testDelegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance_Success() {
        FlowTaskInstance taskInstance = new FlowTaskInstance(1L, TaskType.TYPE_A, new ExecutionStrategyConfig());
        taskInstance.setId(1L);
        testDelegate.setTargetTaskInstanceId(1L);
        testDelegate.setActivityId("activityId");
        testDelegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(testDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus_Success() {
        testDelegate.setFlowInstanceId(1L);
        testDelegate.setFlowInstanceRepository(flowInstanceRepository);
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);
        testDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

}