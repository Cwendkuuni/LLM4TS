package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
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
import com.oceanbase.odc.metadb.task.TaskService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskExecution;
import com.oceanbase.odc.service.task.TaskServiceImpl;

public class OnlineSchemaChangeFlowableTaskTest {
    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters parameter;

    @Mock
    private ScheduleEntity schedule;

    @Mock
    private ScheduleTaskEntity scheduleTask;

    @Mock
    private OrganizationService organizationService;

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
    private FlowTaskUtil flowTaskUtil;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testSubject;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for createScheduleEntity method
    @Test
    public void testCreateScheduleEntity() {
        when(connectionConfig.id()).thenReturn(1L);
        when(parameter.getDatabaseName()).thenReturn("testDb");
        when(scheduleService.create(scheduleEntity)).thenReturn(schedule);

        OnlineSchemaChangeParameters testParameter = new OnlineSchemaChangeParameters();
        testParameter.setDatabaseName("testDb");

        ScheduleEntity createdSchedule = testSubject.createScheduleEntity(connectionConfig, testParameter, "testDb");
        assertEquals(1L, createdSchedule.getConnectionId());
        assertEquals("testDb", createdSchedule.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, createdSchedule.getJobType());
        assertEquals(ScheduleStatus.ENABLED, createdSchedule.getStatus());
        assertEquals(false, createdSchedule.isAllowConcurrent());
        assertEquals(creatorId, createdSchedule.getCreatorId());
        assertEquals(organizationId, createdSchedule.getOrganizationId());
        assertEquals(1L, createdSchedule.getProjectId());
        assertEquals(1L, createdSchedule.getDatabaseId());
        assertEquals(creatorId, createdSchedule.getModifierId());
        assertEquals(JsonUtils.toJson(new TriggerConfig()), createdSchedule.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, createdSchedule.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(testParameter), createdSchedule.getJobParametersJson());
    }

    // Test for createScheduleTaskEntity method
    @Test
    public void testCreateScheduleTaskEntity() {
        when(scheduleRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);

        OnlineSchemaChangeScheduleTaskParameters testParam = new OnlineSchemaChangeScheduleTaskParameters();
        testParam.setDatabaseName("testDb");

        ScheduleTaskEntity createdScheduleTask = testSubject.createScheduleTaskEntity(scheduleId, testParam);
        assertEquals(TaskStatus.PREPARING, createdScheduleTask.getStatus());
        assertEquals("testDb", createdScheduleTask.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), createdScheduleTask.getJobGroup());
        assertEquals(0.0, createdScheduleTask.getProgressPercentage(), 0.01);
        assertEquals(JsonUtils.toJson(testParam), createdScheduleTask.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(testParam)), createdScheduleTask.getResultJson());
        assertEquals(new Date(), createdScheduleTask.getFireTime());
    }

    // Test for start method
    @Test
    public void testStart() throws Exception {
        when(flowTaskUtil.getCloudMainAccountId(execution)).thenReturn("testUid");
        when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(flowTaskUtil.getSchemaName(execution)).thenReturn("testDb");

        doNothing().when(taskHandler).start(scheduleId, scheduleTaskEntity.getId());

        List<ScheduleTaskEntity> tasks = parameter.generateSubTaskParameters(connectionConfig, "testDb").stream()
                .map(param -> createScheduleTaskEntity(1L, param)).collect(Collectors.toList());

        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(Page.empty());

        testSubject.start(1L, new TaskServiceImpl(), execution);

        verify(taskHandler, times(1)).start(scheduleId, tasks.get(0).getId());
        verify(taskService, times(1)).start(1L);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    // Test for isSuccessful method
    @Test
    public void testIsSuccessful() {
        TaskStatus expectedStatus = TaskStatus.DONE;
        when(execution.getVariable("status")).thenReturn(expectedStatus);

        assertTrue(testSubject.isSuccessful(execution));
    }

    // Test for isFailure method
    @Test
    public void testIsFailure() {
        TaskStatus expectedStatus = TaskStatus.FAILED;
        when(execution.getVariable("status")).thenReturn(expectedStatus);

        assertTrue(testSubject.isFailure(execution));
    }

    // Test for onFailure method
    @Test
    public void testOnFailure() throws Exception {
        Long taskId = 1L;
        when(execution.getId()).thenReturn(taskId);
        when(taskService.cancel(taskId)).thenThrow(new RuntimeException("Test Exception"));

        testSubject.onFailure(taskId, taskService);
        verify(log, times(1)).warn("Online schema change task failed, taskId={}", taskId);
    }

    // Test for onSuccessful method
    @Test
    public void testOnSuccessful() throws Exception {
        TaskStatus expectedStatus = TaskStatus.DONE;
        when(execution.getVariable("status")).thenReturn(expectedStatus);
        when(flowTaskUtil.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED)).thenReturn(true);

        testSubject.onSuccessful(taskId, taskService);
        verify(log, times(1)).info("Online schema change task succeed, taskId={}", taskId);
        verify(flowTaskUtil, times(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    // Test for onTimeout method
    @Test
    public void testOnTimeout() {
        testSubject.onTimeout(taskId, taskService);
        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", taskId);
    }

    // Test for onProgressUpdate method
    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(tasks);

        testSubject.onProgressUpdate(taskId, taskService);
        verify(tasks, times(1)).stream();
        verify(tasks, times(1)).map(this::singleTaskPercentage);
        verify(tasks, times(1)).collect(Collectors.toList());
        verify(taskService, times(1)).update(flowTask);
    }

    private double singleTaskPercentage(ScheduleTaskEntity scheduleTask) {
        // Test implementation for singleTaskPercentage method
        return 0.5;
    }

    private TaskEntity flowTask = new ExecutionEntity();
}