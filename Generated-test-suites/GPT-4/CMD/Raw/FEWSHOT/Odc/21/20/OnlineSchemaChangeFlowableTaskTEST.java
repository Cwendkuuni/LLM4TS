package com.oceanbase.odc.service.onlineschemachange;

import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class OnlineSchemaChangeFlowableTaskTest {
  
    private OnlineSchemaChangeFlowableTask task;
    private DelegateExecution execution;
    private User user;
    private OnlineSchemaChangeParameters params;
    private ConnectionConfig config;
    private TaskService taskService;
  
    @Before
    public void setUp() {
      task = new OnlineSchemaChangeFlowableTask();
      execution = Mockito.mock(DelegateExecution.class);
      user = Mockito.mock(User.class);
      params = Mockito.mock(OnlineSchemaChangeParameters.class);
      config = Mockito.mock(ConnectionConfig.class);
      taskService = Mockito.mock(TaskService.class);
    }

    @Test
    public void testStart() throws Exception {
      when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
      when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(params);
      when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(config);
      when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schema");
      when(params.isContinueOnError()).thenReturn(true);
      
      task.start(1L, taskService, execution);
      verify(taskService).start(1L);
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
        long taskId = 1L;
        task.onFailure(taskId, taskService);
        verify(taskService).fail(taskId);
    }

    @Test
    public void testOnSuccessful() {
        long taskId = 1L;
        task.onSuccessful(taskId, taskService);
        verify(taskService).complete(taskId);
    }

    @Test
    public void testOnTimeout() {
        long taskId = 2L;
        task.onTimeout(taskId, taskService);
        verify(taskService).fail(taskId);
    }

    @Test
    public void testCancel() {
        long taskId = 3L;
        assertTrue(task.cancel(true, taskId, taskService));
        verify(taskService).cancel(taskId);
    }

    @Test
    public void testIsCancelled() {
        task.status = TaskStatus.CANCELED;
        assertTrue(task.isCancelled());

        task.status = TaskStatus.RUNNING;
        assertFalse(task.isCancelled());
    }
}