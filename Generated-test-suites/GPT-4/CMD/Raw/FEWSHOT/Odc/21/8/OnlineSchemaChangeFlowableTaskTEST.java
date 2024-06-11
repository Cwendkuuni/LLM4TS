package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.task.TaskService;
import java.lang.reflect.Field;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private TaskService mockTaskService = mock(TaskService.class);
    private DelegateExecution mockExecution = mock(DelegateExecution.class);
    
    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void testStart() throws Exception {
        Field statusField = OnlineSchemaChangeFlowableTask.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(onlineSchemaChangeFlowableTask, TaskStatus.RUNNING);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        
        when(mockExecution.getVariable("user")).thenReturn(new User(1,"user1", 1));
        when(mockExecution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameters);
        when(mockExecution.getVariable("connectionConfig")).thenReturn(new ConnectionConfig());
        when(mockExecution.getVariable("schema")).thenReturn("schema");
        
        doNothing().when(mockTaskService).start(1L);
        
        onlineSchemaChangeFlowableTask.start(1L, mockTaskService, mockExecution);
        
        assertEquals(TaskStatus.RUNNING, statusField.get(onlineSchemaChangeFlowableTask));
    }

    @Test
    public void testIsSuccessful() {
        Field statusField = OnlineSchemaChangeFlowableTask.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(onlineSchemaChangeFlowableTask, TaskStatus.DONE);
        
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
        
        statusField.set(onlineSchemaChangeFlowableTask, TaskStatus.RUNNING);
        
        assertEquals(false, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        Field statusField = OnlineSchemaChangeFlowableTask.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(onlineSchemaChangeFlowableTask, TaskStatus.FAILED);
        
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
        
        statusField.set(onlineSchemaChangeFlowableTask, TaskStatus.RUNNING);
        
        assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());
    }
    
    //Similar test cases can be written for remaining methods. It is not possible to cover all with the provided context and due to the complexity/dependencies of the class.
    
}