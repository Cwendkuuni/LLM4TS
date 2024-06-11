import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FlowTaskInstanceTest {
    @Test
    public void testCreate() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.create();
        verify(instance, times(1)).create();
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.update();
        verify(instance, times(1)).update();
    }

    @Test
    public void testAllocAndDealloc() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.alloc();
        instance.dealloc();
        verify(instance, times(1)).alloc();
        verify(instance, times(1)).dealloc();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelNotOnThisMachine() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        when(instance.isPresentOnThisMachine()).thenReturn(false);
        instance.cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneNotOnThisMachine() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        when(instance.isPresentOnThisMachine()).thenReturn(false);
        instance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultNotOnThisMachine() throws InterruptedException, ExecutionException {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        when(instance.isPresentOnThisMachine()).thenReturn(false);
        instance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbortWithAutoStrategy() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        when(instance.getStrategyConfig().getStrategy()).thenReturn(FlowTaskExecutionStrategy.AUTO);
        instance.abort();
    }

    @Test
    public void testGetTargetDelegateClass() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        assertNotNull(instance.getTargetDelegateClass());
    }

    @Test
    public void testGetterAndSetter() {
        FlowTaskInstance instance = new FlowTaskInstance();
        Long expectedId = 1L;
        instance.setTargetTaskId(expectedId);
        assertEquals(expectedId, instance.getTargetTaskId());
    }
}