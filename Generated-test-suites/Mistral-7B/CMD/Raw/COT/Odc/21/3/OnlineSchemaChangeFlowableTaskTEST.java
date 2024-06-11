package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
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
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskInput;

/**
 * Test class for OnlineSchemaChangeFlowableTask.
 */
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
    private OscTaskCompleteHandler oscTaskCompleteHandler;
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        reset(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService, organizationService, oscTaskCompleteHandler);
    }

    @Test
    public void testStart_whenCalled_shouldStartTaskAndScheduleJob() {
        User creator = createUser();
        ConnectionConfig connectionConfig = createConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        TaskInput taskInput = new TaskInput();
        taskInput.setProcessInstanceId(1L);
        taskInput.setTaskDefinitionKey("OnlineSchemaChangeTask");
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getBusinessKey()).thenReturn("businessKey");
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariables()).thenReturn(Map.of("parameter", parameter));
        when(organizationService.findById(creator.getId())).thenReturn(creator);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(quartzJobService.scheduleJob(anyString(), anyString(), any(TriggerConfig.class), anyLong(), anyString())).thenReturn(1L);

        onlineSchemaChangeFlowableTask.start(1L, null, execution);

        verify(taskService, times(1)).start(1L);
    }

    @Test
    public void testIsSuccessful_whenTaskIsDone_shouldReturnTrue() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure_whenTaskIsFailed_shouldReturnTrue() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure_whenTaskIsFailed_shouldLogWarning() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

        onlineSchemaChangeFlowableTask.onFailure(1L, mock(TaskService.class));

        verify(log, times(1)).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful_whenTaskIsDone_shouldLogInfo() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        onlineSchemaChangeFlowableTask.onSuccessful(1L, mock(TaskService.class));

        verify(log, times(1)).info("Online schema change task succeed, taskId={}", 1L);
    }

    @Test
    public void testOnTimeout_whenTaskIsTimedOut_shouldLogWarning() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        onlineSchemaChangeFlowableTask.onTimeout(1L, mock(TaskService.class));

        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testOnProgressUpdate_whenTaskProgressIsUpdated_shouldUpdateFlowInstanceStatusAndLogInfo() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        when(taskService.detail(anyLong())).thenAnswer(invocation -> {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setStatus(TaskStatus.RUNNING);
            taskEntity.setResultJson("");
            taskEntity.setProgressPercentage(0);
            return taskEntity;
        });

        when(taskService.update(any(TaskEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        verify(log, times(1)).info("List schedule task size is 0 by scheduleId {}.", 1L);
        verify(taskService, times(1)).detail(1L);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCancel_whenCalled_shouldCancelTaskAndScheduleJob() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        when(scheduleTaskRepository.listTask(any(Pageable.class), anyLong())).thenReturn(Page.empty());
        when(taskService.cancel(anyLong(), Boolean.FALSE, any(TaskService.class))).thenReturn(true);
        when(quartzJobService.deleteJob(anyLong())).thenReturn(true);

        assertTrue(onlineSchemaChangeFlowableTask.cancel(Boolean.FALSE, 1L, mock(TaskService.class)));
    }

    @Test
    public void testIsCancelled_whenTaskIsCancelled_shouldReturnTrue() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity_whenCalled_shouldCreateScheduleEntityWithValidValues() {
        ConnectionConfig connectionConfig = createConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(FlowStatus.EXECUTION_PENDING, scheduleEntity.getStatus());
        assertEquals(creator.getId(), scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals(JsonUtils.toJson(createTriggerConfig()), scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity_whenCalled_shouldCreateScheduleTaskEntityWithValidValues() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("OnlineSchemaChangeTask", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertEquals(1L, scheduleTaskEntity.getFireTime().getTime());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(organizationId);
        return user;
    }

    private ConnectionConfig createConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        return connectionConfig;
    }

    private TriggerConfig createTriggerConfig() {
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression(checkTaskCronExpression);
        return triggerConfig;
    }

    private static final long organizationId = 1L;
}