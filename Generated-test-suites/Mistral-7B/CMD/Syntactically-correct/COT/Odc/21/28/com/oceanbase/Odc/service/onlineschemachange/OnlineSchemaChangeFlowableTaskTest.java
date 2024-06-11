package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

/**
 * Test class for OnlineSchemaChangeFlowableTask.
 *
 * @author yaobin
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<OnlineSchemaChangeScheduleTaskParameters> onlineSchemaChangeScheduleTaskParametersCaptor;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private User user;
    private DelegateExecution delegateExecution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = mock(OnlineSchemaChangeParameters.class);
        user = mock(User.class);
        delegateExecution = mock(DelegateExecution.class);
        when(delegateExecution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(delegateExecution.getVariable("onlineSchemaChangeParameters")).thenReturn(onlineSchemaChangeParameters);
        when(delegateExecution.getVariable("user")).thenReturn(user);
        when(organizationService.findById(user.getOrganizationId())).thenReturn(user.getOrganization());
        Context.setProcessEngineConfiguration(mock(org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration.class));
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(mock(Page.class));
    }

    @Test
    public void testStart_shouldStartOnlineSchemaChangeTaskAndScheduleTasks() {
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(mock(Page.class));
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(mock(ScheduleEntity.class));

        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        onlineSchemaChangeScheduleTaskParameters.setUid("uid");
        onlineSchemaChangeScheduleTaskParameters.setGitHubUrl("gitHubUrl");
        onlineSchemaChangeScheduleTaskParameters.setBranchName("branchName");
        onlineSchemaChangeScheduleTaskParameters.setDatabaseName("databaseName");
        onlineSchemaChangeScheduleTaskParameters.setContinueOnError(true);
        List<OnlineSchemaChangeScheduleTaskParameters> onlineSchemaChangeScheduleTaskParametersList = new ArrayList<>();
        onlineSchemaChangeScheduleTaskParametersList.add(onlineSchemaChangeScheduleTaskParameters);

        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "databaseName")).thenReturn(onlineSchemaChangeScheduleTaskParametersList);

        onlineSchemaChangeFlowableTask.start(1L, mock(TaskService.class), delegateExecution);

        OnlineSchemaChangeTaskResult expectedOnlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(onlineSchemaChangeScheduleTaskParametersList.toArray(new OnlineSchemaChangeScheduleTaskParameters[0]));
        verify(quartzJobService).scheduleJob("uid", "JobKey", TaskStatus.RUNNING, expectedOnlineSchemaChangeTaskResult, any(TriggerConfig.class));
    }

    @Test(expected = RuntimeException.class)
    public void testStart_shouldThrowExceptionWhenCreateScheduleEntityFails() {
        when(scheduleService.create(any(ScheduleEntity.class))).thenThrow(new RuntimeException());

        onlineSchemaChangeFlowableTask.start(1L, mock(TaskService.class), delegateExecution);
    }

    @Test
    public void testIsSuccessful_shouldReturnTrueWhenStatusIsDone() {
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = mock(OnlineSchemaChangeTaskResult.class);
        TaskEntity taskEntity = mock(TaskEntity.class);
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setResultJson(JsonUtils.toJson(onlineSchemaChangeTaskResult));

        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful(1L, mock(TaskService.class)));
    }

    @Test
    public void testIsSuccessful_shouldReturnFalseWhenStatusIsFailed() {
        TaskEntity taskEntity = mock(TaskEntity.class);
        taskEntity.setStatus(TaskStatus.FAILED);

        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful(1L, mock(TaskService.class)));
    }

    @Test
    public void testIsFailure_shouldReturnTrueWhenStatusIsFailed() {
        TaskEntity taskEntity = mock(TaskEntity.class);
        taskEntity.setStatus(TaskStatus.FAILED);

        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        assertTrue(onlineSchemaChangeFlowableTask.isFailure(1L, mock(TaskService.class)));
    }

    @Test
    public void testIsFailure_shouldReturnFalseWhenStatusIsNotFailed() {
        TaskEntity taskEntity = mock(TaskEntity.class);
        taskEntity.setStatus(TaskStatus.RUNNING);

        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        assertFalse(onlineSchemaChangeFlowableTask.isFailure(1L, mock(TaskService.class)));
    }

    @Test
    public void testCancel_shouldCancelRunningTask() {
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(mock(Page.class));
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(mock(ScheduleTaskEntity.class));

        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        onlineSchemaChangeScheduleTaskParameters.setUid("uid");
        onlineSchemaChangeScheduleTaskParametersList.add(onlineSchemaChangeScheduleTaskParameters);

        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "databaseName")).thenReturn(onlineSchemaChangeScheduleTaskParametersList);

        onlineSchemaChangeFlowableTask.start(1L, mock(TaskService.class), delegateExecution);

        verify(quartzJobService).scheduleJob("uid", "JobKey", TaskStatus.RUNNING, any(OnlineSchemaChangeTaskResult.class), any(TriggerConfig.class));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, mock(TaskService.class));

        verify(quartzJobService).terminateJob("uid", "JobKey");
    }

    @Test
    public void testCancel_shouldDoNothingWhenNoRunningTask() {
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(mock(Page.class));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, mock(TaskService.class));
    }

    @Test
    public void testGetStatus_shouldReturnCurrentStatus() {
        TaskEntity taskEntity = mock(TaskEntity.class);
        taskEntity.setStatus(TaskStatus.RUNNING);

        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus(1L, mock(TaskService.class)));
    }

    @Test
    public void testGetStatus_shouldReturnCancelledWhenCancelled() {
        TaskEntity taskEntity = mock(TaskEntity.class);
        taskEntity.setStatus(TaskStatus.CANCELED);

        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus(1L, mock(TaskService.class)));
    }

}