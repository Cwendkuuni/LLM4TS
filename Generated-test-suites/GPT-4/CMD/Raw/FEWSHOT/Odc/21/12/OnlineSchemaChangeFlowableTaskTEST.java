package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateScheduleEntity(){
        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "test_schema");

        assertNotNull(result);
        assertEquals(result.getDatabaseName(), "test_schema");
        assertEquals(result.getStatus(), ScheduleStatus.ENABLED);
        assertEquals(result.getTriggerConfig().getTriggerStrategy(), TriggerStrategy.CRON);
    }


    @Test
    public void testCreateScheduleTaskEntity(){
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param);

        assertNotNull(result);
        assertEquals(result.getJobName(), "1");
    }

    @Test
    public void testIsSuccessful() {
        when(task.getStatus()).thenReturn(TaskStatus.DONE);                  
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsNotSuccessful() {
        when(task.getStatus()).thenReturn(TaskStatus.PREPARING);   
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {                 
        when(task.getStatus()).thenReturn(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testIsNotFailure() { 
        when(task.getStatus()).thenReturn(TaskStatus.RUNNING);
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }   

    @Test
    public void testIsCancelled() {                 
        when(task.getStatus()).thenReturn(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }   

    @Test
    public void testIsNotCancelled() {                 
        when(task.getStatus()).thenReturn(TaskStatus.PREPARING); 
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    } 
   
    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        verify(taskService, times(1)).onFailure(1L, taskService);
    }

    @Test
    public void testOnSuccessful(){
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(taskService, times(1)).onSuccessful(1L, taskService);
    }

    @Test
    public void testStartOver(){
        User mockUser = mock(User.class);
        DelegateExecution mockExecution = mock(DelegateExecution.class);
        onlineSchemaChangeFlowableTask.start(1L, taskService, mockExecution);
        verify(taskService, times(1)).start(taskId);
    }

    @Test
    public void testCancel(){
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
        assertEquals(task.getStatus(), TaskStatus.CANCELED);
    }
}