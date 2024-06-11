package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.flowable.engine.delegate.DelegateExecution;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    TaskService taskService;

    @Mock
    DelegateExecution delegateExecution;

    @Test
    public void testStart() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        
        when(FlowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(user);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(new OnlineSchemaChangeParameters());
        when(FlowTaskUtil.getConnectionConfig(delegateExecution)).thenReturn(new ConnectionConfig());
        when(FlowTaskUtil.getSchemaName(delegateExecution)).thenReturn("TestSchema");

        onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);

        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
    }
    
    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }
    
    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
    
    @Test
    public void testCancel() {
        when(taskService.cancel(anyLong())).thenReturn(true);

        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
    }
        
    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> page = mock(Page.class);
        when(page.getSize()).thenReturn(3);
        when(page.getContent()).thenReturn(List.of(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity()));
        when(page.stream()).thenReturn(Stream.of(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity()));
        
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService, page);
    }
}