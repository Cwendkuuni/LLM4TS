package com.oceanbase.odc.service.onlineschemachange;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private TaskService taskService;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    private ScheduleTaskEntity taskEntity;
    
    @Before
    public void setUp() {
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.PREPARING);
    }

    @Test
    public void testStart() throws Exception {
        onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);
        Mockito.verify(taskService).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        taskEntity.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }
    
    @Test
    public void testIsFailure() {
        taskEntity.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        Mockito.verify(taskService).cancel(1L);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        Mockito.verify(taskService).complete(1L);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
        Mockito.verify(taskService).timesOut(1L);
    }

    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(Arrays.asList(taskEntity));
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(tasks);
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        Mockito.verify(taskService).update(taskEntity);
    }

    @Test
    public void testCancel() {
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
    }

    @Test
    public void testIsCancelled() {
        taskEntity.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}
