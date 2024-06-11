package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask task;
    
    @Before
    public void setUp() {
        task = new OnlineSchemaChangeFlowableTask();
        task.setTaskHandler(mock(OnlineSchemaChangeTaskHandler.class));
        task.setScheduleTaskRepository(mock(ScheduleTaskRepository.class));
        task.setScheduleService(mock(ScheduleService.class));
        task.setScheduleTaskService(mock(ScheduleTaskService.class));
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setContinueOnError(true);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameters);
        
        task.start(taskId, taskService, execution);
        
        assertEquals(TaskStatus.RUNNING, task.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        task.setStatus(TaskStatus.DONE);
        assertEquals(true, task.isSuccessful());
    }
    
    @Test
    public void testIsFailure() {
        task.setStatus(TaskStatus.FAILED);
        assertEquals(true, task.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onFailure(taskId, taskService);
        // Add assertions if needed
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onSuccessful(taskId, taskService);
        // Add assertions if needed
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onTimeout(taskId, taskService);
        // Add assertions if needed
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onProgressUpdate(taskId, taskService);
        // Add assertions if needed
    }

    @Test
    public void testCancel() {
        boolean mayInterruptIfRunning = true;
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        assertEquals(true, task.cancel(mayInterruptIfRunning, taskId, taskService));
    }

    @Test
    public void testIsCancelled() {
        task.setStatus(TaskStatus.CANCELED);
        assertEquals(true, task.isCancelled());
    }

}