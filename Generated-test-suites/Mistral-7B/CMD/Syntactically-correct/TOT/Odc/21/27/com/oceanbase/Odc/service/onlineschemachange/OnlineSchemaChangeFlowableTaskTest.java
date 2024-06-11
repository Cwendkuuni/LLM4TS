package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    private BaseODCFlowTaskDelegate<Void> onlineSchemaChangeFlowableTask;
    private TaskService taskService;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        taskService = mock(TaskService.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        connectionConfig = Mockito.spy(new ConnectionConfig());
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask() {
            @Override
            @Autowired
            public TaskService getTaskService() {
                return taskService;
            }

            @Override
            @Autowired
            public ScheduleService getScheduleService() {
                return scheduleService;
            }

            @Override
            @Autowired
            public ScheduleTaskService getScheduleTaskService() {
                return scheduleTaskService;
            }

            @Override
            @Autowired
            public QuartzJobService getQuartzJobService() {
                return quartzJobService;
            }

            @Override
            @Autowired
            public OrganizationService getOrganizationService() {
                return organizationService;
            }
        };
    }

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));
        when(taskService.listById(anyLong())).thenReturn(Optional.of(new TaskEntity()));
        when(taskService.start(anyLong())).thenReturn(null);
        doReturn(null).when(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schema");

        onlineSchemaChangeFlowableTask.start(1L, taskService, Mockito.mock(DelegateExecution.class));

        verify(taskService, times(1)).start(1L);
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test(expected = Exception.class)
    public void testStart_fail() throws Exception {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));
        when(taskService.listById(anyLong())).thenReturn(Optional.empty());

        onlineSchemaChangeFlowableTask.start(1L, taskService, Mockito.mock(DelegateExecution.class));

        // assert exception is thrown
    }

    @Test
    public void testOnSuccessful() {
        TaskEntity taskEntity = new TaskEntity();
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));

        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        verify(taskService, times(1)).update(taskEntity);
    }

    @Test
    public void testOnFailure() {
        TaskEntity taskEntity = new TaskEntity();
        when(taskEntity.getStatus()).thenReturn(TaskStatus.FAILED);
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));

        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        verify(taskService, times(1)).update(taskEntity);
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));
        when(taskService.detail(anyLong())).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        assertThat(result, is(true));
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));
        when(taskService.detail(anyLong())).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        assertThat(result, is(true));
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));
        when(taskService.listById(anyLong())).thenReturn(Optional.of(new TaskEntity()));
        doThrow(Exception.class).when(taskService).cancel(anyLong(), anyBoolean(), anyLong(), any(TaskService.class));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // assert exception is thrown
        verify(taskService, times(1)).cancel(anyLong(), anyBoolean(), anyLong(), any(TaskService.class));
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));
        when(taskService.detail(anyLong())).thenReturn(taskEntity);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.CANCELED);

        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        assertThat(result, is(true));
    }

    @Test
    public void testCreateScheduleEntity() {
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());

        ScheduleEntity result = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schema");

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

        ScheduleTaskEntity result = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, new OnlineSchemaChangeScheduleTaskParameters());

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testListTask() {
        Page<TaskEntity> taskEntities = Page.empty();
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(taskEntities);

        List<TaskEntity> result = onlineSchemaChangeFlowableTask.listTask(1, 10, 0);

        assertThat(result, Matchers.empty());
    }

    @Test
    public void testListTask_success() {
        Page<TaskEntity> taskEntities = Page.of(Arrays.asList(new TaskEntity(), new TaskEntity()), Pageable.unpaged());
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(taskEntities);

        List<TaskEntity> result = onlineSchemaChangeFlowableTask.listTask(1, 10, 0);

        assertThat(result, hasSize(2));
    }

    @Test
    public void testProgressStatusUpdate() {
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));
        Page<ScheduleTaskEntity> tasks = Page.of(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity()), Pageable.unpaged());

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        // verify status is updated
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(20);

        double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertThat(result, is(0.2));
    }

    @Test
    public void testSingleTaskPercentage_preparing() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

        double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertThat(result, is(0));
    }

    @Test
    public void testSingleTaskPercentage_done() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.DONE);

        double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        assertThat(result, is(1));
    }

    @Test
    public void testProgressUpdate() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);
        when(taskService.detail(anyLong())).thenReturn(taskEntity);

        OnlineSchemaChangeTaskResult result = onlineSchemaChangeFlowableTask.progressUpdate(1L, taskService);

        assertThat(result, is(InstanceOf.instanceOf(OnlineSchemaChangeTaskResult.class)));
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeContextHolder.set(new TaskContextHolder(1L, 1L, 1L));

        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        OnlineSchemaChangeContextHolder.clear();
    }
}