package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {
    @Test
    public void testStart() throws Exception {
        //Initializing Mock Objects
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        User creator = mock(User.class);
        when(creator.getId()).thenReturn(100L);
        when(execution.getVariable("taskCreator", User.class)).thenReturn(creator);

        //Test Object
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        //Executing method
        task.start(1L, taskService, execution);

        //Here, verify that certain methods were called using Mockito's verify method
        //For example:
        verify(taskService, times(1)).start(1L);
    }
    
    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.id = 1L;
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schema";

        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        ScheduleEntity actual = task.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(1L, actual.getConnectionId());
        assertEquals(schema, actual.getDatabaseName());
        assertEquals(1L, actual.getCreatorId());
    }
}