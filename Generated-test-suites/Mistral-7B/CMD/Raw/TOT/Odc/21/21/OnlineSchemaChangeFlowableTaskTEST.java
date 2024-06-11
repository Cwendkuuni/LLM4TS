package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Autowired
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;
    private TaskEntity taskEntity;

    @BeforeEach
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        onlineSchemaChangeScheduleTaskParameters.setConnectionConfig(connectionConfig);
        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleTaskEntity = new ScheduleTaskEntity();
        taskEntity = new TaskEntity();
    }

    @Test
    public void testStart_whenFlowInstanceIsApproved_shouldStartScheduleTask() throws Exception {
        // Given
        Pageable pageable = Pageable.unpaged();
        when(scheduleTaskService.listTask(pageable, scheduleEntity.getId())).thenReturn(new PageImpl<>(List.of(scheduleTaskEntity)));
        when(quartzJobService.scheduleJob(onlineSchemaChangeFlowableTask.getCheckTaskCronExpression(), OnlineSchemaChangeFlowableTask.class)).thenReturn(1);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService, mock(DelegateExecution.class));

        // Then
        verify(quartzJobService, times(1)).scheduleJob(onlineSchemaChangeFlowableTask.getCheckTaskCronExpression(), OnlineSchemaChangeFlowableTask.class);
    }

    @Test
    public void testIsSuccessful_whenTaskStatusIsDone_shouldReturnTrue() {
        // Given
        taskEntity.setStatus(TaskStatus.DONE);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful(1L, taskService);

        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsFailure_whenTaskStatusIsFailed_shouldReturnTrue() {
        // Given
        taskEntity.setStatus(TaskStatus.FAILED);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure(1L, taskService);

        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testOnFailure_whenTaskStatusIsFailed_shouldLogWarning() throws Exception {
        // Given
        doThrow(new Exception("Test error")).when(onlineSchemaChangeFlowableTask).onFailure(1L, taskService);

        // When
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        // Then
        verify(log, times(1)).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful_whenTaskStatusIsDone_shouldUpdateFlowInstanceStatus() throws Exception {
        // Given
        when(taskService.detail(1L)).thenReturn(taskEntity);

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        // Then
        verify(taskService, times(1)).update(taskEntity);
        verify(taskEntity, times(1)).setStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    public void testOnSuccessful_whenTaskStatusIsRunning_shouldNotUpdateFlowInstanceStatus() throws Exception {
        // Given
        when(taskService.detail(1L)).thenReturn(taskEntity);

        // When
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        // Then
        verify(taskService, never()).update(taskEntity);
    }

    @Test
    public void testOnTimeout_whenTaskStatusIsTimeout_shouldLogWarning() throws Exception {
        // Given
        when(taskService.detail(1L)).thenReturn(taskEntity);

        // When
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        // Then
        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testOnProgressUpdate_whenTasksAreSuccessfullyFinished_shouldUpdateTaskStatusAndPercentage() throws Exception {
        // Given
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(ListUtils.unionWith(List.of(scheduleTaskEntity), List.of()));
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleEntity.getId())).thenReturn(tasks);
        when(singleTaskPercentage(scheduleTaskEntity)).thenReturn(0.5);

        // When
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Then
        verify(taskService, times(1)).update(taskEntity);
        verify(taskEntity, times(1)).setStatus(TaskStatus.DONE);
        verify(taskEntity, times(1)).setProgressPercentage(50);
    }

    @Test
    public void testCancel_whenRunningTaskIsFound_shouldTerminateItAndCancelTask() throws Exception {
        // Given
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(List.of(scheduleTaskEntity));
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleEntity.getId())).thenReturn(tasks);

        // When
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // Then
        verify(quartzJobService, times(1)).terminateJob(scheduleEntity.getId().toString(), scheduleTaskEntity.getId());
        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testIsCancelled_whenTaskStatusIsCancelled_shouldReturnTrue() {
        // Given
        taskEntity.setStatus(TaskStatus.CANCELED);

        // When
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Then
        Assertions.assertTrue(result);
    }

    private double singleTaskPercentage(ScheduleTaskEntity scheduleTask) {
        double percentage;
        switch (scheduleTask.getStatus()) {
            case PREPARING:
                percentage = 0;
                break;
            case RUNNING:
                percentage = scheduleTask.getProgressPercentage() / 100;
                break;
            default:
                percentage = 1;
        }
        return percentage;
    }
}