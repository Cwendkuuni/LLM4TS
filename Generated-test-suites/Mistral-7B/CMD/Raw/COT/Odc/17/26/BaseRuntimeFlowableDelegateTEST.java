package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private DelegateExecution execution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        eventPublisher = Mockito.mock(EventPublisher.class);
        flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        execution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testConstructor() {
        BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        assertNotNull(baseRuntimeFlowableDelegate.getLatch());
        assertNotNull(baseRuntimeFlowableDelegate.getRetryExecutor());
        assertNotNull(baseRuntimeFlowableDelegate.getTaskInstanceCreatedlistener());
        assertNotNull(baseRuntimeFlowableDelegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testInit() {
        delegate.init(eventPublisher);
        verify(eventPublisher, times(1)).addEventListener(delegate.getActiveTaskStatisticsListener());
        verify(eventPublisher, times(1)).addEventListener(delegate.getTaskInstanceCreatedlistener());
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Mockito.mock(FlowTaskInstance.class));

        delegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenThrow(new RuntimeException());
        delegate.run(execution);

        fail("Should throw ServiceTaskError");
    }

    @Test
    public void testPreHandle() throws Exception {
        delegate.preHandle(execution);
    }

    @Test
    public void testExecute() throws Exception {
        delegate.execute(execution);

        verify(eventPublisher, times(1)).removeEventListener(delegate.getTaskInstanceCreatedlistener());
        verify(eventPublisher, times(1)).removeEventListener(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testIsDone() throws Exception {
        delegate.setDone(true);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.setLatch(latch);
        delegate.setDone(true);

        Object result = delegate.get();
        assertNull(result);
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.setLatch(latch);

        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.setLatch(latch);
        delegate.setThrown(new RuntimeException());

        delegate.get();
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.setLatch(latch);

        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.setLatch(latch);

        delegate.get(2, TimeUnit.SECONDS);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.initTargetTaskInstanceId(execution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId("activityId", 1L);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test(expected = InvocationTargetException.class)
    public void testInitCallableWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenThrow(new RuntimeException());
        delegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        fail("Should throw InvocationTargetException");
    }

}