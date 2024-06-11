package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.*;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask task;

    @Test
    public void testStart() {
        task = spy(new OnlineSchemaChangeFlowableTask());
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        // Mock necessary methods
        doReturn(1L).when(execution).getVariable("taskId");
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User(1L, 1L));
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("1");
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("public");

        try {
            task.start(1L, taskService, execution);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify the method calls
        verify(taskService, times(1)).start(1L);
        verify(taskHandler, times(1)).start(anyLong(), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.DONE);
        assertEquals(true, task.isSuccessful());

        task.setStatus(TaskStatus.RUNNING);
        assertEquals(false, task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.FAILED);
        assertEquals(true, task.isFailure());

        task.setStatus(TaskStatus.RUNNING);
        assertEquals(false, task.isFailure());
    }

    @Test
    public void testCancel() {
        task = spy(new OnlineSchemaChangeFlowableTask());
        TaskService taskService = mock(TaskService.class);

        when(scheduleTaskService.listTask(any(), anyLong()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        task.cancel(true, 1L, taskService);

        // Verify the method calls
        verify(taskHandler, times(1)).terminate(anyLong(), anyLong());
        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testIsCancelled() {
        task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.CANCELED);
        assertEquals(true, task.isCancelled());

        task.setStatus(TaskStatus.RUNNING);
        assertEquals(false, task.isCancelled());
    }

}