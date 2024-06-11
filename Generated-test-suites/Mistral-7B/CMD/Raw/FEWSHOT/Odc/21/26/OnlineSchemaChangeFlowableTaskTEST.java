package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

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
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private BaseODCFlowTaskDelegate<Void> onlineSchemaChangeFlowableTask;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Before
    public void setUp() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        onlineSchemaChangeScheduleTaskParameters = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);

        when(organizationService.getById(anyLong())).thenReturn(Mockito.mock(User.class));

        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(quartzJobService.scheduleJob(anyString(), any(JobType.class), anyLong(), any(Date.class), anyBoolean()))
                .thenReturn(new String());
        when(oscTaskCompleteHandler.start(anyLong(), anyLong())).thenReturn(new Void());
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schemaName");

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setOnlineSchemaChangeTaskHandler(oscTaskCompleteHandler);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);

        onlineSchemaChangeFlowableTask.start(1L, null, execution);

        verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
        verify(quartzJobService, times(1)).scheduleJob("OnlineSchemaChangeJob-1", JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, 1L, new Date(), false);
        verify(oscTaskCompleteHandler, times(1)).start(1L, 1L);
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        onlineSchemaChangeFlowableTask.onFailure(1L, null);

        verify(log, times(1)).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        onlineSchemaChangeFlowableTask.onSuccessful(1L, null);

        verify(log, times(1)).info("Online schema change task succeed, taskId={}", 1L);
    }

    @Test
    public void testOnTimeout() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        onlineSchemaChangeFlowableTask.onTimeout(1L, null);

        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        verify(onlineSchemaChangeFlowableTask, times(1)).setStatus(TaskStatus.DONE);
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schemaName");

        assertEquals("schemaName", scheduleEntity.getDatabaseName());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
    }

}