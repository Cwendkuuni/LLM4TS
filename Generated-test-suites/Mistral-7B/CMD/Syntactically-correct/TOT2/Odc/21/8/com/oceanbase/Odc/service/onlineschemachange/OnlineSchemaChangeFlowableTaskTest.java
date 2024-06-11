package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
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
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
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
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * Unit test cases for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 * @date 2023-05-23
 */
@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ScheduleTaskResult scheduleTaskResult;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method start()
     */
    @Test
    public void testStart() throws Exception {
        // Given
        when(connectionConfig.id()).thenReturn(1L);
        when(organizationService.getCurrentUser()).thenReturn(new User());
        when(flowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(onlineSchemaChangeParameters);
        when(flowTaskUtil.getSchemaName(delegateExecution)).thenReturn("schemaName");
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(Page.empty());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);
        when(quartzJobService.scheduleJob("OnlineSchemaChangeTaskHandler", scheduleId, 0)).thenReturn(true);

        // When
        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.trace(creatorId, taskId, organizationId);
        TaskStatus actualStatus = onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);

        // Then
        verify(taskService).start(taskId);
        verify(quartzJobService).scheduleJob("OnlineSchemaChangeTaskHandler", scheduleId, 0);
        assertEquals(TaskStatus.RUNNING, actualStatus);
        OnlineSchemaChangeContextHolder.clear();
    }

    /**
     * Test method isSuccessful()
     */
    @Test
    public void testIsSuccessful() {
        // Given
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());

        // When
        boolean actualResult = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertTrue(actualResult);
    }

    /**
     * Test method isFailure()
     */
    @Test
    public void testIsFailure() {
        // Given
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());
        when(taskService.detail(taskId).getStatus()).thenReturn(TaskStatus.FAILED);

        // When
        boolean actualResult = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertTrue(actualResult);
    }

    /**
     * Test method onFailure()
     */
    @Test
    public void testOnFailure() throws Exception {
        // Given
        when(taskService.detail(taskId)).thenThrow(new RuntimeException());

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // Then
        verify(log).warn("Online schema change task failed, taskId={}", taskId);
    }

    /**
     * Test method onSuccessful()
     */
    @Test
    public void testOnSuccessful() throws Exception {
        // Given
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());
        when(taskService.detail(taskId).getStatus()).thenReturn(TaskStatus.EXECUTION_SUCCEEDED);

        // When
        OnlineSchemaChangeTaskResult actualResult = (OnlineSchemaChangeTaskResult) onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Then
        assertNotNull(actualResult);
        verify(taskService).update(taskService.detail(taskId));
    }

    /**
     * Test method onTimeout()
     */
    @Test
    public void testOnTimeout() throws Exception {
        // Given

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // Then
        verify(log).warn("Online schema change task timeout, taskId={}", taskId);
    }

    /**
     * Test method onProgressUpdate()
     */
    @Test
    public void testOnProgressUpdate() throws Exception {
        // Given
        Page<ScheduleTaskEntity> tasks = Page.of(scheduleTaskEntity, scheduleTaskEntity, scheduleTaskEntity);
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(tasks);
        when(singleTaskPercentage(scheduleTaskEntity)).thenReturn(0.3);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        // Then
        verify(taskService).update(taskService.detail(taskId));
    }

    /**
     * Test method cancel()
     */
    @Test
    public void testCancel() throws Exception {
        // Given
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(Page.of(scheduleTaskEntity));

        // When
        boolean actualResult = onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

        // Then
        verify(scheduleTaskService).terminate(scheduleId, scheduleTaskEntity.getId());
        verify(taskService).cancel(taskId);
        assertTrue(actualResult);
    }

    /**
     * Test method isCancelled()
     */
    @Test
    public void testIsCancelled() throws Exception {
        // Given
        when(taskService.detail(taskId)).thenReturn(new TaskEntity());
        when(taskService.detail(taskId).getStatus()).thenReturn(TaskStatus.CANCELED);

        // When
        boolean actualResult = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        assertTrue(actualResult);
    }

    /**
     * Test method createScheduleEntity()
     */
    @Test
    public void testCreateScheduleEntity() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "schemaName";

        // When
        ScheduleEntity actualScheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                onlineSchemaChangeParameters, schema);

        // Then
        assertNotNull(actualScheduleEntity);
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, actualScheduleEntity.getJobType());
        assertEquals(scheduleEntity.getCreatorId(), actualScheduleEntity.getCreatorId());
        assertEquals(scheduleEntity.getOrganizationId(), actualScheduleEntity.getOrganizationId());
        assertEquals(1L, actualScheduleEntity.getProjectId());
        assertEquals(1L, actualScheduleEntity.getDatabaseId());
        assertEquals(FlowStatus.EXECUTION_SUCCEEDED, actualScheduleEntity.getStatus());
        assertEquals(false, actualScheduleEntity.isAllowConcurrent());
    }

    /**
     * Test method createScheduleTaskEntity()
     */
    @Test
    public void testCreateScheduleTaskEntity() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ScheduleTaskEntity actualScheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId,
                onlineSchemaChangeScheduleTaskParameters);

        // Then
        assertNotNull(actualScheduleTaskEntity);
        assertEquals(TaskStatus.PREPARING, actualScheduleTaskEntity.getStatus());
        assertEquals("OnlineSchemaChangeTaskHandler", actualScheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), actualScheduleTaskEntity.getJobGroup());
        assertEquals(0.0, actualScheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters), actualScheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)),
                actualScheduleTaskEntity.getResultJson());
        assertEquals(new Date(), actualScheduleTaskEntity.getFireTime());
    }
}