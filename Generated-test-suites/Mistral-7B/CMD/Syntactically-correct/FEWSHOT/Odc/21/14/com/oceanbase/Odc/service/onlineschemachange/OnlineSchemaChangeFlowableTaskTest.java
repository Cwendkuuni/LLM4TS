package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.test.util.TestUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private OscTaskCompleteHandler taskHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = TestUtil.createMockConnectionConfig();
        OnlineSchemaChangeParameters parameter = TestUtil.createMockOnlineSchemaChangeParameters();
        String schema = "test_schema";
        ScheduleEntity expectedScheduleEntity = TestUtil.createMockScheduleEntity();

        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(expectedScheduleEntity);

        ScheduleEntity actualScheduleEntity = testee.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(expectedScheduleEntity, actualScheduleEntity);
        verify(scheduleService).create(any(ScheduleEntity.class));
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = TestUtil.createMockOnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity expectedScheduleTaskEntity = TestUtil.createMockScheduleTaskEntity();

        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(expectedScheduleTaskEntity);

        ScheduleTaskEntity actualScheduleTaskEntity = testee.createScheduleTaskEntity(scheduleId, param);

        assertEquals(expectedScheduleTaskEntity, actualScheduleTaskEntity);
        verify(scheduleTaskRepository).saveAndFlush(any(ScheduleTaskEntity.class));
    }

    @Test
    public void testStart() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        User creator = TestUtil.createMockUser();
        this.creatorId = creator.getId();
        this.organizationId = creator.getOrganizationId();

        OnlineSchemaChangeParameters parameter = TestUtil.createMockOnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = TestUtil.createMockConnectionConfig();
        String schema = "test_schema";

        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);

        doNothing().when(taskService).start(taskId);

        testee.start(taskId, taskService, execution);

        verify(taskService).start(taskId);
    }

    // Add more test methods for other methods in the class

    // ...
}