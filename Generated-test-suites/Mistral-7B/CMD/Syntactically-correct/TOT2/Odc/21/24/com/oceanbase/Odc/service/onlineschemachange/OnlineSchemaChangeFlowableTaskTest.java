package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

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
import com.oceanbase.odc.service.iam.model.User;
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
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.Class)
public class OnlineSchemaChangeFlowableTaskTest {

    @MockBean
    private QuartzJobService quartzJobService;

    @MockBean
    private OrganizationService organizationService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private ScheduleTaskService scheduleTaskService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private OscTaskCompleteHandler taskHandler;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private User creator;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameter;

    @Before
    public void setup() {
        creator = Mockito.mock(User.class);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);

        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        testInstance.setTaskService(taskService);

        Mockito.when(execution.getProcessInstanceId()).thenReturn(processInstance.getId());
        Mockito.when(execution.getVariables()).thenReturn(Collections.singletonMap("parameter", parameter));
        Mockito.when(execution.getVariables().get("parameter")).thenReturn(parameter);
        Mockito.when(execution.getVariables().get("connectionConfig")).thenReturn(connectionConfig);
        Mockito.when(execution.getVariables().get("connectionConfig")).thenReturn(connectionConfig);

        Mockito.when(organizationService.getCreator(Mockito.anyLong())).thenReturn(creator);
        Mockito.when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("uid");
        Mockito.when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schemaName");
        Mockito.when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        Mockito.when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);

        Mockito.when(quartzJobService.createJob(Mockito.any(ScheduleEntity.class), Mockito.any(JavaDelegate.class)))
                .thenReturn(new Object());

        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.trace(creator.getId(), processInstance.getId(), creator.getOrganizationId());

        testInstance.start(processInstance.getId(), taskService, execution);

        OnlineSchemaChangeContextHolder.clear();

        // Check if the correct values have been set
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertNotNull(onlineSchemaChangeFlowableTask.getScheduleId());
        assertNotNull(onlineSchemaChangeFlowableTask.getCreatorId());
        assertNotNull(onlineSchemaChangeFlowableTask.getOrganizationId());
        assertEquals("uid", onlineSchemaChangeFlowableTask.getUid());
        assertEquals("schemaName", onlineSchemaChangeFlowableTask.getSchema());
        assertEquals(false, onlineSchemaChangeFlowableTask.isContinueOnError());

        Mockito.verify(quartzJobService, Mockito.times(1)).createJob(Mockito.any(ScheduleEntity.class),
                Mockito.any(JavaDelegate.class));
    }

    @Test
    public void testIsSuccessful() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        testInstance.setStatus(TaskStatus.DONE);

        assertTrue(testInstance.isSuccessful());
    }

    @Test
    public void testIsFailure() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        testInstance.setStatus(TaskStatus.FAILED);

        assertTrue(testInstance.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        testInstance.onFailure(taskId, taskService);

        Mockito.verify(log, Mockito.times(1)).warn("Online schema change task failed, taskId={}", taskId);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        testInstance.onSuccessful(taskId, taskService);

        Mockito.verify(log, Mockito.times(1)).info("Online schema change task succeed, taskId={}", taskId);
        Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        testInstance.onTimeout(taskId, taskService);

        Mockito.verify(log, Mockito.times(1)).warn("Online schema change task timeout, taskId={}", taskId);
    }

    @Test
    public void testProgressUpdate() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();

        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        Mockito.when(scheduleTaskService.listTask(Mockito.any(Pageable.class), Mockito.anyLong())).thenReturn(tasks);

        testInstance.progressStatusUpdate(tasks);

        Mockito.verify(tasks, Mockito.times(1)).stream();
        Mockito.verify(tasks, Mockito.times(1)).map(Mockito.any(ScheduleTaskEntity.class));
        Mockito.verify(tasks, Mockito.times(1)).collect(Mockito.eq(Collectors.toList()));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testSingleTaskPercentage() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        Mockito.when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);

        double percentage = testInstance.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(0, percentage, 0.01);

        Mockito.when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        double progressPercentage = 50.0;
        Mockito.when(scheduleTaskEntity.getProgressPercentage()).thenReturn(progressPercentage);

        percentage = testInstance.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(progressPercentage / 100, percentage, 0.01);

        Mockito.when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        percentage = testInstance.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(1, percentage, 0.01);

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testCancel() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        boolean mayInterruptIfRunning = true;
        Long taskId = 1L;

        testInstance.cancel(mayInterruptIfRunning, taskId, taskService);

        Mockito.verify(log, Mockito.times(1)).warn("Online schema change task cancelled, taskId={}", taskId);
        Mockito.verify(taskService, Mockito.times(1)).cancel(taskId);
    }

    @Test
    public void testIsCancelled() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        testInstance.setStatus(TaskStatus.CANCELED);

        assertTrue(testInstance.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId("connectionConfigId");

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setGenerateSubTaskParameters(Collections.emptyList());

        String schema = "schemaName";

        ScheduleEntity scheduleEntity = testInstance.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(false, scheduleEntity.isAllowConcurrent());
        assertEquals(creator.getId(), scheduleEntity.getCreatorId());
        assertEquals(creator.getOrganizationId(), scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals("connectionConfigId", scheduleEntity.getConnectionId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(FlowStatus.EXECUTION_PENDING, scheduleEntity.getFlowStatus());
        assertEquals(creator.getId(), scheduleEntity.getModifierId());
        assertNotNull(scheduleEntity.getTriggerConfigJson());
        assertNotNull(scheduleEntity.getMisfireStrategy());
        assertNotNull(scheduleEntity.getJobParametersJson());

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();

        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = testInstance.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.01);
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());

        OnlineSchemaChangeContextHolder.clear();
    }

}