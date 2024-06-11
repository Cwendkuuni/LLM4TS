package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

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
    private OrganizationService organizationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(mock(ScheduleEntity.class));
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setId(1L);
        String schema = "test_schema";
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameter);
        when(execution.getVariable("schemaName")).thenReturn(schema);

        onlineSchemaChangeFlowableTask.start(1L, mock(TaskService.class), execution);

        ScheduleEntity scheduleEntity = (ScheduleEntity) when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(mock(ScheduleEntity.class)).getAllCalls().get(0).getArgument(0);
        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());

        ScheduleTaskEntity scheduleTaskEntity = (ScheduleTaskEntity) when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]).getAllCalls().get(0).getArgument(0);
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(scheduleId, scheduleTaskEntity.getScheduleId());
    }

    @Test
    public void testIsSuccessful() {
        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onFailure(taskId, mock(TaskService.class));
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_FAILED);
        verify(taskEntity).setStatus(TaskStatus.FAILED);
        verify(taskEntity).setEndTime(any(Date.class));
    }

    @Test
    public void testOnSuccessful() throws Exception {
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, mock(TaskService.class));
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
        verify(taskEntity).setStatus(TaskStatus.DONE);
        verify(taskEntity).setEndTime(any(Date.class));
        verify(taskEntity).setResultJson(anyString());
    }

    @Test
    public void testOnTimeout() throws Exception {
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onTimeout(taskId, mock(TaskService.class));
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        verify(taskEntity).setStatus(TaskStatus.TIMED_OUT);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        long taskId = 1L;
        List<ScheduleTaskEntity> tasks = Arrays.asList(mock(ScheduleTaskEntity.class), mock(ScheduleTaskEntity.class));
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(tasks));

        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        testObject.start(taskId, mock(TaskService.class), mock(DelegateExecution.class));
        testObject.onProgressUpdate(taskId, mock(TaskService.class));

        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(Arrays.asList(mock(ScheduleTaskEntity.class), mock(ScheduleTaskEntity.class)));
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(onlineSchemaChangeTaskResult));
        assertEquals(onlineSchemaChangeTaskResult.getTasks(), testObject.getTasks());
    }

    @Test
    public void testCancel() throws Exception {
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, mock(TaskService.class));
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskService.detail(1L)).thenReturn(taskEntity);
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        testObject.start(1L, mock(TaskService.class), mock(DelegateExecution.class));
        verify(taskEntity).setStatus(TaskStatus.CANCELED);
        verify(taskEntity).setEndTime(any(Date.class));
        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask testObject = new OnlineSchemaChangeFlowableTask();
        testObject.setStatus(TaskStatus.CANCELED);
        assertTrue(testObject.isCancelled());
    }
}