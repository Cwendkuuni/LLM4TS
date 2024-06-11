package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OnlineSchemaChangeFlowableTaskTest {
    
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
        
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

}