
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.task.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OnlineSchemaChangeFlowableTaskTest {
   
    @Mock
    private DelegateExecution execution;
    @Mock
    private TaskService taskService;
    @Mock
    private User creator;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private OnlineSchemaChangeParameters parameters;
    
    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testStart() throws Exception {
        when(execution.getVariable("creator", User.class)).thenReturn(creator);
        when(execution.getVariable("connectionConfig", ConnectionConfig.class)).thenReturn(connectionConfig);
        when(execution.getVariable("parameters", OnlineSchemaChangeParameters.class)).thenReturn(parameters);
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }

    // add more tests
}

