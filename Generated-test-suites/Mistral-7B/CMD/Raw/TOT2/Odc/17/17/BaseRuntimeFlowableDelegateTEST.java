package com.oceanbase.odc.service.flow.test.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.MockProcessEngineRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Rule
    public MockProcessEngineRule processEngineRule = new MockProcessEngineRule();

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                throw new UnsupportedOperationException();
            }
        };
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.eventPublisher = mock(EventPublisher.class);

        this.delegate.setFlowableAdaptor(this.flowableAdaptor);
        this.delegate.setFlowInstanceRepository(this.flowInstanceRepository);
        this.delegate.setEventPublisher(this.eventPublisher);

        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));
        when(this.flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));
    }

    @After
    public void tearDown() {
        this.delegate = null;
        this.flowableAdaptor = null;
        this.flowInstanceRepository = null;
        this.eventPublisher = null;
    }

    @Test
    @Deployment
    public void testRun() throws Exception {
        ProcessInstance processInstance = processEngineRule.deployAndReturnProcessInstance("testProcess", null);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessInstanceId()).thenReturn(processInstance.getId());

        CountDownLatch latch = new CountDownLatch(1);
        this.delegate.setLatch(latch);

        this.delegate.run(execution);

        latch.await();

        verify(this.flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(this.flowableAdaptor).getTaskInstanceByActivityId(anyString(), anyLong());
        verifyZeroInteractions(this.eventPublisher);
        verifyZeroInteractions(this.flowInstanceRepository);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        ProcessInstance processInstance = processEngineRule.deployAndReturnProcessInstance("testProcess", null);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessInstanceId()).thenReturn(processInstance.getId());

        CountDownLatch latch = new CountDownLatch(1);
        this.delegate.setLatch(latch);

        this.delegate.run(execution);

        latch.await();

        when(this.eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);

        assertEquals(null, this.delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException, TimeoutException {
        ProcessInstance processInstance = processEngineRule.deployAndReturnProcessInstance("testProcess", null);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessInstanceId()).thenReturn(processInstance.getId());

        CountDownLatch latch = new CountDownLatch(1);
        this.delegate.setLatch(latch);

        this.delegate.run(execution);

        latch.await();

        throw new InterruptedException();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException, TimeoutException {
        ProcessInstance processInstance = processEngineRule.deployAndReturnProcessInstance("testProcess", null);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessInstanceId()).thenReturn(processInstance.getId());

        CountDownLatch latch = new CountDownLatch(1);
        this.delegate.setLatch(latch);

        when(this.eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenThrow(new RuntimeException());

        this.delegate.run(execution);

        latch.await();

        try {
            this.delegate.get();
        } catch (InterruptedException e) {
            throw new ExecutionException(e);
        }
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        ProcessInstance processInstance = processEngineRule.deployAndReturnProcessInstance("testProcess", null);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessInstanceId()).thenReturn(processInstance.getId());

        CountDownLatch latch = new CountDownLatch(1);
        this.delegate.setLatch(latch);

        when(this.eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);

        this.delegate.run(execution);

        latch.await(1, TimeUnit.SECONDS);

        throw new TimeoutException();
    }

    @Test
    public void testPreHandle() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);

        this.delegate.preHandle(execution);

        verifyZeroInteractions(this.flowableAdaptor);
        verifyZeroInteractions(this.flowInstanceRepository);
        verifyZeroInteractions(this.eventPublisher);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        this.delegate.bindToFlowTaskInstance(flowTaskInstance);

        assertTrue(flowTaskInstance.isBound());
        assertNotNull(flowTaskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        this.delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(this.flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }
}