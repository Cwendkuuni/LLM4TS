
package com.oceanbase.odc.service.flow.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseRuntimeFlowableDelegateTest {
    
    @InjectMocks
    private BaseRuntimeFlowableDelegate baseDelegate;
    
    @Mock
    private DelegateExecution delegateExecution;
    
    @Test
    public void testPreHandle() {
        doNothing().when(delegateExecution).setVariable(anyString(), any());
        baseDelegate.preHandle(delegateExecution);
        verify(delegateExecution, times(1)).setVariable(anyString(), any());
    }
    
    @Test
    public void testIsDone() {
        boolean done = baseDelegate.isDone();
        assertFalse(done);
    }
    
    @Test(expected = InterruptedException.class)
    public void testGet_withInterruptedException() throws InterruptedException, ExecutionException {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testId");
        baseDelegate.get();
    }

   //Include more test cases for all the methods in the class.
}
