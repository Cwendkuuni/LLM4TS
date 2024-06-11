
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class BaseRuntimeFlowableDelegateTest {
    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;
    private DelegateExecution mockExecution;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        mockExecution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testIsDone() {
        Mockito.when(baseRuntimeFlowableDelegate.isDone()).thenReturn(false);
        assertFalse(baseRuntimeFlowableDelegate.isDone());

        Mockito.when(baseRuntimeFlowableDelegate.isDone()).thenReturn(true);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Object mockedObject = new Object();
        Mockito.when(baseRuntimeFlowableDelegate.get()).thenReturn(mockedObject);
        assertEquals(mockedObject, baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException {
        Object mockedObject = new Object();
        Mockito.when(baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS)).thenReturn(mockedObject);
        assertEquals(mockedObject, baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testRun() throws Exception {
        Mockito.doNothing().when(baseRuntimeFlowableDelegate).run(mockExecution);
        baseRuntimeFlowableDelegate.run(mockExecution);
        Mockito.verify(baseRuntimeFlowableDelegate, Mockito.times(1)).run(mockExecution);
    }

    @Test
    public void testExecute() {
        Mockito.doNothing().when(baseRuntimeFlowableDelegate).execute(mockExecution);
        baseRuntimeFlowableDelegate.execute(mockExecution);
        Mockito.verify(baseRuntimeFlowableDelegate, Mockito.times(1)).execute(mockExecution);
    }
}
