
package com.oceanbase.odc.service.onlineschemachange;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskService scheduleTaskService;
    @Mock
    private TaskService taskService;

    @Test
    public void testStartSuccess() throws Exception {
        assertDoesNotThrow(() -> onlineSchemaChangeFlowableTask.start(1L, taskService, mock(DelegateExecution.class)));
        assertDoesNotThrow(() -> onlineSchemaChangeFlowableTask.start(null, null, null));
    }

    @Test
    public void testIsSuccessful() {
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        assertDoesNotThrow(() -> onlineSchemaChangeFlowableTask.onFailure(1L, taskService));
    }

    @Test
    public void testOnSuccessful() {
        assertDoesNotThrow(() -> onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService));
    }

    @Test
    public void testOnTimeout() {
        assertDoesNotThrow(() -> onlineSchemaChangeFlowableTask.onTimeout(1L, taskService));
    }

    @Test
    public void testOnProgressUpdate() {
        when(scheduleTaskService.listTask(any(), anyLong())).thenReturn(new PageImpl<>(Arrays.asList(new ScheduleTaskEntity())));
        assertDoesNotThrow(() -> onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService));
    }

    @Test
    public void testCancel() {
        when(scheduleTaskService.listTask(any(), anyLong())).thenReturn(new PageImpl<>(Arrays.asList(new ScheduleTaskEntity())));
        assertTrue(onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService));
    }

    @Test
    public void testIsCancelled() {
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }
}
