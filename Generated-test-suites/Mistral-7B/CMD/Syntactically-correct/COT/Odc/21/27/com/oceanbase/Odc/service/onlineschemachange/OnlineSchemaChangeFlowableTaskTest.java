package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.RuntimeContext;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

/**
 * Test class for OnlineSchemaChangeFlowableTask.
 */
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleService scheduleService;
    private TaskService taskService;
    private QuartzJobService quartzJobService;
    private ConnectionConfig connectionConfig;
    private OrganizationService organizationService;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ScheduleEntity scheduleEntity;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleService = mock(ScheduleService.class);
        taskService = mock(TaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        connectionConfig = new ConnectionConfig();
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        scheduleEntity = new ScheduleEntity();
    }

    @Test
    public void testStart_success() throws Exception {
        // Given
        when(taskService.detail(Mockito.anyLong())).thenReturn(createTaskEntity());
        when(taskService.start(Mockito.anyLong())).thenReturn(Void.TYPE);
        when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(scheduleEntity);
        when(quartzJobService.scheduleJob(Mockito.anyString(), Mockito.any(TriggerConfig.class), Mockito.anyString()))
                .thenReturn(Void.TYPE);

        // When
        DelegateExecution execution = createDelegateExecution();
        OnlineSchemaChangeParameters onlineSchemaChangeParameter = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setConnectionConfig(connectionConfig);
        task.setOrganizationService(organizationService);
        task.setScheduleService(scheduleService);
        task.setQuartzJobService(quartzJobService);
        task.setTaskService(taskService);
        task.start(1L, taskService, execution);

        // Then
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TaskResult> resultCaptor = ArgumentCaptor.forClass(TaskResult.class);
        Mockito.verify(taskService).start(taskIdCaptor.capture(taskId));
        TaskResult result = Mockito.capture(resultCaptor.getValue());
        assertEquals(TaskStatus.RUNNING, result.getStatus());
        assertEquals(FlowStatus.EXECUTION_INITIATED, result.getFlowInstanceStatus());
        assertNull(result.getErrorMessage());
        assertEquals(1L, taskId.getValue());
    }

    @Test
    public void testStart_failure() throws Exception {
        // Given
        when(taskService.detail(Mockito.anyLong())).thenReturn(createTaskEntity());
        when(taskService.start(Mockito.anyLong())).thenThrow(new RuntimeException("Task start failed"));

        // When
        DelegateExecution execution = createDelegateExecution();
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setConnectionConfig(connectionConfig);
        task.setOrganizationService(organizationService);
        task.setScheduleService(scheduleService);
        task.setQuartzJobService(quartzJobService);
        task.setTaskService(taskService);
        task.start(1L, taskService, execution);

        // Then
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(taskService).start(taskIdCaptor.capture(taskId));
        assertEquals(1L, taskId.getValue());
        Mockito.verify(taskService, Mockito.never()).update(Mockito.any(TaskEntity.class));
        Mockito.verify(taskService, Mockito.never()).cancel(Mockito.anyLong(), Mockito.any(boolean.class), Mockito.any(TaskService.class));
    }

    @Test
    public void testOnTimeout() throws Exception {
        // Given
        TaskEntity taskEntity = createTaskEntity();

        // When
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.onTimeout(1L, taskService);

        // Then
        Mockito.verify(taskService, Mockito.never()).cancel(Mockito.anyLong(), Mockito.any(boolean.class), Mockito.any(TaskService.class));
    }

    @Test
    public void testIsSuccessful() throws Exception {
        // Given
        TaskEntity taskEntity = createTaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);

        // When
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        // Then
        boolean result = task.isSuccessful();
        assertFalse(result);

        taskEntity.setStatus(TaskStatus.DONE);
        result = task.isSuccessful();
        assertTrue(result);
    }

    @Test
    public void testIsFailure() throws Exception {
        // Given
        TaskEntity taskEntity = createTaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);

        // When
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        // Then
        boolean result = task.isFailure();
        assertFalse(result);

        taskEntity.setStatus(TaskStatus.FAILED);
        result = task.isFailure();
        assertTrue(result);
    }

    @Test
    public void testCancel_success() throws Exception {
        // Given
        when(taskService.cancel(Mockito.anyLong(), Mockito.any(boolean.class), Mockito.any(TaskService.class)))
                .thenReturn(true);

        // When
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        boolean result = task.cancel(true, 1L, taskService);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCancel_failure() throws Exception {
        // Given
        when(taskService.cancel(Mockito.anyLong(), Mockito.any(boolean.class), Mockito.any(TaskService.class)))
                .thenReturn(false);

        // When
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        boolean result = task.cancel(true, 1L, taskService);

        // Then
        assertFalse(result);
    }

    @Test
    public void testIsCancelled() throws Exception {
        // Given
        TaskEntity taskEntity = createTaskEntity();
        taskEntity.setStatus(TaskStatus.CANCELED);

        // When
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        // Then
        assertTrue(task.isCancelled());

        taskEntity.setStatus(TaskStatus.DONE);
        assertFalse(task.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        // Given
        String connectionId = RandomStringUtils.randomAlphanumeric(10);
        String schema = "test_schema";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(connectionId);

        // When
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        ScheduleEntity result = task.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        // Then
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, result.getJobType());
        assertEquals(ScheduleStatus.ENABLED, result.getStatus());
        assertEquals(connectionId, result.getConnectionId());
        assertEquals(schema, result.getDatabaseName());
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters params = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity result = task.createScheduleTaskEntity(scheduleId, params);

        // Then
        assertEquals(TaskStatus.PREPARING, result.getStatus());
    }

    private ProcessInstance createProcessInstance() {
        return mock(ProcessInstance.class);
    }

    private TaskEntity createTaskEntity() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setFlowInstanceId(1L);
        return taskEntity;
    }

    private DelegateExecution createDelegateExecution() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(RuntimeContext.getProcessInstance()).thenReturn(createProcessInstance());
        return execution;
    }
}