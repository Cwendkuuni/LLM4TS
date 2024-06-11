
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;

import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private DelegateExecution execution;

    @Mock
    private TaskService taskService;

    @Mock
    private User creator;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters parameter;

    @Mock
    private ScheduleEntity schedule;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private Page<ScheduleTaskEntity> tasks;

    @Test
    public void testStartAndGetValueMethods() throws Exception {
        Mockito.when(creator.getId()).thenReturn(1L);
        Mockito.when(creator.getOrganizationId()).thenReturn(1L);
        Mockito.when(schedule.getId()).thenReturn(1L);

        Mockito.when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        Mockito.when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        Mockito.when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);

        Mockito.when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(schedule);

        assertNull(onlineSchemaChangeFlowableTask.start(1L, taskService, execution));
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        Mockito.verify(taskService, Mockito.times(1)).fail(Mockito.anyLong());
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        Mockito.verify(taskService, Mockito.times(1)).success(Mockito.anyLong());
    }

    @Test
    public void testCancel() {
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "");

        assertNotNull(scheduleEntity);
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertNotNull(scheduleEntity.getTriggerConfigJson());
    }

    @Test
    public void testOnProgressUpdate() {
        Mockito.when(tasks.stream()).thenReturn(Collections.singletonList(scheduleTaskEntity).stream());

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
    }
}
