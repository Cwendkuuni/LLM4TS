
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.task.TaskEntity;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeParameters parameters = mock(OnlineSchemaChangeParameters.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        User user = mock(User.class);

        when(execution.getVariable("currentTaskCreator")).thenReturn(user);
        when(execution.getVariable("onlineSchemaChangeParameter")).thenReturn(parameters);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);

        onlineSchemaChangeFlowableTask.start(1L, null, execution);
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, null);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, null);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, null);
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "dummy_schema");
        assert(scheduleEntity instanceof ScheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeParameters parameters = mock(OnlineSchemaChangeParameters.class);
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, parameters);
        assert (scheduleTaskEntity instanceof ScheduleTaskEntity);
    }

    @Test
    public void testOnProgressUpdate() {
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.STARTED);
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, null);
    }

    @Test
    public void testCheckTaskStatus() {
        assert(onlineSchemaChangeFlowableTask.isSuccessful());
        assert(!onlineSchemaChangeFlowableTask.isFailure());
    }
}
