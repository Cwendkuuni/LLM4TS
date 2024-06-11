package com.oceanbase.odc.service.onlineschemachange;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
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
import com.oceanbase.odc.service.task.model.TaskResult;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 */
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

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Captor
    private ArgumentCaptor<ScheduleEntity> scheduleEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<ScheduleTaskEntity> scheduleTaskEntityArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
        onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
    }

    @Test
    public void testStart_WhenCalled_ShouldCreateScheduleEntityAndStartTaskHandler() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        OnlineSchemaChangeContextHolder.setCreatorId(1L);
        OnlineSchemaChangeContextHolder.setOrganizationId(1L);
        OnlineSchemaChangeContextHolder.setFlowTaskId(1L);

        when(organizationService.getCurrentUser()).thenReturn(new User());
        when(taskHandler.generateSubTaskParameters(connectionConfig, schema)).thenReturn(new ArrayList<>());

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
        onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);

        onlineSchemaChangeFlowableTask.start(1L, null, execution);

        verify(scheduleService).create(scheduleEntityArgumentCaptor.capture());
        ScheduleEntity scheduleEntity = scheduleEntityArgumentCaptor.getValue();
        verify(quartzJobService).startJob(scheduleEntity.getId(), 0);
    }

    @Test
    public void testStart_WhenCalledWithException_ShouldThrowRuntimeException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        OnlineSchemaChangeContextHolder.setCreatorId(1L);
        OnlineSchemaChangeContextHolder.setOrganizationId(1L);
        OnlineSchemaChangeContextHolder.setFlowTaskId(1L);

        when(organizationService.getCurrentUser()).thenReturn(new User());
        when(taskHandler.generateSubTaskParameters(connectionConfig, schema)).thenThrow(new RuntimeException());

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);

        try {
            OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
            onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
            onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
            onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
            onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
            onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
            onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
            onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);

            onlineSchemaChangeFlowableTask.start(1L, null, execution);

            fail("Should throw an exception but test passed.");
        } catch (RuntimeException e) {
            // Expected exception.
        }
    }

    @Test
    public void testIsCancelled_WhenCalledWithRunningTask_ShouldReturnFalse() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.findAllByScheduleIdAndStatus(1L, TaskStatus.RUNNING)).thenReturn(new ArrayList<>());

        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        assertThat(result, is(false));
    }

    @Test
    public void testIsCancelled_WhenCalledWithCanceledTask_ShouldReturnTrue() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.findAllByScheduleIdAndStatus(1L, TaskStatus.RUNNING)).thenReturn(new ArrayList<>());
        when(scheduleTaskRepository.findAllByScheduleIdAndStatus(1L, TaskStatus.CANCELED)).thenReturn(new ArrayList<>());
        when(scheduleTaskRepository.findAllByScheduleIdAndStatus(1L, TaskStatus.FAILED)).thenReturn(new ArrayList<>());
        when(scheduleTaskRepository.findAllByScheduleIdAndStatus(1L, TaskStatus.DONE)).thenReturn(new ArrayList<>());

        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(new PageImpl<>(List.of(new ScheduleTaskEntity())));

        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        assertThat(result, is(true));
    }

    @Test
    public void testCancel_WhenCalled_ShouldCancelTaskAndUpdateExecutionStatus() {
        when(scheduleTaskRepository.findAllByScheduleId(1L)).thenReturn(new ArrayList<>());
        when(scheduleTaskRepository.save(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        when(quartzJobService.terminate(anyLong(), anyLong())).thenReturn(true);

        onlineSchemaChangeFlowableTask.cancel(true, 1L, null);

        verify(quartzJobService).terminate(1L, 1L);
        verify(scheduleTaskRepository).save(scheduleTaskEntityArgumentCaptor.capture());
        ScheduleTaskEntity scheduleTaskEntity = scheduleTaskEntityArgumentCaptor.getValue();
        assertThat(scheduleTaskEntity.getStatus(), is(TaskStatus.CANCELED));

        ExecutionEntity executionEntity = new ExecutionEntity();
        when(executionEntity.getId()).thenReturn(1L);
        when(executionEntity.getStatus()).thenReturn(FlowStatus.EXECUTING.name());
        when(taskService.detail(1L)).thenReturn(executionEntity);

        onlineSchemaChangeFlowableTask.cancel(true, 1L, null);

        verify(taskService).update(executionEntity);
        verify(executionEntity).setStatus(FlowStatus.CANCELED.name());
    }

    @Test
    public void testCancel_WhenCalledWithException_ShouldThrowRuntimeException() {
        when(scheduleTaskRepository.findAllByScheduleId(1L)).thenReturn(new ArrayList<>());

        try {
            onlineSchemaChangeFlowableTask.cancel(true, 1L, null);
            fail("Should throw an exception but test passed.");
        } catch (RuntimeException e) {
            // Expected exception.
        }
    }

    @Test
    public void testGetStatus_WhenRunning_ShouldReturnRunning() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.findAllByScheduleId(1L)).thenReturn(new ArrayList<>());

        String result = onlineSchemaChangeFlowableTask.getStatus();

        assertThat(result, is(TaskStatus.RUNNING.name()));
    }

    @Test
    public void testGetStatus_WhenFailed_ShouldReturnFailed() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.findAllByScheduleId(1L)).thenReturn(List.of(new ScheduleTaskEntity()));

        String result = onlineSchemaChangeFlowableTask.getStatus();

        assertThat(result, is(TaskStatus.FAILED.name()));
    }

    @Test
    public void testGetStatus_WhenDone_ShouldReturnDone() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.findAllByScheduleId(1L)).thenReturn(List.of(new ScheduleTaskEntity()));

        String result = onlineSchemaChangeFlowableTask.getStatus();

        assertThat(result, is(TaskStatus.DONE.name()));
    }

    @Test
    public void testGetStatus_WhenCanceled_ShouldReturnCanceled() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.findAllByScheduleId(1L)).thenReturn(List.of(new ScheduleTaskEntity()));

        String result = onlineSchemaChangeFlowableTask.getStatus();

        assertThat(result, is(TaskStatus.CANCELED.name()));
    }

    @Test
    public void testGetStatus_WhenEmpty_ShouldReturnEmpty() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        String result = onlineSchemaChangeFlowableTask.getStatus();

        assertThat(result, is(StringUtils.EMPTY));
    }

}