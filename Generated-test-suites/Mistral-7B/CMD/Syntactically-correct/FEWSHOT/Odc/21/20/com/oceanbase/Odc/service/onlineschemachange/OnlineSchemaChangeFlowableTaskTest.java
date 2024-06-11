package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
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
import com.oceanbase.odc.metadb.task.TaskService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.JobBuilder;
import com.oceanbase.odc.service.quartz.QuartzJobBuilder;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.Trigger;
import com.oceanbase.odc.service.quartz.TriggerBuilder;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.ScheduleEntity;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private TaskService mockTaskService;
    private ScheduleTaskService mockScheduleTaskService;
    private ScheduleService mockScheduleService;
    private QuartzJobService mockQuartzJobService;
    private OrganizationService mockOrganizationService;
    private ScheduleTaskRepository mockScheduleTaskRepository;
    private OscTaskCompleteHandler mockOscTaskCompleteHandler;

    @Before
    public void setUp() {
        mockTaskService = mock(TaskService.class);
        mockScheduleTaskService = mock(ScheduleTaskService.class);
        mockScheduleService = mock(ScheduleService.class);
        mockQuartzJobService = mock(QuartzJobService.class);
        mockOrganizationService = mock(OrganizationService.class);
        mockScheduleTaskRepository = mock(ScheduleTaskRepository.class);
        mockOscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setTaskService(mockTaskService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(mockScheduleTaskService);
        onlineSchemaChangeFlowableTask.setScheduleService(mockScheduleService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(mockQuartzJobService);
        onlineSchemaChangeFlowableTask.setOrganizationService(mockOrganizationService);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(mockScheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(mockOscTaskCompleteHandler);

        this.onlineSchemaChangeFlowableTask = onlineSchemaChangeFlowableTask;
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(FlowStatus.EXECUTION_SUCCEEDED, scheduleEntity.getStatus());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertNotNull(scheduleEntity.getTriggerConfigJson());
        assertNotNull(scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(1.0, scheduleTaskEntity.getProgressPercentage());
    }

    @Test
    public void testCancel() {
        boolean canceled = onlineSchemaChangeFlowableTask.cancel(true, 1L, mockTaskService);

        assertTrue(canceled);
        verify(mockTaskService, times(1)).cancel(1L);
    }

    @Test
    public void testIsCancelled() {
        TaskStatus taskStatus = TaskStatus.CANCELED;

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS")).thenReturn(new OnlineSchemaChangeParameters());
        when(execution.getVariable("CONNECTION_CONFIG")).thenReturn(new ConnectionConfig());
        when(execution.getVariable("SCHEMA_NAME")).thenReturn("testSchema");
        when(execution.getVariable("ONLINE_SCHEMA_CHANGE_TASK_HANDLER_CLASS")).thenReturn(OnlineSchemaChangeTaskHandler.class);
        when(mockTaskService.start(taskId)).thenReturn(true);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setTaskService(taskService);

        OnlineSchemaChangeContextHolder.set(new OnlineSchemaChangeContext(1L, 1L, 1L));

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        ConnectionConfig connectionConfig = new ConnectionConfig();

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        verify(mockTaskService, times(1)).start(taskId);
        verifyZeroInteractions(mockScheduleTaskService, mockScheduleService, mockQuartzJobService, mockOrganizationService, mockScheduleTaskRepository, mockOscTaskCompleteHandler);
        verify(execution, times(1)).setVariable("SCHEDULE_ID", anyLong());
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);

        when(mockScheduleTaskService.listTask(Mockito.any(Pageable.class), Mockito.eq(scheduleId))).thenReturn(tasks);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setTaskService(taskService);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(taskService, times(1)).detail(taskId);
        verify(tasks, times(1)).stream();
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus taskStatus = TaskStatus.DONE;

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus taskStatus = TaskStatus.FAILED;

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testGetConnectionConfig() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getVariable("CONNECTION_CONFIG")).thenReturn(new ConnectionConfig());

        ConnectionConfig connectionConfig = onlineSchemaChangeFlowableTask.getConnectionConfig(execution);

        assertEquals(new ConnectionConfig(), connectionConfig);
    }

    @Test
    public void testGetOnlineSchemaChangeParameters() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getVariable("ONLINE_SCHEMA_CHANGE_PARAMETERS")).thenReturn(new OnlineSchemaChangeParameters());

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = onlineSchemaChangeFlowableTask.getOnlineSchemaChangeParameters(execution);

        assertEquals(new OnlineSchemaChangeParameters(), onlineSchemaChangeParameters);
    }

    @Test
    public void testGetSchemaName() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getVariable("SCHEMA_NAME")).thenReturn("testSchema");

        String schemaName = onlineSchemaChangeFlowableTask.getSchemaName(execution);

        assertEquals("testSchema", schemaName);
    }

    @Test
    public void testGetOnlineSchemaChangeTaskHandlerClass() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getVariable("ONLINE_SCHEMA_CHANGE_TASK_HANDLER_CLASS")).thenReturn(OnlineSchemaChangeTaskHandler.class);

        Class<?> onlineSchemaChangeTaskHandlerClass = onlineSchemaChangeFlowableTask.getOnlineSchemaChangeTaskHandlerClass(execution);

        assertEquals(OnlineSchemaChangeTaskHandler.class, onlineSchemaChangeTaskHandlerClass);
    }

    @Test
    public void testGetFlowStatus() {
        TaskStatus taskStatus = TaskStatus.EXECUTING;

        assertEquals(FlowStatus.EXECUTION_IN_PROGRESS, onlineSchemaChangeFlowableTask.getFlowStatus(taskStatus));
    }

    @Test
    public void testGetTaskStatus() {
        TaskStatus taskStatus = TaskStatus.EXECUTING;

        assertEquals(taskStatus, onlineSchemaChangeFlowableTask.getTaskStatus(taskStatus));
    }

    @Test
    public void testGetPercentage() {
        double percentage = 50.0;

        assertEquals(percentage, onlineSchemaChangeFlowableTask.getPercentage(percentage), 0.0);
    }

    @Test
    public void testGetResultJson() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(Arrays.asList(new ScheduleTaskEntity()));

        when(onlineSchemaChangeParameters.getTaskHandlerResult()).thenReturn(onlineSchemaChangeTaskResult);

        String resultJson = onlineSchemaChangeFlowableTask.getResultJson(onlineSchemaChangeParameters);

        assertEquals(JsonUtils.toJson(onlineSchemaChangeTaskResult), resultJson);
    }

    @Test
    public void testGetFlowInstanceStatus() {
        FlowStatus flowStatus = FlowStatus.EXECUTION_SUCCEEDED;

        assertEquals(flowStatus, onlineSchemaChangeFlowableTask.getFlowInstanceStatus(TaskStatus.DONE));
    }

    @Test
    public void testGetFlowInstanceStatus_Failed() {
        FlowStatus flowStatus = FlowStatus.EXECUTION_FAILED;

        assertEquals(flowStatus, onlineSchemaChangeFlowableTask.getFlowInstanceStatus(TaskStatus.FAILED));
    }
}