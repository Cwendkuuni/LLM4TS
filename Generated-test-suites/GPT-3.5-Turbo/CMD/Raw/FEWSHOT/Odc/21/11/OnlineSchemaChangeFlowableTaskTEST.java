package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

import java.util.ArrayList;
import java.util.List;

public class OnlineSchemaChangeFlowableTaskTest {

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeParameters parameters = mock(OnlineSchemaChangeParameters.class);
        when(parameters.isContinueOnError()).thenReturn(true);

        task.start(1L, taskService, execution);

        assertEquals(task.getCreatorId(), 0);
        assertEquals(task.getStatus(), TaskStatus.RUNNING);
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.DONE);

        assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.FAILED);

        assertTrue(task.isFailure());
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        task.setStatus(TaskStatus.RUNNING);

        assertTrue(task.cancel(true, 1L, taskService));
        assertEquals(task.getStatus(), TaskStatus.CANCELED);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.CANCELED);

        assertTrue(task.isCancelled());
    }

    @Test
    public void testProgressStatusUpdate() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity task1 = new ScheduleTaskEntity();
        task1.setStatus(TaskStatus.DONE);
        ScheduleTaskEntity task2 = new ScheduleTaskEntity();
        task2.setStatus(TaskStatus.FAILED);
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);

        task.progressStatusUpdate(tasks);

        assertEquals(task.getStatus(), TaskStatus.FAILED);
    }

    @Test
    public void testSingleTaskPercentage() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity task1 = new ScheduleTaskEntity();
        task1.setStatus(TaskStatus.PREPARING);

        assertEquals(task.singleTaskPercentage(task1), 0.0, 0.001);
    }
}