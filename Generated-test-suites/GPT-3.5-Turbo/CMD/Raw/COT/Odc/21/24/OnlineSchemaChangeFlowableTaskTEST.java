
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Test
    public void testStart() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = new TaskService();
        DelegateExecution execution = new DelegateExecution();

        try {
            task.start(taskId, taskService, execution);
            assertTrue(task.isSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        assertTrue(!task.isFailure());
    }

    @Test
    public void testOnFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = new TaskService();

        task.onFailure(taskId, taskService);
        assertTrue(!task.isCancelled());
    }

    @Test
    public void testOnSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = new TaskService();

        task.onSuccessful(taskId, taskService);
        assertTrue(!task.isCancelled());
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = new TaskService();

        task.onTimeout(taskId, taskService);
        assertTrue(!task.isCancelled());
    }

    @Test
    public void testOnProgressUpdate() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = new TaskService();

        task.onProgressUpdate(taskId, taskService);
        assertTrue(!task.isCancelled());
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = new TaskService();

        assertTrue(task.cancel(true, taskId, taskService));
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        assertTrue(!task.isCancelled());
    }

}
