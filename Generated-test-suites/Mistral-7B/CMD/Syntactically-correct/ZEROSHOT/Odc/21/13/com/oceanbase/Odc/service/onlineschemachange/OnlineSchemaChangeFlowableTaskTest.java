package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
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
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTaskTest;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerType;
import com.oceanbase.odc.service.task.TaskService;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

@ContextConfiguration(classes = OnlineSchemaChangeFlowableTaskTest.class)
@ActiveProfiles("test")
@SpringJUnitConfig(classes = {OnlineSchemaChangeFlowableTask.class, QuartzJobService.class,
        ScheduleService.class, ScheduleTaskService.class, OrganizationService.class, OscTaskCompleteHandler.class})
public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OrganizationService organizationService;

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        when(organizationService.getUserById(anyLong())).thenReturn(Mockito.mock(User.class));
        when(taskService.start(anyLong())).thenReturn(new ProcessInstance());
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User());
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameters);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);

        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        testInstance.start(anyLong(), taskService, execution);

        verify(taskService).start(anyLong());
        verify(organizationService).getUserById(task.getCreatorId());
        verify(taskService).detail(anyLong());
    }

    @Test
    public void testIsSuccessful() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        assertEquals(TaskStatus.RUNNING, testInstance.getStatus());
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
        verify(taskService).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        testInstance.onSuccessful(taskId, taskService);
        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        testInstance.onTimeout(taskId, taskService);
        // TODO: implement testOnTimeout
    }

    @Test
    public void testProgressUpdate() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());
        Page<ScheduleTaskEntity> page = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(page);
        when(page.getSize()).thenReturn(2);
        when(page.getContent()).thenReturn(tasks.toArray(new ScheduleTaskEntity[0]));
        when(singleTaskPercentage(tasks.get(0))).thenReturn(0.2);
        when(singleTaskPercentage(tasks.get(1))).thenReturn(0.8);

        testInstance.progressStatusUpdate(page);

        assertEquals(TaskStatus.DONE, testInstance.getStatus());
    }

    @Test
    public void testCancel() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();
        boolean result = testInstance.cancel(true, 1L, taskService);
        assertTrue(result);

        verify(scheduleTaskService).listTask(Pageable.unpaged(), anyLong());
        verify(taskHandler).terminate(anyLong(), anyLong());
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();

        ScheduleEntity result = testInstance.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, result.getJobType());
        assertEquals(ScheduleStatus.ENABLED, result.getStatus());
        assertEquals(creatorId, result.getCreatorId());
        assertEquals(organizationId, result.getOrganizationId());
        assertEquals(TriggerStrategy.CRON, result.getTriggerConfigJson().getTriggerStrategy());
        assertEquals(checkTaskCronExpression, result.getTriggerConfigJson().getCronExpression());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, result.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), result.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeFlowableTask testInstance = new OnlineSchemaChangeFlowableTask();

        ScheduleTaskEntity result = testInstance.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, result.getStatus());
        assertEquals(scheduleId.toString(), result.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), result.getJobGroup());
        assertEquals(0.0, result.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param), result.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), result.getResultJson());
        assertEquals(new Date(), result.getFireTime());
    }
}