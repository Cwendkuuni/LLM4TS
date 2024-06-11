package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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
import com.oceanbase.odc.service.quartz.JobBuilder;
import com.oceanbase.odc.service.quartz.QuartzJobBuilder;
import com.oceanbase.odc.service.quartz.Trigger;
import com.oceanbase.odc.service.quartz.TriggerBuilder;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.Execution;
import com.oceanbase.odc.service.task.model.Task;

/**
 * Test class for OnlineSchemaChangeFlowableTask.
 *
 * @author yaobin
 * @date 2023-05-23
 */
@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private TaskService taskService;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        // given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        DelegateExecution execution = mock(DelegateExecution.class);
        Task task = new Task();
        task.setId(1L);
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(1L);

        // when
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
        when(taskService.start(any(Long.class))).thenReturn(null);
        when(organizationService.getCreator(any(Long.class), any(Long.class))).thenReturn(new User());

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // then
        verify(taskService, times(1)).start(eq(1L));
    }

    @Test
    public void testIsSuccessful() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // when
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

        // then
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // when
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        // then
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        // when
        doThrow(new RuntimeException()).when(onlineSchemaChangeFlowableTask).onFailure(any(Long.class), any(TaskService.class));
        long taskId = 1L;
        TaskService taskService = mock(TaskService.class);

        // then
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService, times(1)).cancel(eq(taskId), eq(true));
    }

    @Test
    public void testOnSuccessful() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        // when
        TaskService taskService = mock(TaskService.class);
        long taskId = 1L;

        // then
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(taskService, times(1)).update(any(Task.class));
    }

    @Test
    public void testOnTimeout() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

        // when
        TaskService taskService = mock(TaskService.class);
        long taskId = 1L;

        // then
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verify(taskService, times(1)).cancel(eq(taskId), eq(true));
    }

    @Test
    public void testProgressUpdate() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity scheduleTaskEntity1 = new ScheduleTaskEntity();
        scheduleTaskEntity1.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity1.setProgressPercentage(50);
        ScheduleTaskEntity scheduleTaskEntity2 = new ScheduleTaskEntity();
        scheduleTaskEntity2.setStatus(TaskStatus.DONE);
        ScheduleTaskEntity scheduleTaskEntity3 = new ScheduleTaskEntity();
        scheduleTaskEntity3.setStatus(TaskStatus.FAILED);
        List<ScheduleTaskEntity> scheduleTasks = Arrays.asList(scheduleTaskEntity1, scheduleTaskEntity2, scheduleTaskEntity3);
        Page<ScheduleTaskEntity> page = new PageImpl<>(scheduleTasks);

        // when
        onlineSchemaChangeFlowableTask.percentage = 30;
        onlineSchemaChangeFlowableTask.progressStatusUpdate(page);

        // then
        assertEquals(60, onlineSchemaChangeFlowableTask.percentage);
    }

    @Test
    public void testCreateScheduleEntity() {
        // given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        // when
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);

        // then
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(TaskStatus.ENABLED, scheduleEntity.getStatus());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        // when
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        // then
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testIsCancelled() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

        // then
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // given
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        // when
        onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

        // then
        // no action since this is a mock class
    }
}