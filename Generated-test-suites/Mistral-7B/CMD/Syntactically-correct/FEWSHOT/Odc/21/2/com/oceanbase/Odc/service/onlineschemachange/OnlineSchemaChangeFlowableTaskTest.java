package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.cli.util.TestUtil;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
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
import com.oceanbase.odc.service.task.model.TaskResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;
    private TaskEntity taskEntity;
    private TaskService taskService;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        connectionConfig = TestUtil.createConnectionConfig();
        onlineSchemaChangeParameters = TestUtil.createOnlineSchemaChangeParameters();
        scheduleEntity = TestUtil.createScheduleEntity();
        scheduleTaskEntity = TestUtil.createScheduleTaskEntity();
        taskEntity = TestUtil.createTaskEntity();
        taskService = Mockito.mock(TaskService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        scheduleTaskService = Mockito.mock(ScheduleTaskService.class);
        quartzJobService = Mockito.mock(QuartzJobService.class);
        organizationService = Mockito.mock(OrganizationService.class);
        oscTaskCompleteHandler = Mockito.mock(OscTaskCompleteHandler.class);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTest.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTest.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTest.setOscTaskCompleteHandler(oscTaskCompleteHandler);
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(taskService.start(anyLong())).thenReturn(taskEntity);
        when(execution.getId()).thenReturn(1L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(TestUtil.createUser());
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("testSchema");
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("testUid");
        doNothing().when(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, onlineSchemaChangeParameters,
                "testSchema");
        doNothing().when(onlineSchemaChangeFlowableTest).createScheduleTaskEntity(scheduleEntity.getId(),
                TestUtil.createOnlineSchemaChangeScheduleTaskParameters());
        onlineSchemaChangeFlowableTest.start(1L, taskService, execution);
        verify(taskService, times(1)).start(1L);
        verify(quartzJobService, times(1)).start(scheduleEntity.getId(), scheduleTaskEntity.getId());
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTest.getStatus());
        assertNull(onlineSchemaChangeFlowableTest.getScheduleId());
        assertEquals(1L, onlineSchemaChangeFlowableTest.getCreatorId());
        assertEquals(1L, onlineSchemaChangeFlowableTest.getOrganizationId());
        assertEquals(false, onlineSchemaChangeFlowableTest.isContinueOnError());
        assertEquals("testSchema", onlineSchemaChangeFlowableTest.getSchemaName());
        OnlineSchemaChangeContextHolder.assertTrace(1L, 1L, 1L);
    }

    @Test
    public void testIsSuccessful() {
        assertTrue(onlineSchemaChangeFlowableTest.isSuccessful());
        assertFalse(onlineSchemaChangeFlowableTest.isFailure());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTest.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTest.isFailure());
        assertFalse(onlineSchemaChangeFlowableTest.isSuccessful());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        onlineSchemaChangeFlowableTest.onFailure(taskId, taskService);
        verify(taskService, times(1)).cancel(taskId, false);
        verify(log, times(1)).warn("Online schema change task failed, taskId={}", taskId);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskResult result = new TaskResult();
        result.setStatus(TaskStatus.DONE);
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(result));
        onlineSchemaChangeFlowableTest.onSuccessful(taskId, taskService);
        verify(taskService, times(1)).update(taskEntity);
        verify(log, times(1)).info("Online schema change task succeed, taskId={}", taskId);
        verify(scheduleService, times(1)).update(scheduleEntity, ScheduleStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        onlineSchemaChangeFlowableTest.onTimeout(taskId, taskService);
        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        Page<ScheduleTaskEntity> tasks = TestUtil.createScheduleTaskEntities(2).stream().map(scheduleTaskEntity -> {
            scheduleTaskEntity.setId(scheduleTaskEntity.getId() + 1);
            return scheduleTaskEntity;
        }).collect(Collectors.toList());
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleEntity.getId())).thenReturn(Page.of(tasks));
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(tasks.get(0).getContent());
        when(JsonUtils.toJson(tasks.get(0).getResultJson())).thenReturn(JsonUtils.toJson(onlineSchemaChangeTaskResult));
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult2 = new OnlineSchemaChangeTaskResult(tasks.get(1).getContent());
        when(JsonUtils.toJson(tasks.get(1).getResultJson())).thenReturn(JsonUtils.toJson(onlineSchemaChangeTaskResult2));
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        onlineSchemaChangeFlowableTest.onProgressUpdate(taskId, taskService);
        verify(taskService, times(1)).update(taskEntity);
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTest.getStatus());
        assertEquals(1L, onlineSchemaChangeFlowableTest.getScheduleId());
        assertEquals(1L, onlineSchemaChangeFlowableTest.getCreatorId());
        assertEquals(1L, onlineSchemaChangeFlowableTest.getOrganizationId());
        assertEquals(false, onlineSchemaChangeFlowableTest.isContinueOnError());
        assertEquals(0.0, onlineSchemaChangeFlowableTest.getPercentage());
        OnlineSchemaChangeContextHolder.assertTrace(1L, 1L, 1L);
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;
        boolean result = onlineSchemaChangeFlowableTest.cancel(true, taskId, taskService);
        verify(taskService, times(1)).cancel(taskId, false);
        assertTrue(result);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTest = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTest.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTest.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        OnlineSchemaChangeEntity onlineSchemaChangeEntity = TestUtil.createOnlineSchemaChangeEntity();
        when(connectionConfig.getId()).thenReturn(1L);
        when(onlineSchemaChangeParameters.getConnectionConfig()).thenReturn(connectionConfig);
        when(onlineSchemaChangeParameters.getSchemaName()).thenReturn("testSchema");
        when(organizationService.getCurrentUser()).thenReturn(TestUtil.createUser());
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        ScheduleEntity result = testObject.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "testSchema");
        assertEquals(scheduleEntity, result);
        verify(scheduleService, times(1)).create(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        when(scheduleRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);
        OnlineSchemaChangeFlowableTest testObject = new OnlineSchemaChangeFlowableTest();
        ScheduleTaskEntity result = testObject.createScheduleTaskEntity(scheduleEntity.getId(),
                TestUtil.createOnlineSchemaChangeScheduleTaskParameters());
        assertEquals(scheduleTaskEntity, result);
        verify(scheduleRepository, times(1)).saveAndFlush(scheduleTaskEntity);
    }
}