package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
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
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult.Status;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeFlowableTaskTest {

    private static final long SCHEDULE_ID = 1L;
    private static final String SCHEDULE_NAME = "scheduleName";
    private static final long FLOW_INSTANCE_ID = 1L;
    private static final long TASK_ID = 1L;
    private static final long CREATOR_ID = 1L;
    private static final long ORGANIZATION_ID = 1L;
    private static final boolean CONTINUE_ON_ERROR = true;
    private static final double PERCENTAGE = 50.0;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private TaskService taskService;
    private OrganizationService organizationService;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        taskService = mock(TaskService.class);
        organizationService = mock(OrganizationService.class);

        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
    }

    @Test
    public void testStart_ValidParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setId(1L);
        parameters.setSchemaName("schemaName");

        when(scheduleService.create(any(ScheduleEntity.class)))
                .thenReturn(new ScheduleEntity());

        when(quartzJobService.createJob(any(JobType.class), anyString(), anyString(), any(Date.class), anyList()))
                .thenReturn(true);

        OnlineSchemaChangeContextHolder.set(CREATOR_ID, FLOW_INSTANCE_ID, ORGANIZATION_ID);

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(TASK_ID);
        when(execution.getVariable("onlineSchemaChangeParameter")).thenReturn(parameters);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);

        ProcessInstance processInstance = mock(ProcessInstance.class);
        when(taskService.detail(TASK_ID)).thenReturn(processInstance);

        onlineSchemaChangeFlowableTask.start(FLOW_INSTANCE_ID, taskService, execution);

        verify(taskService, times(1)).start(TASK_ID);
        verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
        verify(quartzJobService, times(1)).createJob(eq(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE), eq(SCHEDULE_NAME), anyString(),
                any(Date.class), anyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_NullExecution() throws Exception {
        OnlineSchemaChangeContextHolder.set(CREATOR_ID, FLOW_INSTANCE_ID, ORGANIZATION_ID);

        DelegateExecution execution = null;
        onlineSchemaChangeFlowableTask.start(FLOW_INSTANCE_ID, taskService, execution);
    }

    @Test
    public void testIsSuccessful_TaskDone() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);

        when(taskService.detail(TASK_ID)).thenReturn(taskEntity);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsSuccessful_TaskFailed() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        when(taskService.detail(TASK_ID)).thenReturn(taskEntity);

        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testOnFailure_ExceptionThrown() throws Exception {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        when(taskService.detail(TASK_ID)).thenThrow(new Exception());

        onlineSchemaChangeFlowableTask.onFailure(TASK_ID, taskService);
    }

    @Test
    public void testOnSuccessful_ExceptionThrown() throws Exception {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);

        when(taskService.detail(TASK_ID)).thenThrow(new Exception());

        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult();
        OnlineSchemaChangeTaskResult.Status status = OnlineSchemaChangeTaskResult.Status.COMPLETED;
        result.setStatus(status);

        when(taskService.detail(TASK_ID)).thenReturn(taskEntity);
        when(taskService.update(any(TaskEntity.class))).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.onSuccessful(TASK_ID, taskService);

        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout_ExceptionThrown() throws Exception {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.TIMEOUT);

        when(taskService.detail(TASK_ID)).thenThrow(new Exception());

        onlineSchemaChangeFlowableTask.onTimeout(TASK_ID, taskService);
    }

    @Test
    public void testOnProgressUpdate_Success() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setId(1L);
        parameters.setSchemaName("schemaName");

        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        subTaskParameter.setId(1L);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersList = ListUtils.emptyIfNull(
                new ArrayList<OnlineSchemaChangeScheduleTaskParameters>());
        subTaskParametersList.add(subTaskParameter);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(SCHEDULE_ID);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);

        Page<ScheduleTaskEntity> scheduleTasks = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), SCHEDULE_ID)).thenReturn(scheduleTasks);

        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
        when(quartzJobService.createJob(any(JobType.class), anyString(), anyString(), any(Date.class), anyList()))
                .thenReturn(true);

        OnlineSchemaChangeContextHolder.set(CREATOR_ID, FLOW_INSTANCE_ID, ORGANIZATION_ID);

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(TASK_ID);
        when(execution.getVariable("onlineSchemaChangeParameter")).thenReturn(parameters);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);

        ProcessInstance processInstance = mock(ProcessInstance.class);
        when(taskService.detail(TASK_ID)).thenReturn(processInstance);

        onlineSchemaChangeFlowableTask.start(FLOW_INSTANCE_ID, taskService, execution);

        onlineSchemaChangeFlowableTask.onProgressUpdate(TASK_ID, taskService);

        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCancel_InterruptRunningTask() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setId(1L);
        parameters.setSchemaName("schemaName");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(SCHEDULE_ID);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);

        ScheduleTaskEntity runningTask = new ScheduleTaskEntity();
        runningTask.setStatus(TaskStatus.RUNNING);

        List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
        scheduleTasks.add(runningTask);

        Page<ScheduleTaskEntity> page = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), SCHEDULE_ID)).thenReturn(page);
        when(page.getContent()).thenReturn(scheduleTasks);

        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
        when(quartzJobService.createJob(any(JobType.class), anyString(), anyString(), any(Date.class), anyList()))
                .thenReturn(true);

        OnlineSchemaChangeContextHolder.set(CREATOR_ID, FLOW_INSTANCE_ID, ORGANIZATION_ID);

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(TASK_ID);
        when(execution.getVariable("onlineSchemaChangeParameter")).thenReturn(parameters);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);

        ProcessInstance processInstance = mock(ProcessInstance.class);
        when(taskService.detail(TASK_ID)).thenReturn(processInstance);

        onlineSchemaChangeFlowableTask.start(FLOW_INSTANCE_ID, taskService, execution);

        onlineSchemaChangeFlowableTask.cancel(true, TASK_ID, taskService);

        verify(quartzJobService, times(1)).terminate(SCHEDULE_ID, runningTask.getId());
        verify(taskService, times(1)).cancel(TASK_ID);
    }

    @Test
    public void testIsCancelled_TaskCancelled() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.CANCELED);

        when(taskService.detail(TASK_ID)).thenReturn(taskEntity);

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity_ValidParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setId(1L);
        parameter.setSchemaName("schemaName");

        ScheduleEntity scheduleEntity = new ScheduleEntity();

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        doReturn(scheduleEntity).when(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, parameter,
                "schemaName");

        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "schemaName");

        assertEquals(scheduleEntity, result);
    }

    @Test
    public void testCreateScheduleTaskEntity_ValidParameters() {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        param.setId(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        doReturn(scheduleTaskEntity).when(onlineSchemaChangeFlowableTask)
                .createScheduleTaskEntity(SCHEDULE_ID, param);

        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(SCHEDULE_ID, param);

        assertEquals(scheduleTaskEntity, result);
    }
}