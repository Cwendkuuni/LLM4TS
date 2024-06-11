package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.MyCallable;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
    public void setup() {
        baseRuntimeFlowableDelegate = Mockito.spy(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        
        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
        taskInstance.setId(1L);
        
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        
        Mockito.verify(taskInstance, Mockito.times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
        
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        
        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.any(FlowStatus.class));
    }

    @Test
    public void testInitCallable() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        
        Callable<Object> callable = baseRuntimeFlowableDelegate.initCallable(execution);
        
        assertNotNull(callable);
        assertTrue(callable instanceof MyCallable);
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        
        baseRuntimeFlowableDelegate.run(execution);
        
        Mockito.verify(baseRuntimeFlowableDelegate, Mockito.times(1)).initTargetTaskInstanceId(execution);
        Mockito.verify(baseRuntimeFlowableDelegate, Mockito.times(1)).initCallable(execution);
        Mockito.verify(baseRuntimeFlowableDelegate, Mockito.times(1)).preHandle(execution);
        Mockito.verify(baseRuntimeFlowableDelegate, Mockito.times(1)).execute(execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.setDone(true);
        baseRuntimeFlowableDelegate.setReturnObject("Test");
        
        Object result = baseRuntimeFlowableDelegate.get();
        
        assertNotNull(result);
        assertEquals("Test", result);
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.setDone(false);
        baseRuntimeFlowableDelegate.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testGetException() throws InterruptedException {
        baseRuntimeFlowableDelegate.setDone(true);
        baseRuntimeFlowableDelegate.setThrown(new Exception("Test Exception"));
        
        try {
            baseRuntimeFlowableDelegate.get();
        } catch (ExecutionException e) {
            assertNotNull(e.getCause());
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }
}