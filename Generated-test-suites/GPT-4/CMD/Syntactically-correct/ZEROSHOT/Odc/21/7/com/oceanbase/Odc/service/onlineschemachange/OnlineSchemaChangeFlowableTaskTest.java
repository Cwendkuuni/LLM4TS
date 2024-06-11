package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.stream.Stream;
import java.lang.Exception;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;


public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private DelegateExecution execution;

    @Mock
    private TaskService taskService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        when(taskService.start(anyLong())).thenReturn(true);
        assertNull(onlineSchemaChangeFlowableTask.start(1L, taskService, execution));
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

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        verify(taskService, times(1)).fail(1L);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(taskService, times(1)).end(1L);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
        verify(taskService, times(1)).fail(1L);
    }

    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = mock(Page.class);

        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);
        when(tasks.stream()).thenReturn(Stream.of(new ScheduleTaskEntity()));
        assertNotNull(onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService));
    }

    @Test
    public void testCancel() {
        when(taskService.cancel(anyLong())).thenReturn(true);
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
    }


    @Test
    public void testisCancelled() {
        OnlineSchemaChangeFlowableTask online = new OnlineSchemaChangeFlowableTask();
        online.status = TaskStatus.CANCELED;
        assertTrue(online.isCancelled());
    }
}
