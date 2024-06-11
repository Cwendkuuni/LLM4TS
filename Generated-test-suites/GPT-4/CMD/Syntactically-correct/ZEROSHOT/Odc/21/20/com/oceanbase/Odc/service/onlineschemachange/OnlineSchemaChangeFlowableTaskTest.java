package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;
    
    @Mock
    private User user;
    
    @Mock
    private DelegateExecution delegateExecution;
    
    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    
    @Mock
    private ConnectionConfig connectionConfig;

    @Test
    public void testStart() throws Exception {
        when(delegateExecution.getVariable("creator")).thenReturn(user);
        when(delegateExecution.getVariable("onlineSchemaChangeParameters")).thenReturn(onlineSchemaChangeParameters);
        when(delegateExecution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        assertNotNull(onlineSchemaChangeFlowableTask.start(1L, mock(TaskService.class), delegateExecution));
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
        onlineSchemaChangeFlowableTask.onFailure(1L, mock(TaskService.class));
        verify(oscTaskCompleteHandler, times(1)).onFailure();
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, mock(TaskService.class));
        verify(oscTaskCompleteHandler, times(1)).onSuccess();
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, mock(TaskService.class));
        verify(oscTaskCompleteHandler, times(1)).onTimeout();
    }

    @Test
    public void testCancel() {
        when(onlineSchemaChangeFlowableTask.status).thenReturn(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, mock(TaskService.class)));
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}
