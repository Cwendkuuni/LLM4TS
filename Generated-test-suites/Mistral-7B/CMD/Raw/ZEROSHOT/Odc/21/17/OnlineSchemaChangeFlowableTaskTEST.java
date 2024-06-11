package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedInternalContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;

@ContextConfiguration
@SpringJUnitConfig
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
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private FlowTaskUtil flowTaskUtil;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() {
        DelegateExecution execution = mock(DelegateExecution.class);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schemaName";
        when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(flowTaskUtil.getSchemaName(execution)).thenReturn(schema);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // do nothing
                return null;
            }
        }).when(quartzJobService).start(anyLong(), anyLong());

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // do nothing
                return null;
            }
        }).when(quartzJobService).terminate(anyLong(), anyLong());

        doAnswer(new Answer<Page<ScheduleTaskEntity>>() {
            @Override
            public Page<ScheduleTaskEntity> answer(InvocationOnMock invocation) throws Throwable {
                List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
                ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
                scheduleTask.setStatus(TaskStatus.DONE);
                scheduleTasks.add(scheduleTask);
                return new PageImpl<>(scheduleTasks);
            }
        }).when(scheduleTaskService).listTask(any(Pageable.class), anyLong());

        testee.start(1L, mock(TaskService.class), execution);

        verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), anyLong());
        verify(quartzJobService, times(1)).start(anyLong(), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        assertTrue(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(testee.isFailure());
    }

    @Test
    public void testOnFailure() {
        // TODO: Implement this test
    }

    @Test
    public void testOnSuccessful() {
        // TODO: Implement this test
    }

    @Test
    public void testOnTimeout() {
        // TODO: Implement this test
    }

    @Test
    public void testOnProgressUpdate() {
        // TODO: Implement this test
    }

    @Test
    public void testCancel() {
        // TODO: Implement this test
    }

    @Test
    public void testIsCancelled() {
        // TODO: Implement this test
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schemaName";

        ScheduleEntity scheduleEntity = testee.createScheduleEntity(connectionConfig, parameter, schema);

        ArgumentCaptor<ScheduleEntity> scheduleEntityArgumentCaptor = ArgumentCaptor.forClass(ScheduleEntity.class);
        verify(scheduleService, times(1)).create(scheduleEntityArgumentCaptor.capture());

        ScheduleEntity capturedScheduleEntity = scheduleEntityArgumentCaptor.getValue();
        assertEquals("ONLINE_SCHEMA_CHANGE_COMPLETE", capturedScheduleEntity.getJobType().getName());
        assertEquals(TaskStatus.ENABLED, capturedScheduleEntity.getStatus());
        assertEquals(false, capturedScheduleEntity.isAllowConcurrent());
        assertEquals(creatorId, capturedScheduleEntity.getCreatorId());
        assertEquals(organizationId, capturedScheduleEntity.getOrganizationId());
        assertEquals(1L, capturedScheduleEntity.getProjectId());
        assertEquals(1L, capturedScheduleEntity.getDatabaseId());
        assertEquals("schemaName", capturedScheduleEntity.getDatabaseName());
        assertEquals(JsonUtils.toJson(new TriggerConfig().setTriggerStrategy(TriggerStrategy.CRON).setCronExpression("0/10 * * * * ?")), capturedScheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, capturedScheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), capturedScheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // TODO: Implement this test
    }
}