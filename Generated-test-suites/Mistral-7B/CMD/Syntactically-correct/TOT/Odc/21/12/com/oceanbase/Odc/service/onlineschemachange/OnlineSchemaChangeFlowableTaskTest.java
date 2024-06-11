package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskStatus;

@SuppressWarnings("unchecked")
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
    private QuartzJobService quartzJobService;

    @Mock
    private TaskService taskService;

    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionConfig.id()).thenReturn(1L);
        when(organizationService.getOrganizationIdByUserId(any())).thenReturn(1L);
        when(scheduleService.create(any())).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any())).thenReturn(new ScheduleTaskEntity());
        when(taskService.detail(any())).thenReturn(new ExecutionEntity());
        when(taskService.start(any())).thenReturn(null);
    }

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        TaskEntity task = new TaskEntity();
        task.setId(1L);
        DelegateExecution execution = new DelegateExecution();
        execution.setVariables(Collections.singletonMap("parameter", parameter));

        when(quartzJobService.schedule(any(), any(), any()))
                .thenReturn(new ScheduleEntity());

        testee.start(1L, taskService, execution);

        verify(quartzJobService).schedule(any(), any(), any());
        verify(taskService).start(1L);
        assertEquals(TaskStatus.RUNNING, testee.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        testee.setStatus(TaskStatus.DONE);
        assertTrue(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        testee.setStatus(TaskStatus.FAILED);
        assertTrue(testee.isFailure());
    }

    @Test
    public void testIsCancelled() {
        testee.setStatus(TaskStatus.CANCELED);
        assertTrue(testee.isCancelled());
    }

    @Test
    public void testOnTimeout() {
        testee.onTimeout(1L, taskService);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());

        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);
        testee.progressStatusUpdate(page);

        assertEquals(TaskStatus.DONE, testee.getStatus());
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.RUNNING);
        scheduleTask.setProgressPercentage(50);

        double actual = testee.singleTaskPercentage(scheduleTask);
        assertEquals(0.5, actual, 0.01);
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        testee.createScheduleEntity(connectionConfig, parameter, "test_schema");

        verify(scheduleService).create(any());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        testee.createScheduleTaskEntity(1L, param);

        verify(scheduleTaskRepository).saveAndFlush(any());
    }

    @Test
    public void testCancel() {
        testee.cancel(false, 1L, taskService);

        verify(taskService).cancel(1L);
    }

    @Test
    public void testListScheduleTasks() {
        List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
        Pageable pageable = Pageable.unpaged();

        when(scheduleTaskRepository.list(pageable, 1L)).thenReturn(new PageImpl<>(scheduleTasks));

        List<OnlineSchemaChangeTaskStatus> actual = testee.listTasks(pageable);

        assertEquals(0, actual.size());
    }

    @Test
    public void testListTasks() {
        List<TaskEntity> tasks = new ArrayList<>();
        Pageable pageable = Pageable.unpaged();

        when(taskService.list(pageable)).thenReturn(new PageImpl<>(tasks));

        List<OnlineSchemaChangeTaskStatus> actual = testee.listTasks(pageable);

        assertEquals(0, actual.size());
    }

    @Test
    public void testGetTaskResult() {
        OnlineSchemaChangeTaskResult expected = new OnlineSchemaChangeTaskResult();

        when(taskService.detail(any())).thenReturn(new ExecutionEntity());
        when(taskService.getVariable(any(), "result")).thenReturn(expected);

        OnlineSchemaChangeTaskStatus actual = testee.getTaskResult(1L, taskService);

        assertEquals(expected, actual.getResult());
    }
}