package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
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

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Void> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Void>() {
            @Override
            protected Callable<Void> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(execution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.execute(execution);
        Void result = baseRuntimeFlowableDelegate.get();
        assertNotNull(result);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.execute(execution);
        Void result = baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
        assertNotNull(result);
    }

    @Test
    public void testRun() {
        try {
            baseRuntimeFlowableDelegate.run(execution);
        } catch (Exception e) {
            assertTrue(e instanceof ServiceTaskError);
        }
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(1L);
        baseRuntimeFlowableDelegate.setActivityId("activityId");
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        assertTrue(taskInstance.isBoundServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);
        baseRuntimeFlowableDelegate.setRetryExecutor(RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build());
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        assertEquals(FlowStatus.COMPLETED, baseRuntimeFlowableDelegate.getFlowStatus());
    }
}