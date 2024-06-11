package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
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

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<TestObject> delegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowTaskInstance taskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate = new BaseRuntimeFlowableDelegate<TestObject>() {
            @Override
            protected Callable<TestObject> initCallable(DelegateExecution execution) {
                return () -> new TestObject();
            }
        };
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.init(eventPublisher);
    }

    @Test
    public void testInit() {
        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getLatch());
        assertNotNull(delegate.getTaskInstanceCreatedlistener());
        assertNotNull(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(123L));
        
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 123L)).thenReturn(Optional.of(taskInstance));
        when(taskInstance.getId()).thenReturn(456L);
        when(taskInstance.getTaskType()).thenReturn(TaskType.TYPE_1);
        
        delegate.initTargetTaskInstanceId(execution);

        assertEquals("activityId", delegate.getActivityId());
        assertEquals(Long.valueOf(456L), delegate.getTargetTaskInstanceId());
        assertEquals(TaskType.TYPE_1, delegate.getTaskType());
        assertEquals(Long.valueOf(123L), delegate.getFlowInstanceId());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        delegate.setTargetTaskInstanceId(456L);
        
        when(taskInstance.getId()).thenReturn(456L);
        
        delegate.bindToFlowTaskInstance(taskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.setFlowInstanceId(123L);
        when(flowInstanceRepository.updateStatusById(123L, FlowStatus.COMPLETED)).thenReturn(true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(execution);
        Thread.sleep(100); // Wait for latch countdown
        assertNotNull(delegate.isDone());
        assertNotNull(delegate.get());
    }

    @Test
    public void testExecute() {
        delegate.execute(execution);
        assertNotNull(delegate.getEventPublisher());
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.get();
        Thread.sleep(100); // Wait for latch countdown
        assertNotNull(delegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.get(1, TimeUnit.SECONDS);
        Thread.sleep(100); // Wait for latch countdown
        assertNotNull(delegate.get(1, TimeUnit.SECONDS));
    }

    private class TestObject {}

}