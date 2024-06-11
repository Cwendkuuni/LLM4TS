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
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionVariableEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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

public class OnlineSchemaChangeFlowableTaskTest {

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private OrganizationService organizationService;

    @MockBean
    private QuartzJobService quartzJobService;

    @MockBean
    private ScheduleTaskService scheduleTaskService;

    @MockBean
    private TaskService taskService;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId("connectionId");
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(false);
        OnlineSchemaChangeScheduleTaskParameters scheduleParameters = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleParameters.setDatabaseName("databaseName");
        User creator = new User();
        creator.setId(1L);
        creator.setOrganizationId(1L);

        when(scheduleTaskRepository.saveAndFlush(ArgMatchers.any(ScheduleTaskEntity.class)))
                .thenReturn(new ScheduleTaskEntity());

        when(scheduleService.create(ArgMatchers.any(ScheduleEntity.class)))
                .thenReturn(new ScheduleEntity());

        when(organizationService.findById(1L)).thenReturn(creator);
    }

    @Test
    public void testSuccessStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, param, "databaseName");
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(List.of());
        when(quartzJobService.trigger(ArgMatchers.anyString(), ArgMatchers.anyLong()))
                .thenReturn(true);
        when(taskService.update(ArgMatchers.any(TaskEntity.class)))
                .then(invocation -> {
                    TaskEntity taskEntity = invocation.getArgument(0);
                    taskEntity.setStatus(TaskStatus.DONE);
                    taskEntity.setResultJson(JsonUtils.toJson(result));
                    return null;
                });

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.isSuccessful());
        assertEquals(1L, scheduleId);
    }

    @Test
    public void testFailedStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, param, "databaseName");
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(List.of());
        when(quartzJobService.trigger(ArgMatchers.anyString(), ArgMatchers.anyLong()))
                .thenReturn(false);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.isFailure());
        assertEquals(1L, scheduleId);
    }

    @Test
    public void testFailedOnTimeout() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, param, "databaseName");
        when(quartzJobService.trigger(ArgMatchers.anyString(), ArgMatchers.anyLong()))
                .thenReturn(true);
        when(taskService.update(ArgMatchers.any(TaskEntity.class)))
                .then(invocation -> {
                    TaskEntity taskEntity = invocation.getArgument(0);
                    taskEntity.setStatus(TaskStatus.TIMEOUT);
                    return null;
                });

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        assertEquals(TaskStatus.TIMEOUT, onlineSchemaChangeFlowableTask.isFailure());
        assertEquals(1L, scheduleId);
    }

    @Test
    public void testFailedOnError() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, param, "databaseName");
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(List.of());
        when(quartzJobService.trigger(ArgMatchers.anyString(), ArgMatchers.anyLong()))
                .thenReturn(true);
        when(taskService.update(ArgMatchers.any(TaskEntity.class)))
                .then(invocation -> {
                    TaskEntity taskEntity = invocation.getArgument(0);
                    taskEntity.setStatus(TaskStatus.FAILED);
                    return null;
                });

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.isFailure());
        assertEquals(1L, scheduleId);
    }

    @Test
    public void testCancel() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);

        when(taskService.cancel(ArgMatchers.anyLong())).thenReturn(true);

        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.isCancelled());
        assertEquals(1L, scheduleId);
    }

    @Test
    public void testIsCancelled() throws Exception {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        assertEquals(false, onlineSchemaChangeFlowableTask.isCancelled());
    }
}