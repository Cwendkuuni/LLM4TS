
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskService mockScheduleTaskService;

    @Mock
    private TaskService mockTaskService;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleId(123L);
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 456L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        when(mockTaskService.detail(taskId)).thenReturn(new TaskEntity());

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        verify(taskService, times(1)).start(taskId);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());

        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
        assertEquals(false, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());

        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
        assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 123L;
        TaskService taskService = mock(TaskService.class);

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 123L;
        TaskService taskService = mock(TaskService.class);

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(taskService, times(1)).update(any());
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 123L;
        TaskService taskService = mock(TaskService.class);

        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verifyZeroInteractions(taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 123L;
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);
        taskEntity.setProgressPercentage(50.0);
        List<ScheduleTaskEntity> taskList = new ArrayList<>();
        taskList.add(taskEntity);

        when(mockScheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);
        when(tasks.getSize()).thenReturn(1);
        when(tasks.getContent()).thenReturn(taskList);
        when(tasks.stream()).thenReturn(taskList.stream());

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(taskService, times(1)).update(any());
    }

    @Test
    public void testCancel() {
        Long taskId = 123L;
        TaskService taskService = mock(TaskService.class);

        boolean result = onlineSchemaChangeFlowableTask.cancel(false, taskId, taskService);

        assertEquals(true, result);
        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertEquals(true, onlineSchemaChangeFlowableTask.isCancelled());

        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
        assertEquals(false, onlineSchemaChangeFlowableTask.isCancelled());
    }
}

