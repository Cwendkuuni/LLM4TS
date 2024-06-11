package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.service.flow.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegateTestUtil;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.iam.OrganizationService;

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
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private TaskEntity taskEntity;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private TaskService taskService;

    @Mock
    private User user;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    private static final Long SCHEDULE_ID = 1L;
    private static final Long ORGANIZATION_ID = 1L;
    private static final Long CREATOR_ID = 1L;
    private static final Long FLOW_TASK_ID = 1L;
    private static final Long SCHEDULE_TASK_ID = 1L;
    private static final String SCHEMA = "testSchema";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        when(organizationService.getCreator(anyLong())).thenReturn(user);
        when(connectionConfig.getId()).thenReturn(CREATOR_ID);
        when(delegateExecution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(delegateExecution.getVariable("onlineSchemaChangeParameters")).thenReturn(new OnlineSchemaChangeParameters());
        when(delegateExecution.getVariable("schemaName")).thenReturn(SCHEMA);
        when(delegateExecution.getVariable("continueOnError")).thenReturn(true);

        testee.start(FLOW_TASK_ID, taskService, delegateExecution);

        verify(taskService, times(1)).start(FLOW_TASK_ID);
        ArgumentCaptor<TaskStatus> taskStatusArgumentCaptor = ArgumentCaptor.forClass(TaskStatus.class);
        verify(taskService, times(1)).update(taskEntity.setStatus(taskStatusArgumentCaptor.capture()));
        TaskStatus actualStatus = taskStatusArgumentCaptor.getValue();
        assertEquals(TaskStatus.RUNNING, actualStatus);

        verify(quartzJobService, times(1)).terminate(anyLong(), anyLong());

        OnlineSchemaChangeParameters parameters = (OnlineSchemaChangeParameters) delegateExecution.getVariable("onlineSchemaChangeParameters");
        assertEquals(SCHEMA, parameters.getSchemaName());
        assertEquals(CONNECTOR_ID, parameters.getConnectionConfig().getId());
        assertEquals(CREATOR_ID, parameters.getCreatorId());
        assertEquals(ORGANIZATION_ID, parameters.getOrganizationId());
        assertEquals(true, parameters.isContinueOnError());
        verify(connectionConfig, times(1)).getId();
        verify(connectionConfig, times(1)).getName();

        OnlineSchemaChangeContextHolder.assertTraceEquals(CREATOR_ID, FLOW_TASK_ID, ORGANIZATION_ID);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(CREATOR_ID);
        scheduleEntity.setDatabaseName(SCHEMA);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(CREATOR_ID);
        scheduleEntity.setOrganizationId(ORGANIZATION_ID);
        // todo project id database id
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setModifierId(CREATOR_ID);
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameters));
        verify(scheduleService, times(1)).create(scheduleEntity);

        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = parameters.generateSubTaskParameters(connectionConfig, SCHEMA)
                .stream()
                .collect(Collectors.toList());

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        scheduleTaskEntity.setJobName(FLOW_TASK_ID.toString());
        scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleTaskEntity.setProgressPercentage(0.0);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(subTasks.get(0)));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(subTasks.get(0))));
        scheduleTaskEntity.setFireTime(new Date());

        verify(scheduleTaskRepository, times(1)).saveAndFlush(scheduleTaskEntity);

        OnlineSchemaChangeTaskResult taskResult = JsonUtils.parse(JsonUtils.toJson(new OnlineSchemaChangeTaskResult()),
                OnlineSchemaChangeTaskResult.class);
        assertEquals(List.of(subTasks.get(0)), taskResult.getScheduleTasks());
        assertEquals(TaskStatus.RUNNING, taskResult.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        testee.status = TaskStatus.DONE;
        assertTrue(testee.isSuccessful());

        testee.status = TaskStatus.FAILED;
        assertFalse(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        testee.status = TaskStatus.DONE;
        assertFalse(testee.isFailure());

        testee.status = TaskStatus.FAILED;
        assertTrue(testee.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        doThrow(new Exception()).when(taskHandler).start(SCHEDULE_ID, SCHEDULE_TASK_ID);
        testee.start(FLOW_TASK_ID, taskService, delegateExecution);

        verify(taskService, times(1)).cancel(FLOW_TASK_ID, true);
        ArgumentCaptor<String> errorMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(log, times(1)).warn(errorMessageArgumentCaptor.capture());
        String actualErrorMessage = errorMessageArgumentCaptor.getValue();
        assertTrue(actualErrorMessage.startsWith("Online schema change task failed, taskId="));
        assertEquals(FLOW_TASK_ID, actualErrorMessage.split(", ")[1].trim());
    }

    @Test
    public void testOnSuccessful() throws Exception {
        doReturn(taskEntity).when(taskService).detail(FLOW_TASK_ID);
        doAnswer(invocation -> {
            TaskStatus dbStatus = invocation.getArgument(0).getStatus();
            testee.status = dbStatus;
            return null;
        }).when(testee).updateFlowInstanceStatus(eq(FlowStatus.EXECUTION_SUCCEEDED));

        testee.onSuccessful(FLOW_TASK_ID, taskService);

        verify(taskService, times(1)).update(taskEntity.setStatus(eq(TaskStatus.DONE)));

        ArgumentCaptor<String> logMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(log, times(1)).info(logMessageArgumentCaptor.capture());
        String actualLogMessage = logMessageArgumentCaptor.getValue();
        assertTrue(actualLogMessage.startsWith("Online schema change task succeed, taskId="));
        assertEquals(FLOW_TASK_ID, actualLogMessage.split(", ")[1].trim());
    }

    @Test
    public void testOnTimeout() {
        testee.onTimeout(FLOW_TASK_ID, taskService);

        ArgumentCaptor<String> logMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(log, times(1)).warn(logMessageArgumentCaptor.capture());
        String actualLogMessage = logMessageArgumentCaptor.getValue();
        assertTrue(actualLogMessage.startsWith("Online schema change task timeout, taskId="));
        assertEquals(FLOW_TASK_ID, actualLogMessage.split(", ")[1].trim());
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        Page<ScheduleTaskEntity> tasks = BaseODCFlowTaskDelegateTestUtil.createScheduleTasksPage(3);

        testee.progressStatusUpdate(tasks);

        assertEquals(TaskStatus.FAILED, testee.status);

        testee.percentage = 50;
        testee.onProgressUpdate(FLOW_TASK_ID, taskService);

        verify(taskService, times(1)).update(taskEntity.setProgressPercentage(50));
        ArgumentCaptor<String> logMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(log, times(1)).info(logMessageArgumentCaptor.capture());
        String actualLogMessage = logMessageArgumentCaptor.getValue();
        assertTrue(actualLogMessage.startsWith("Progress update: taskId="));
        assertEquals(FLOW_TASK_ID, actualLogMessage.split(", ")[1].trim());
    }

    @Test
    public void testCancel() throws Exception {
        testee.status = TaskStatus.RUNNING;
        assertTrue(testee.cancel(true, FLOW_TASK_ID, taskService));

        ArgumentCaptor<String> errorMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(log, times(1)).warn(errorMessageArgumentCaptor.capture());
        String actualErrorMessage = errorMessageArgumentCaptor.getValue();
        assertTrue(actualErrorMessage.startsWith("Online schema change task canceled, taskId="));
        assertEquals(FLOW_TASK_ID, actualErrorMessage.split(", ")[1].trim());

        verify(taskService, times(1)).cancel(FLOW_TASK_ID, true);
    }

    @Test
    public void testIsCancelled() {
        testee.status = TaskStatus.CANCELED;
        assertTrue(testee.isCancelled());

        testee.status = TaskStatus.DONE;
        assertFalse(testee.isCancelled());
    }
}