package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mockito;

public class OnlineSchemaChangeFlowableTaskTest {
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

    @Test
    public void testStart() {
        Long taskId = 1L;
        TaskService taskServiceMock = Mockito.mock(TaskService.class);
        DelegateExecution executionMock = Mockito.mock(DelegateExecution.class);

        when(taskServiceMock.start(anyLong())).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.start(taskId, taskServiceMock, executionMock);

        verify(taskServiceMock).start(taskId);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }
}
