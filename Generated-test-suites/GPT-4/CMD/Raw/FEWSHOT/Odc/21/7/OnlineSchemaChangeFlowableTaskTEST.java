package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.*;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {
    
    @Mock
    private FlowableExecutionDelegate executionDelegateMock;

    @InjectMocks
    OnlineSchemaChangeFlowableTask task;

    @MockBean
    private DelegateExecution delegateExecution;

    @MockBean
    private TaskService taskService;

    @MockBean
    private User creator;

    @MockBean
    private ConnectionConfig connectionConfig;

    @MockBean
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() throws Exception {
        when(creator.getId()).thenReturn(Long.valueOf(1));
        when(creator.getOrganizationId()).thenReturn(Long.valueOf(1));
        when(flowableExecutionHelper.getCloudMainAccountId(delegateExecution)).thenReturn("uid");
        when(flowableExecutionHelper.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(onlineSchemaChangeParameters);
        when(flowableExecutionHelper.getConnectionConfig(delegateExecution)).thenReturn(connectionConfig);
        when(flowableExecutionHelper.getSchemaName(delegateExecution)).thenReturn("schema");
    }

    @Test
    public void testStartTask() {
        task.start(Long.valueOf(1), taskService, delegateExecution);
        verify(taskService).start(Long.valueOf(1));
        assertEquals(task.status, TaskStatus.RUNNING);
    }

    @Test
    public void testIsSuccessful() {
        task.status = TaskStatus.DONE;
        assertTrue(task.isSuccessful());
        task.status = TaskStatus.RUNNING;
        assertFalse(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.status = TaskStatus.FAILED;
        assertTrue(task.isFailure());
        task.status = TaskStatus.RUNNING;
        assertFalse(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        task.onFailure(Long.valueOf(1), taskService);
        verify(taskService).updateTaskStatus(Long.valueOf(1), TaskStatus.FAILED);
    }

    @Test
    public void testOnSuccessful() {
        task.status = TaskStatus.DONE;
        task.onSuccessful(Long.valueOf(1), taskService);
        verify(taskService).updateTaskStatus(Long.valueOf(1), TaskStatus.DONE);
    }

    @Test
    public void testOnTimeout() {
        task.onTimeout(Long.valueOf(1), taskService);
        verify(taskService).updateTaskStatus(Long.valueOf(1), TaskStatus.TIMEOUT);
    }

    @Test
    public void testCancelTask() {
        when(taskService.cancelTask(anyLong())).thenReturn(Boolean.TRUE);
        assertTrue(task.cancel(true, Long.valueOf(1), taskService));
        assertEquals(task.status, TaskStatus.CANCELED);
    }

    @Test
    public void testIsCancelled() {
        task.status = TaskStatus.CANCELED;
        assertTrue(task.isCancelled());
        task.status = TaskStatus.RUNNING;
        assertFalse(task.isCancelled());
    }

    /*** Add more unit tests as necessary ***/
}