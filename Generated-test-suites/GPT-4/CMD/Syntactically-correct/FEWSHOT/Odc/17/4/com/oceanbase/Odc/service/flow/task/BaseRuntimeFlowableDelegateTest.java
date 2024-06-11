
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {
    
    @InjectMocks
    BaseRuntimeFlowableDelegate<Integer> baseRuntimeFlowableDelegate;
    
    @Mock
    EventPublisher eventPublisher;
    
    @Mock
    FlowableAdaptor flowableAdaptor;
    
    @Mock
    FlowInstanceRepository flowInstanceRepository;
    
    @Mock
    DelegateExecution delegateExecution;

    @Test
    public void testIsDone() throws Exception {
        assertFalse(this.baseRuntimeFlowableDelegate.isDone());
        this.startTask();
        assertTrue(this.baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        this.startTask();
        assertEquals(Integer.valueOf(1), this.baseRuntimeFlowableDelegate.get());
    }
    
    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        this.startTaskWithException();
        this.baseRuntimeFlowableDelegate.get(); 
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        this.startTask();
        assertEquals(Integer.valueOf(1), this.baseRuntimeFlowableDelegate.get(1L, TimeUnit.MILLISECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeoutAndException() throws Exception {
        this.startTaskWithException();
        this.baseRuntimeFlowableDelegate.get(1L, TimeUnit.MILLISECONDS); 
    }

    private void startTask() throws Exception {
        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(Optional.of(1L)));
        when(this.flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(createFlowTaskInstance()));
        this.baseRuntimeFlowableDelegate.execute(delegateExecution);
    }

    private void startTaskWithException() throws Exception {
        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(Optional.of(1L)));
        when(this.flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(createFlowTaskInstanceThrowException()));
        this.baseRuntimeFlowableDelegate.execute(delegateExecution);
    }

    private FlowTaskInstance createFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);
        flowTaskInstance.setStrategyConfig(new StrategyConfig());
        flowTaskInstance.setTaskCallable(()->1);
        return flowTaskInstance;
    }

    private FlowTaskInstance createFlowTaskInstanceThrowException() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);
        flowTaskInstance.setStrategyConfig(new StrategyConfig());
        flowTaskInstance.setTaskCallable(() -> {
            throw new Exception("Test Exception");
        });
        return flowTaskInstance;
    }

}
