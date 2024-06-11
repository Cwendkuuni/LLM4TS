package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

public class BaseRuntimeFlowableDelegateTest {

    @Autowired
    private FlowableAdaptor flowableAdaptor;

    @Autowired
    private EventPublisher eventPublisher;
    
    private BaseRuntimeFlowableDelegate delegate;

    private DelegateExecution execution;

    private FlowTaskInstance flowTaskInstance;

    private Callable callable;

    @Before
    public void setup() {
      execution = mock(DelegateExecution.class);
      flowTaskInstance = mock(FlowTaskInstance.class);
      callable = mock(Callable.class);
      delegate = spy(new BaseRuntimeFlowableDelegate() {
        @Override
        protected Callable initCallable(DelegateExecution execution) {
          return callable;
        }
      });
    }

    @Test
    public void testRun() throws Exception {
        
        doReturn("example").when(delegate).initCallable(any());
        doNothing().when(delegate).preHandle(any());
        
        delegate.run(execution);
        
        assertEquals(true, delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        
        doReturn(Optional.of(1L)).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(any());
        
        delegate.get();
        
        verify(flowableAdaptor, atLeastOnce()).getFlowInstanceIdByProcessDefinitionId(any());
    }

    @Test
    public void testGet_withTimeout() throws Exception {
        
        doReturn(Optional.of(1L)).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(any());
        
        delegate.get(1, TimeUnit.SECONDS);
        
        verify(flowableAdaptor, atLeastOnce()).getFlowInstanceIdByProcessDefinitionId(any());
    }

    @Test(expected = ExecutionException.class)
    public void testThrownException() throws Exception {
        
        doThrow(new RuntimeException()).when(delegate).run(any());
        
        delegate.run(execution);
        delegate.get();
    }
    
    @Test
    public void testBindToFlowTaskInstance() {
        doReturn(1L).when(flowTaskInstance).getId();
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        doReturn(1).when(flowableAdaptor).updateStatusById(anyLong(), any());
        delegate.updateFlowInstanceStatus(FlowStatus.CREATING);
        verify(flowableAdaptor, times(1)).updateStatusById(anyLong(), any());
    }
    
}