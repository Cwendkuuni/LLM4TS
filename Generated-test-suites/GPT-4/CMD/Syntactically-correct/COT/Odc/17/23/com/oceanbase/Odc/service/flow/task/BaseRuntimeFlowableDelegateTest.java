package com.oceanbase.odc.service.flow.task;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    EventPublisher mockEventPublisher;
    @InjectMocks
    BaseRuntimeFlowableDelegate objectUnderTest;

    @Test
    public void testInit(){
        assertNotNull(objectUnderTest.getActivityId());
        assertNotNull(objectUnderTest.getTargetTaskInstanceId());
        assertNotNull(objectUnderTest.getTaskType());
        assertNotNull(objectUnderTest.getFlowInstanceId());
        assertNotNull(objectUnderTest.getStrategyConfig());
        assertTrue(objectUnderTest.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWhenNotDone() throws InterruptedException, ExecutionException{
        objectUnderTest.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeoutWhenNotDone() throws InterruptedException, ExecutionException, TimeoutException{
        objectUnderTest.get(1L, TimeUnit.MILLISECONDS);
    }
  
    @Test
    public void bindToFlowTaskInstance_ValidTaskInstance_NothingHappens(){   
        FlowTaskInstance mockTaskInstance = mock(FlowTaskInstance.class);   
        when(mockTaskInstance.getId()).thenReturn(objectUnderTest.getTargetTaskInstanceId());   
        objectUnderTest.bindToFlowTaskInstance(mockTaskInstance);  
    }
  
    @Test(expected = NullPointerException.class)
    public void bindToFlowTaskInstance_NullTaskInstance_NullPointerException(){
        objectUnderTest.bindToFlowTaskInstance(null);
    }
  
    @Test
    public void updateFlowInstanceStatus_ValidFlowStatus_NothingHappens(){   
        FlowStatus mockFlowStatus = mock(FlowStatus.class);   
        objectUnderTest.updateFlowInstanceStatus(mockFlowStatus);  
    }
  
    @Test(expected = NullPointerException.class)
    public void updateFlowInstanceStatus_NullFlowStatus_NullPointerException(){
        objectUnderTest.updateFlowInstanceStatus(null);
    }

}