package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
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
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.FlowTaskResult;

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
    private TaskService taskService;
    @Mock
    private OscTaskCompleteHandler taskHandler;

    private BaseODCFlowTaskDelegate<Void> testee;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = mock(OnlineSchemaChangeParameters.class);
        scheduleEntity = mock(ScheduleEntity.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        onlineSchemaChangeScheduleTaskParameters = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        testee = new OnlineSchemaChangeFlowableTask();
        testee.setConnectionConfig(connectionConfig);
        testee.setOrganizationService(organizationService);
        testee.setScheduleService(scheduleService);
        testee.setScheduleTaskRepository(scheduleTaskRepository);
        testee.setScheduleTaskService(scheduleTaskService);
        testee.setQuartzJobService(quartzJobService);
        testee.setTaskHandler(taskHandler);
        testee.setTaskService(taskService);
    }

    @Test
    public void testStart() throws Exception {
        when(scheduleTaskRepository.saveAndFlush(ArgumentMatchers.<ScheduleTaskEntity> any())).thenReturn(scheduleTaskEntity);

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS")).thenReturn(onlineSchemaChangeParameters);
        when(execution.getVariable("SCHEDULE_ID")).thenReturn(1L);
        when(execution.getVariable("CREATOR_ID")).thenReturn(1L);
        when(execution.getVariable("ORGANIZATION_ID")).thenReturn(1L);
        when(execution.getVariable("CONTINUE_ON_ERROR")).thenReturn(false);

        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskService.detail(1L)).thenReturn(taskEntity);

        testee.start(1L, taskService, execution);

        verify(taskService).start(1L);
        verify(quartzJobService).start(scheduleId, 1L);
        verify(organizationService).getOrganizationId(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(1L));
        verify(onlineSchemaChangeParameters).generateSubTaskParameters(connectionConfig, "db_name");
        verify(scheduleTaskRepository, Mockito.times(1)).saveAndFlush(ArgumentMatchers.<ScheduleTaskEntity> any());
    }

    @Test
    public void testIsSuccessful() {
        testee.status = TaskStatus.DONE;
        boolean result = testee.isSuccessful();
        assertEquals(true, result);
    }

    @Test
    public void testIsFailure() {
        testee.status = TaskStatus.FAILED;
        boolean result = testee.isFailure();
        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        testee.status = TaskStatus.CANCELED;
        boolean result = testee.isCancelled();
        assertEquals(true, result);
    }

    @Test
    public void testCancel() {
        when(scheduleTaskRepository.listTask(ArgumentMatchers.<Pageable> any(), ArgumentMatchers.<Long> eq(1L)))
                .thenReturn(Page.empty());
        when(scheduleTaskService.terminate(1L, 1L)).thenReturn(true);

        testee.cancel(true, 1L, taskService);

        verify(scheduleTaskService).terminate(1L, 1L);
        verify(taskService).cancel(1L);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        ProcessInstance processInstance = mock(ProcessInstance.class);
        when(taskService.detail(1L)).thenReturn(taskEntity);
        when(taskService.update(taskEntity)).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(new OnlineSchemaChangeTaskResult(null)));

        testee.onSuccessful(1L, taskService);

        verify(taskService).update(taskEntity);
        verify(quartzJobService).deleteJob(ArgumentMatchers.<Long> eq(1L), ArgumentMatchers.<String> eq("ONLINE_SCHEMA_CHANGE_COMPLETE-1"));
    }

    @Test
    public void testOnFailure() throws Exception {
        ProcessInstance processInstance = mock(ProcessInstance.class);
        when(taskService.detail(1L)).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

        testee.onFailure(1L, taskService);

        verify(taskService).update(taskEntity);
        verify(quartzJobService).deleteJob(ArgumentMatchers.<Long> eq(1L), ArgumentMatchers.<String> eq("ONLINE_SCHEMA_CHANGE_COMPLETE-1"));
    }

    @Test
    public void testOnTimeout() {
        testee.onTimeout(1L, taskService);

        verify(quartzJobService).deleteJob(ArgumentMatchers.<Long> eq(1L), ArgumentMatchers.<String> eq("ONLINE_SCHEMA_CHANGE_COMPLETE-1"));
    }

    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(scheduleTaskService.listTask(ArgumentMatchers.<Pageable> any(), ArgumentMatchers.<Long> eq(1L))).thenReturn(tasks);
        when(tasks.getSize()).thenReturn(2);
        when(tasks.getContent()).thenReturn(ListUtils.emptyIfNull(ListOrderedMap. orderedList(List.of(scheduleTaskEntity, scheduleTaskEntity))));

        testee.onProgressUpdate(1L, taskService);

        verify(taskService).update(taskEntity);
    }

    @Test
    public void testCreateScheduleEntity() {
        when(organizationService.getOrganizationId(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(1L))).thenReturn(1L);

        ScheduleEntity scheduleEntity = testee.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "db_name");

        verify(scheduleService).create(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        when(onlineSchemaChangeScheduleTaskParameters.getParametersJson()).thenReturn(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));

        ScheduleTaskEntity scheduleTaskEntity = testee.createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters);

        verify(scheduleTaskRepository).saveAndFlush(scheduleTaskEntity);
    }
}