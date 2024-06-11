
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask task;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    TaskService taskService;

    @Before
    public void setUp() throws Exception {
        task = new OnlineSchemaChangeFlowableTask();
        task.setScheduleTaskRepository(Mockito.mock(ScheduleTaskRepository.class));
        task.setScheduleService(Mockito.mock(ScheduleService.class));
        task.setScheduleTaskService(Mockito.mock(ScheduleTaskService.class));
        task.setQuartzJobService(Mockito.mock(QuartzJobService.class));
        task.setTaskHandler(Mockito.mock(OnlineSchemaChangeTaskHandler.class));
        task.setCompleteHandler(Mockito.mock(OscTaskCompleteHandler.class));
        task.setOrganizationService(Mockito.mock(OrganizationService.class));

        when(delegateExecution.getVariable(eq("creator"))).thenReturn(new User());
        when(delegateExecution.getVariable(eq("mainAccountId"))).thenReturn("mockUid");
        when(delegateExecution.getVariable(eq("parameters"))).thenReturn(new OnlineSchemaChangeParameters());
        when(delegateExecution.getVariable(eq("connectionConfig"))).thenReturn(new ConnectionConfig());
        when(delegateExecution.getVariable(eq("schema"))).thenReturn("mockSchema");
    }

    @Test
    public void testStartMethod() throws Exception {
        task.start(1L, taskService, delegateExecution);
        verify(taskService, times(1)).start(anyLong());
    }

    @Test
    public void testIsSuccessful() {
        task.setStatus(TaskStatus.DONE);
        assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.setStatus(TaskStatus.FAILED);
        assertTrue(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        task.setStatus(TaskStatus.FAILED);
        task.onFailure(1L, taskService);
        verify(taskService, times(1)).update(any());
        assertEquals(TaskStatus.FAILED, task.getStatus());
    }

    @Test
    public void testOnSuccessful() {
        task.setStatus(TaskStatus.DONE);
        task.onSuccessful(1L, taskService);
        verify(taskService, times(1)).update(any());
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    public void testOnTimeout() {
        task.setStatus(TaskStatus.PREPARING);
        task.onTimeout(1L, taskService);
        assertEquals(TaskStatus.PREPARING, task.getStatus());
    }

    @Test
    public void testOnProgressUpdate() {
        task.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskService.listTask(Mockito.any(), Mockito.anyLong()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        task.onProgressUpdate(1L, taskService);
        assertEquals(TaskStatus.RUNNING, task.getStatus());
    }

    @Test
    public void testCancel() {
        task.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskService.listTask(Mockito.any(Pageable.class), Mockito.anyLong()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        task.cancel(true, 1L, taskService);
        assertTrue(task.isCancelled());
    }

}
