
package com.oceanbase.odc.service.onlineschemachange;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private DelegateExecution mockExecution;
    @Mock
    private TaskService taskService;
    @Mock
    private User mockUser;
    @Mock
    private ConnectionConfig mockConnectionConfig;
    @Mock
    private OnlineSchemaChangeParameters mockOnlineSchemaChangeParameters;
    @Mock
    private TaskEntity taskEntity;
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() throws Exception {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.creatorId = 1L;
        onlineSchemaChangeFlowableTask.organizationId = 1L;
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        onlineSchemaChangeFlowableTask.scheduleId = 1L;
    }

    @Test
    public void testStart() throws Exception {
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getOrganizationId()).thenReturn(1L);
        when(mockExecution.getVariable(Mockito.anyString())).thenReturn(mockUser, mockOnlineSchemaChangeParameters, mockConnectionConfig, "testSchema");
        
        onlineSchemaChangeFlowableTask.start(1L, taskService, mockExecution);
        verify(taskService).start(any(Long.class));
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
        verify(taskService, times(0)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(taskService, times(0)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnProgressUpdate() {
        List<ScheduleTaskEntity> scheduleTaskEntityList = Arrays.asList(scheduleTaskEntity);
        when(taskService.detail(any(Long.class))).thenReturn(taskEntity);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskEntity.getProgressPercentage()).thenReturn(50.0);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(taskEntity.getProgressPercentage()).thenReturn(40.0);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testCancel() {
        when(taskService.cancel(any(Long.class))).thenReturn(true);
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}
