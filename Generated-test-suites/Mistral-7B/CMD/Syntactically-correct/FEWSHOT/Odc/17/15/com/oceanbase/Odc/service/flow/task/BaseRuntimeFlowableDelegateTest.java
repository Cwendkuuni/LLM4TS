package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 *
 * @author yh263208
 */
public class BaseRuntimeFlowableDelegateTest extends BaseFlowableDelegateTest {

    private static final String ACTIVITY_ID = "activityId";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TARGET_TASK_INSTANCE_ID = 1L;
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;

    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private BaseRuntimeFlowableDelegate<String> delegate;
    private AnnotationConfigApplicationContext applicationContext;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(FlowableAdaptor.class);
        applicationContext.refresh();
        FlowableAdaptor flowableAdaptor = applicationContext.getBean(FlowableAdaptor.class);
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };
        delegate.init(eventPublisher);
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.flowInstanceRepository = flowInstanceRepository;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = createMockExecution();
        when(execution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
        when(flowInstanceRepository.updateStatusById(FLOW_INSTANCE_ID, FlowStatus.RUNNING)).thenReturn(true);

        delegate.execute(execution);

        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
        verify(flowInstanceRepository, times(1)).updateStatusById(FLOW_INSTANCE_ID, FlowStatus.RUNNING);
        assertEquals("result", delegate.get());
    }

    @Test
    public void testRunWithException() throws Exception {
        DelegateExecution execution = createMockExecution();
        when(execution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
        when(flowInstanceRepository.updateStatusById(FLOW_INSTANCE_ID, FlowStatus.RUNNING)).thenThrow(new RuntimeException());

        try {
            delegate.execute(execution);
            fail();
        } catch (ExecutionException e) {
            assertEquals("java.lang.RuntimeException", e.getCause().getClass().getName());
        }

        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
        verify(flowInstanceRepository, times(1)).updateStatusById(FLOW_INSTANCE_ID, FlowStatus.RUNNING);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = delegate.initCallable(createMockExecution());
        assertEquals("result", callable.call());
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(TARGET_TASK_INSTANCE_ID);
        when(flowTaskInstance.getTaskType()).thenReturn(TASK_TYPE);

        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance, times(1)).bindServiceTask(isA(BaseRuntimeFlowableDelegate.class));
    }

    @Test
    public void testIsDone() {
        assertTrue(delegate.isDone());
        delegate.done = false;
        assertFalse(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.done = true;
        delegate.returnObject = "result";

        assertEquals("result", delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException {
        delegate.latch = new CountDownLatch(0);
        delegate.done = true;
        delegate.returnObject = "result";

        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.done = true;
        delegate.throwable = new RuntimeException();

        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.latch = new CountDownLatch(0);

        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetNull() throws InterruptedException, ExecutionException {
        delegate.done = true;
        delegate.returnObject = null;

        assertNull(delegate.get());
    }

    private DelegateExecution createMockExecution() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn(getProcessDefinitionId());
        when(execution.getProcessInstanceId()).thenReturn(getProcessInstanceId());
        when(execution.getBusinessKey()).thenReturn("businessKey");
        when(execution.getId()).thenReturn(getExecutionId());
        when(execution.getBusinessKey()).thenReturn("businessKey");
        when(execution.getProcessDefinitionKey()).thenReturn(getProcessDefinitionKey());
        when(execution.getProcessDefinitionName()).thenReturn("processDefinitionName");
        when(execution.getProcessInstanceName()).thenReturn("processInstanceName");
        when(execution.getProcessInstanceId()).thenReturn(getProcessInstanceId());
        when(execution.getExecutionLongVariable("foo")).thenReturn("bar");
        return execution;
    }

    private String getProcessDefinitionId() {
        return "processDefinitionId";
    }

    private String getProcessDefinitionKey() {
        return "processDefinitionKey";
    }

    private String getProcessInstanceId() {
        return "processInstanceId";
    }

    private String getExecutionId() {
        return "executionId";
    }

}