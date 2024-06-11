package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask task;
    private Long validTaskId;
    private TaskService taskService;

	private DelegateExecution execution;

    @Before
    public void setUp() {
        task = new OnlineSchemaChangeFlowableTask();
        validTaskId = 1L;
        taskService = mock(TaskService.class);
        execution = mock(DelegateExecution.class);
    }

    @Test
    public void testStart() {
        try {
            task.start(validTaskId, taskService, execution);
            verify(taskService,times(1)).start(anyLong());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testIsSuccessful() {
        assertFalse(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        task.onFailure(validTaskId, taskService);
        verify(taskService,times(1)).completeWithFailure(anyLong());
    }

    @Test
    public void testOnSuccessful() {
        task.onSuccessful(validTaskId, taskService);
        verify(taskService,times(1)).complete(anyLong());
    }

    @Test
    public void testOnTimeout() {
        task.onTimeout(validTaskId, taskService);
        verify(taskService,Mockito.times(0)).complete(anyLong());
    }

    @Test
    public void testOnProgressUpdate() {
        task.onProgressUpdate(validTaskId, taskService);
        verify(taskService,Mockito.times(0)).update(any());
    }

    @Test
    public void testCancel() {
        boolean result = task.cancel(true, validTaskId, taskService);
        assertTrue(result);
        verify(taskService,times(1)).cancel(anyLong());
    }

    @Test
    public void testIsCancelled() {
        assertFalse(task.isCancelled());
    }
}