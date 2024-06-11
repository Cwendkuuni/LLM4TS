package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeFlowableTaskTest
{
    private OnlineSchemaChangeFlowableTask task;

    @Before
    public void init()    {
        task = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void testStart()    {  
        try {
            Long taskId = new Long(1);
            TaskService taskService = new MockTaskService(); 
            DelegateExecution execution = new MockDelegateExecution(); 

            task.start(taskId, taskService, execution);
            
            // We assert that the method executes without throwing an exception.
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }
}