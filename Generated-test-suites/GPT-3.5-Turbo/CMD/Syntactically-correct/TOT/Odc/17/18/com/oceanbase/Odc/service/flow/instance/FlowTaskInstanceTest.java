import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private TaskService taskServiceMock;

    @Before
    public void setUp() {
        taskServiceMock = mock(TaskService.class);
        flowTaskInstance = new FlowTaskInstance(TaskType.TYPE_A, 1L, 1L, new ExecutionStrategyConfig(), true, false, null, null, null, taskServiceMock, null, null, null);
    }

    @Test
    public void testComplete() {
        // Typical use case
        flowTaskInstance.create();
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.confirmExecute();
        assertTrue(flowTaskInstance.isDone());

        // Edge case
        flowTaskInstance.create();
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.confirmExecute();
        assertFalse(flowTaskInstance.isDone());

        // Error scenario
        flowTaskInstance.abort();
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testCancel() {
        // Typical use case
        flowTaskInstance.create();
        BaseRuntimeFlowableDelegate<?> delegateMock = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegateMock);
        flowTaskInstance.cancel(false);
        verify(delegateMock, times(1)).cancel(false);

        // Edge case
        flowTaskInstance.create();
        flowTaskInstance.cancel(true);

        // Error scenario
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.cancel(false));
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Typical use case
        flowTaskInstance.create();
        BaseRuntimeFlowableDelegate<?> delegateMock = mock(BaseRuntimeFlowableDelegate.class);
        when(delegateMock.get()).thenReturn("Result");
        flowTaskInstance.bindServiceTask(delegateMock);
        assertEquals("Result", flowTaskInstance.getResult());

        // Edge case
        flowTaskInstance.create();
        when(delegateMock.get(anyLong(), eq(TimeUnit.SECONDS))).thenReturn("Result");
        assertEquals("Result", flowTaskInstance.getResult(10, TimeUnit.SECONDS));

        // Error scenario
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult());
    }

}