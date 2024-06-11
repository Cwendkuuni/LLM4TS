package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleExecution;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskInstance;

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
    private OnlineSchemaChangeTaskHandler onlineSchemaChangeTaskHandler;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private ProcessInstance processInstance;
    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Test
    public void testSuccess() throws Exception {
        givenSuccessScenario();
        onlineSchemaChangeFlowableTask.start(1L, taskService(), processInstance);
        verify(onlineSchemaChangeTaskHandler).start(eq(1L), eq(1L));
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testFailure() throws Exception {
        givenFailureScenario();
        onlineSchemaChangeFlowableTask.start(1L, taskService(), processInstance);
        verify(onlineSchemaChangeTaskHandler).terminate(eq(1L), eq(1L));
        verify(quartzJobService).deleteJob(anyString(), anyString());
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    private void givenSuccessScenario() {
        when(connectionConfig.id()).thenReturn(1L);
        when(organizationService.getCreatorId()).thenReturn(1L);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = ListUtils.emptyList();
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        when(onlineSchemaChangeTaskHandler.generateSubTaskParameters(connectionConfig, "schema"))
                .thenReturn(subTasks.stream().collect(Collectors.toList()));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);

        TaskService taskService = mock(TaskService.class);
        when(taskService.listTask(any(Pageable.class), eq(1L))).thenReturn(new PageImpl<>(ListUtils.emptyList()));
        when(taskService.update(any(TaskEntity.class))).thenAnswer(invocation -> {
            TaskEntity taskEntity = invocation.getArgument(0);
            taskEntity.setStatus(TaskStatus.DONE);
            return taskEntity;
        });
        when(taskService.start(anyLong())).thenAnswer(invocation -> {
            TaskInstance taskInstance = new TaskInstance();
            taskInstance.setId(1L);
            processInstance.setId(1L);
            taskInstance.setProcessInstanceId(processInstance.getId());
            invocation.getMock().start(1L);
            return taskInstance;
        });

        when(execution.getVariables()).thenReturn(Map.of());

        doNothing().when(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    private void givenFailureScenario() {
        when(connectionConfig.id()).thenReturn(1L);
        when(organizationService.getCreatorId()).thenReturn(1L);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = ListUtils.emptyList();
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        when(onlineSchemaChangeTaskHandler.generateSubTaskParameters(connectionConfig, "schema"))
                .thenThrow(new RuntimeException("Failed to generate subtasks"));

        TaskService taskService = mock(TaskService.class);
        when(taskService.listTask(any(Pageable.class), eq(1L))).thenReturn(new PageImpl<>(ListUtils.emptyList()));
        when(taskService.update(any(TaskEntity.class))).thenAnswer(invocation -> {
            TaskEntity taskEntity = invocation.getArgument(0);
            taskEntity.setStatus(TaskStatus.CANCELED);
            return taskEntity;
        });
        when(taskService.cancel(anyBoolean(), anyLong(), any(TaskService.class)))
                .thenAnswer(invocation -> {
                    boolean mayInterrupt = invocation.getArgument(0);
                    long taskId = invocation.getArgument(1);
                    OnlineSchemaChangeFlowableTask testInstance = invocation.getMock();
                    testInstance.cancel(mayInterrupt, taskId, taskService());
                    return true;
                });

        when(execution.getVariables()).thenReturn(Map.of());

        doNothing().when(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_FAILED);
    }

    private TaskService taskService() {
        return mock(TaskService.class);
    }
}