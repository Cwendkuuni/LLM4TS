package com.oceanbase.odc.service.flow.task;
import org.junit.Test;
import org.mockito.Mockito;
import org.flowable.engine.delegate.DelegateExecution;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.*;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testRun() throws Exception {
        // Initialize Mockito mocks for the various objects
        DelegateExecution mockExecution = Mockito.mock(DelegateExecution.class);
        EventPublisher mockEventPublisher = Mockito.mock(EventPublisher.class);
        Callable<T> mockCallable = Mockito.mock(Callable.class);
        BaseRuntimeFlowableDelegate<T> delegate = Mockito.spy(BaseRuntimeFlowableDelegate.class);

        // instruct Mockito to return the mockCallable when initCallable is called.
        Mockito.when(delegate.initCallable(mockExecution)).thenReturn(mockCallable);

        // Instruct Mockito to do nothing when publishEvent is called.
        Mockito.doNothing().when(mockEventPublisher).publishEvent(Mockito.any());

        // instruct Mockito to run the delegate's execute method.
        delegate.run(mockExecution);

        // verify that the Callable object was called
        Mockito.verify(mockCallable, Mockito.times(1)).call();
    }

    @Test(expected = ExecutionException.class)
    public void testExecuteThrowsException() {
        // Initialize Mockito mocks for the various objects
        DelegateExecution mockExecution = Mockito.mock(DelegateExecution.class);
        EventPublisher mockEventPublisher = Mockito.mock(EventPublisher.class);
        BaseRuntimeFlowableDelegate<T> delegate = Mockito.spy(BaseRuntimeFlowableDelegate.class);

        // Instruct Mockito to do nothing when removeEventListener is called.
        Mockito.doNothing().when(mockEventPublisher).removeEventListener(Mockito.any());

        // instruct Mockito to throw an exception when super.execute is called.
        Mockito.doThrow(new ExecutionException("Mock exception", new Throwable())).when(delegate).execute(mockExecution);
    }

}