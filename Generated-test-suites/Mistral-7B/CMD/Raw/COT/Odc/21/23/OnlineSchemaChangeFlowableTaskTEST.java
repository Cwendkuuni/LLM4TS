package com.oceanbase.odc.service.onlineschemachange;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
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
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private TaskEntity taskEntity;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters parameter;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters scheduleParam;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartMethod() throws Exception {
        // Given
        String schemaName = RandomStringUtils.randomAlphanumeric(10);
        Mockito.when(delegateExecution.getVariable("schemaName")).thenReturn(schemaName);
        Mockito.when(delegateExecution.getVariable("parameters")).thenReturn(parameter);
        Mockito.when(delegateExecution.getVariable("connectionConfig")).thenReturn(connectionConfig);

        // When
        onlineSchemaChangeFlowableTask.start(null, null, delegateExecution);

        // Then
        Mockito.verify(scheduleService).create(scheduleEntity);
        Mockito.verify(scheduleTaskRepository).saveAndFlush(scheduleTaskEntity);
        Mockito.verify(taskHandler).start(scheduleId, scheduleTaskEntity.getId());
        Mockito.verify(organizationService).getCreator(anyLong());

        // Additional asserts
        assertNotNull(onlineSchemaChangeFlowableTask.getScheduleId());
        assertNotNull(onlineSchemaChangeFlowableTask.getCreatorId());
        assertNotNull(onlineSchemaChangeFlowableTask.getOrganizationId());
        assertNotNull(onlineSchemaChangeFlowableTask.getPercentage());
        assertNull(onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsSuccessfulMethod() {
        // Given
        String status = "DONE";
        Mockito.when(taskEntity.getStatus()).thenReturn(status);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsFailureMethod() {
        // Given
        String status = "FAILED";
        Mockito.when(taskEntity.getStatus()).thenReturn(status);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertTrue(result);
    }

    @Test
    public void testOnFailureMethod() {
        // Given
        Long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onFailure(taskId, null);

        // Then
        Mockito.verify(log).warn("Online schema change task failed, taskId={}", taskId);
    }

    @Test
    public void testOnSuccessfulMethod() {
        // Given
        Long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, null);

        // Then
        Mockito.verify(log).info("Online schema change task succeed, taskId={}", taskId);
    }

    @Test
    public void testOnTimeoutMethod() {
        // Given
        Long taskId = 1L;

        // When
        onlineSchemaChangeFlowableTask.onTimeout(taskId, null);

        // Then
        Mockito.verify(log).warn("Online schema change task timeout, taskId={}", taskId);
    }

    @Test
    public void testOnProgressUpdateMethod() throws Exception {
        // Given
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(scheduleTaskEntity);
        Page<ScheduleTaskEntity> taskPage = new PageImpl<>(tasks);
        Double singleTaskPercentage = 0.5;

        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(taskPage);
        Mockito.when(singleTaskPercentage(scheduleTaskEntity)).thenReturn(singleTaskPercentage);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, null);

        // Then
        Mockito.verify(taskService).update(flowTask);
        assertEquals(singleTaskPercentage * 100, onlineSchemaChangeFlowableTask.getPercentage());
    }

    @Test
    public void testCancelMethod() throws Exception {
        // Given
        boolean canceled = true;
        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(new PageImpl<>(Collections.emptyList()));

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, taskId, null);

        // Then
        Mockito.verify(taskService).cancel(taskId);
        assertTrue(result);
    }

    @Test
    public void testIsCancelledMethod() {
        // Given
        String status = "CANCELED";
        Mockito.when(taskEntity.getStatus()).thenReturn(status);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        assertTrue(result);
    }

    @Test
    public void testCreateScheduleEntityMethod() {
        // Given
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "test_schema";

        // When
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        // Then
        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(delegateExecution.getCreatorId(), scheduleEntity.getCreatorId());
        assertEquals(delegateExecution.getOrganizationId(), scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(delegateExecution.getCreatorId(), scheduleEntity.getModifierId());
    }

    @Test
    public void testCreateScheduleTaskEntityMethod() {
        // Given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters scheduleParam = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);

        // When
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, scheduleParam);

        // Then
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(scheduleId.toString(), scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(scheduleParam), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(scheduleParam)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }

    private Double singleTaskPercentage(ScheduleTaskEntity scheduleTaskEntity) {
        double percentage;
        switch (scheduleTaskEntity.getStatus()) {
            case PREPARING:
                percentage = 0;
                break;
            case RUNNING:
                percentage = scheduleTaskEntity.getProgressPercentage() / 100;
                break;
            default:
                percentage = 1;
        }
        return percentage;
    }
}