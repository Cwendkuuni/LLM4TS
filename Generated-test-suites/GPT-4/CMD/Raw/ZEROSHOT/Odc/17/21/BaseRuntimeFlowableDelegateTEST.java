
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

    @Mock
    DelegateExecution delegateExecution;

    @Mock
    EventPublisher eventPublisher;;

    @Test
    public void testRun() {
        try {
            when(delegateExecution.getCurrentActivityId()).thenReturn("TestActivityId");
            baseRuntimeFlowableDelegate.run(delegateExecution);
            assertTrue(baseRuntimeFlowableDelegate.isDone());
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testExecute() {
        try {
            baseRuntimeFlowableDelegate.execute(delegateExecution);
            verify(eventPublisher,times(1)).removeEventListener(any());
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testIsDone() {
        try {
            baseRuntimeFlowableDelegate.isDone();
            assertFalse(baseRuntimeFlowableDelegate.isDone());
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testObjectGet() throws InterruptedException, ExecutionException {
        when(delegateExecution.getCurrentActivityId()).thenReturn("TestActivityId");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            try {
                baseRuntimeFlowableDelegate.run(delegateExecution);
            } catch (Exception e) {
                fail("Should not have thrown any exception");
            }
        });
        while (!future.isDone()) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
        assertNotNull(baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException {
        when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException());
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException {
        when(delegateExecution.getCurrentActivityId()).thenReturn("TestActivityId");
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertNotNull(baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException {
        when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException());
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance(){
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(1L);
        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance,times(1)).bindServiceTask(any());
    }
    
    // Test additional methods in a similar manner
}
