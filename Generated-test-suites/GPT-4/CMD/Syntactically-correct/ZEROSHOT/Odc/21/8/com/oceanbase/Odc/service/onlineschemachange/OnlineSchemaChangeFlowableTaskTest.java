
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private Pageable pageable;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    void testStart() throws Exception {
        // Prepare test data
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(2L);

        // Mock methods
        when(delegateExecution.getVariable("creator")).thenReturn(user);
        when(delegateExecution.getVariable("onlineSchemaChangeParameters")).thenReturn(new OnlineSchemaChangeParameters());
        when(delegateExecution.getVariable("connectionConfig")).thenReturn(new ConnectionConfig());
        doNothing().when(scheduleTaskRepository).saveAndFlush(any());

        // Do test
        onlineSchemaChangeFlowableTask.start(1L, null, delegateExecution);

        // Verify result
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertEquals(true, onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasksList = mock(Page.class);
        ScheduleTaskEntity entity = new ScheduleTaskEntity();
        entity.setStatus(TaskStatus.RUNNING);
        List<ScheduleTaskEntity> taskEntities = new ArrayList<>();
        taskEntities.add(entity);
        when(tasksList.getSize()).thenReturn(1);
        when(tasksList.stream()).thenReturn(taskEntities.stream());

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, null);

        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    void testProgressStatusUpdate() {
        Page<ScheduleTaskEntity> tasksList = mock(Page.class);
        ScheduleTaskEntity entity = new ScheduleTaskEntity();
        entity.setStatus(TaskStatus.DONE);
        List<ScheduleTaskEntity> taskEntities = new ArrayList<>();
        taskEntities.add(entity);
        when(tasksList.getSize()).thenReturn(1);
        when(tasksList.stream()).thenReturn(taskEntities.stream());

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasksList);

        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(10.0);
        double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(0.1, result, 0.001);
    }

    @Test
    void testCancel() {
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, null));
        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters parameters = new OnlineSchemaChangeScheduleTaskParameters();
        parameters.setUid("1");

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, parameters);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
    }

    @Test
    void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setContinueOnError(true);

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameters, "schema");
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
    }
}
