package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.identity.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
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
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskEntityImpl;

public class OnlineSchemaChangeFlowableTaskTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OnlineSchemaChangeTaskHandler taskHandler;
    private OscTaskCompleteHandler completeHandler;
    private OrganizationService organizationService;
    private BaseODCFlowTaskDelegate<Void> testee;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        taskHandler = mock(OnlineSchemaChangeTaskHandler.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        organizationService = mock(OrganizationService.class);
        testee = new OnlineSchemaChangeFlowableTask();
        testee.setOrganizationService(organizationService);
        testee.setScheduleService(scheduleService);
        testee.setScheduleTaskRepository(scheduleTaskRepository);
        testee.setScheduleTaskService(scheduleTaskService);
        testee.setQuartzJobService(quartzJobService);
        testee.setTaskHandler(taskHandler);
        testee.setCompleteHandler(completeHandler);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        User creator = new User();
        creator.setId(1L);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getVariables()).thenReturn(null);
        when(execution.getProcessInstanceId()).thenReturn(1L);
        when(execution.getId()).thenReturn(1L);
        when(execution.getBusinessKey()).thenReturn("businessKey");
        when(execution.getProcessDefinitionId()).thenReturn(1L);
        when(execution.getProcessDefinitionKey()).thenReturn("processKey");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskParameters scheduleParameters = new OnlineSchemaChangeScheduleTaskParameters();
        parameter.setGenerateSubTaskParameters(List.of(scheduleParameters));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setDatabaseName(schema);

        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(arg -> arg.getArguments()[0]);

        // When
        testee.start(1L, mock(TaskService.class), execution);

        // Then
        ArgumentCaptor<OnlineSchemaChangeScheduleTaskParameters> scheduleTaskParameterCaptor = ArgumentCaptor
                .forClass(OnlineSchemaChangeScheduleTaskParameters.class);
        when(scheduleService.create(scheduleEntity)).thenAnnotateFieldsWith(arg -> {
            arg.setTriggerConfig(new TriggerConfig());
            arg.getTriggerConfig().setTriggerStrategy(TriggerStrategy.CRON);
            arg.getTriggerConfig().setCronExpression("0/10 * * * * ?");
            arg.getTriggerConfig().setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
            return arg;
        });
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleEntity.getId())).thenReturn(
                Page.empty());
        testee.createScheduleTaskEntity(scheduleEntity.getId(), scheduleTaskParameterCaptor.capture());
        OnlineSchemaChangeScheduleTaskParameters scheduleTaskParameters = scheduleTaskParameterCaptor
                .getValue();
        TestUtil.verifyJsonEquals(JsonUtils.toJson(parameter), JsonUtils.toJson(scheduleTaskParameters
                .getParametersJson()));
        verify(taskHandler).start(scheduleEntity.getId(), scheduleTaskParameters.getId());
        verify(quartzJobService).scheduleJob(eq(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE), eq(scheduleEntity.getId()),
                any(Date.class));
        assertEquals(TaskStatus.RUNNING, testee.getStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWithThrowableInCreateScheduleTaskEntity() throws Exception {
        // Given
        User creator = new User();
        creator.setId(1L);
        DelegateExecution execution = mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskParameters scheduleParameters = new OnlineSchemaChangeScheduleTaskParameters();
        parameter.setGenerateSubTaskParameters(List.of(scheduleParameters));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setDatabaseName(schema);

        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        doThrow(new RuntimeException("Test exception")).when(scheduleTaskRepository)
                .saveAndFlush(any(ScheduleTaskEntity.class));

        // When
        testee.start(1L, mock(TaskService.class), execution);

        // Then
        verify(taskHandler, never()).start(scheduleEntity.getId(), anyInt());
        verify(quartzJobService, never()).scheduleJob(any(JobType.class), anyLong(), any(Date.class));
        assertEquals(TaskStatus.FAILED, testee.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        // Given
        testee.setStatus(TaskStatus.DONE);

        // When
        boolean result = testee.isSuccessful();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsFailure() {
        // Given
        testee.setStatus(TaskStatus.FAILED);

        // When
        boolean result = testee.isFailure();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsCancelled() {
        // Given
        testee.setStatus(TaskStatus.CANCELED);

        // When
        boolean result = testee.isCancelled();

        // Then
        assertTrue(result);
    }

    @Test
    public void testCancel() throws Exception {
        // Given
        testee.setStatus(TaskStatus.RUNNING);
        testee.setScheduleId(1L);
        ScheduleTaskEntity runningEntity = mock(ScheduleTaskEntity.class);
        when(runningEntity.getId()).thenReturn(1L);
        when(runningEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskRepository.listTask(Pageable.unpaged(), testee.getScheduleId()))
                .thenReturn(Page.of(List.of(runningEntity)));

        // When
        testee.cancel(false, 1L, mock(TaskService.class));

        // Then
        verify(taskHandler).terminate(testee.getScheduleId(), runningEntity.getId());
        assertEquals(TaskStatus.CANCELED, testee.getStatus());
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        // Given
        OnlineSchemaChangeFlowableTask testee = new OnlineSchemaChangeFlowableTask();
        testee.setStatus(TaskStatus.RUNNING);
        testee.setScheduleId(1L);
        List<ScheduleTaskEntity> tasks = List.of(new ScheduleTaskEntity(), new ScheduleTaskEntity());
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);

        // When
        testee.onProgressUpdate(1L, mock(TaskService.class));

        // Then
        double percentage = testee.getPercentage();
        assertEquals(100.0 / tasks.size(), percentage, 0.01);
    }

}