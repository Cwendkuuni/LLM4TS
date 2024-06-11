package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;

import java.util.Date;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask task;

    @Before
    public void setUp() {
        task = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void testTaskStatus() {
        assertEquals(TaskStatus.PREPARING, task.status);
    }
  
    @Test
    public void testScheduleId() {
        assertTrue(task.scheduleId > 0);
    }
  
    @Test
    public void testOrganizationId() {
        assertTrue(task.organizationId > 0);
    }

    @Test
    public void testContinueOnError() {
        assertFalse(task.continueOnError);
    }

    @Test
    public void testPercentage() {
        assertEquals(0.0, task.percentage, 0.00001);
    }

    @Test
    public void testSuccessful() {
        assertFalse(task.isSuccessful());
    }
    
    @Test
    public void testFailure() {
        assertFalse(task.isFailure());
    }
    
    @Test
    public void testCancelled() {
        assertFalse(task.isCancelled());
    }

}