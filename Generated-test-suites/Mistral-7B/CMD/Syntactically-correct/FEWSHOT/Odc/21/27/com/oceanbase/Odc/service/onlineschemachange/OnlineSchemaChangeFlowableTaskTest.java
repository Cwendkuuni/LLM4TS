package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.cli.ParseException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private TaskService taskService;

    @Mock
    private FlowTaskUtil flowTaskUtil;

    @Mock
    private OnlineSchemaChangeParameters parameters;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        when(flowTaskUtil.getOnlineSchemaChangeParameter(any())).thenReturn(parameters);
        when(flowTaskUtil.getConnectionConfig(any())).thenReturn(connectionConfig);
        when(flowTaskUtil.getSchemaName(any())).thenReturn("schema");
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schema");

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(FlowStatus.EXECUTION_SUCCEEDED, scheduleEntity.getStatus());
        assertNotNull(scheduleEntity.getTriggerConfigJson());
        assertNotNull(scheduleEntity.getJobParametersJson());

        TriggerConfig triggerConfig = JsonUtils.parse(scheduleEntity.getTriggerConfigJson(), TriggerConfig.class);
        assertEquals(TriggerStrategy.CRON, triggerConfig.getTriggerStrategy());
        assertEquals(checkTaskCronExpression, triggerConfig.getCronExpression());

        OnlineSchemaChangeParameters jsonParameters = JsonUtils.parse(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class);
        assertEquals("schema", jsonParameters.getSchema());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("OnlineSchemaChangeFlowableTask-1", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals("", scheduleTaskEntity.getResultJson());

        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = JsonUtils.parse(scheduleTaskEntity.getParametersJson(), OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(onlineSchemaChangeScheduleTaskParameters, onlineSchemaChangeScheduleTaskResult.getParameters());
    }

    @Test
    public void testStart() throws ParseException, Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariables()).thenReturn(mock(Map.class));
        when(taskService.start(1L)).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                Long taskId = (Long) args[0];
                return null;
            }
        });

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.RUNNING);

        when(taskService.detail(1L)).thenReturn(taskEntity);

        when(flowTaskUtil.getTaskCreator(execution)).thenReturn(new User(1L, "testUser", null));
        when(organizationService.getOrganizationId(any())).thenReturn(1L);

        doNothing().when(taskHandler).start(1L, 1L);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
        onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setFlowTaskUtil(flowTaskUtil);

        OnlineSchemaChangeContextHolder.setTraceId(1L);
        OnlineSchemaChangeContextHolder.setFlowTaskId(1L);
        OnlineSchemaChangeContextHolder.setOrganizationId(1L);

        long flowTaskId = 1L;
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        String schema = "schema";
        Boolean continueOnError = true;

        OnlineSchemaChangeEntity scheduleEntity = new OnlineSchemaChangeEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setConnectionId(connectionConfig.getId());
        scheduleEntity.setDatabaseName(schema);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(creatorId);
        scheduleEntity.setOrganizationId(organizationId);
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setModifierId(scheduleEntity.getCreatorId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression(checkTaskCronExpression);
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));

        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        List<ScheduleTaskEntity> scheduleTasks = subTasks.stream()
                .map(param -> onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param))
                .collect(Collectors.toList());

        when(scheduleTaskRepository.saveAll(anyList())).thenReturn(scheduleTasks);

        OnlineSchemaChangeFlowableTask testOnlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        testOnlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        testOnlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        testOnlineSchemaChangeFlowableTask.setFlowTaskUtil(flowTaskUtil);
        testOnlineSchemaChangeFlowableTask.setTaskService(taskService);

        OnlineSchemaChangeContextHolder.clear();

        testOnlineSchemaChangeFlowableTask.start(flowTaskId, taskService, execution);

        verify(taskService, times(1)).start(1L);
        verify(taskHandler, times(1)).start(1L, 1L);
        verify(flowTaskUtil, times(1)).getCloudMainAccountId(execution);
        verify(flowTaskUtil, times(1)).getOnlineSchemaChangeParameter(execution);
        verify(flowTaskUtil, times(1)).getConnectionConfig(execution);
        verify(flowTaskUtil, times(1)).getSchemaName(execution);
        verify(organizationService, times(1)).getOrganizationId(any());
        verify(scheduleService, times(1)).create(scheduleEntity);
        verify(scheduleTaskRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));

        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testList() throws Exception {
        Pageable pageable = PageRequest.unpaged();
        Page<ScheduleTaskEntity> scheduleTasks = new PageImpl<>(new ArrayList<>());
        when(scheduleTaskService.listTask(pageable, 1L)).thenReturn(scheduleTasks);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        List<ScheduleTaskEntity> result = onlineSchemaChangeFlowableTask.list(pageable, 1L);

        assertEquals(result, scheduleTasks.getContent());
    }

    @Test
    public void testListWithTasks() throws Exception {
        Pageable pageable = PageRequest.unpaged();
        Page<ScheduleTaskEntity> scheduleTasks = new PageImpl<>(Arrays.asList(mock(ScheduleTaskEntity.class), mock(ScheduleTaskEntity.class)));
        when(scheduleTaskService.listTask(pageable, 1L)).thenReturn(scheduleTasks);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        List<ScheduleTaskEntity> result = onlineSchemaChangeFlowableTask.list(pageable, 1L);

        assertEquals(2, result.size());
    }

    @Test
    public void testListWithEmptyTasks() throws Exception {
        Pageable pageable = PageRequest.unpaged();
        Page<ScheduleTaskEntity> scheduleTasks = new PageImpl<>(Collections.emptyList());
        when(scheduleTaskService.listTask(pageable, 1L)).thenReturn(scheduleTasks);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        List<ScheduleTaskEntity> result = onlineSchemaChangeFlowableTask.list(pageable, 1L);

        assertEquals(0, result.size());
    }
}