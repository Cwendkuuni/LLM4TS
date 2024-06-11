package com.oceanbase.odc.service.onlineschemachange.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {
    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private ScheduleTaskService scheduleTaskService;
    @MockBean
    private QuartzJobService quartzJobService;
    @MockBean
    private OrganizationService organizationService;
    @MockBean
    private OscTaskCompleteHandler completeHandler;
    @MockBean
    private TaskService taskService;

    private OnlineSchemaChangeFlowableTask testee;

    @Autowired
    private FlowTaskUtil flowTaskUtil;

    @Before
    public void setUp() {
        testee = new OnlineSchemaChangeFlowableTask();
        testee.flowTaskUtil = flowTaskUtil;
        testee.scheduleTaskRepository = scheduleTaskRepository;
        testee.scheduleService = scheduleService;
        testee.scheduleTaskService = scheduleTaskService;
        testee.quartzJobService = quartzJobService;
        testee.organizationService = organizationService;
        testee.taskHandler = mock(OnlineSchemaChangeTaskHandler.class);
        testee.completeHandler = mock(OscTaskCompleteHandler.class);
        testee.taskService = taskService;
    }

    @Test
    public void testStart() throws Exception {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        TaskEntity task = new TaskEntity();
        task.setId(1L);

        when(taskService.start(anyLong())).thenReturn(task);
        when(flowTaskUtil.getTaskCreator(any())).thenReturn(new User());
        when(flowTaskUtil.getOnlineSchemaChangeParameter(any())).thenReturn(parameter);
        when(flowTaskUtil.getConnectionConfig(any())).thenReturn(connectionConfig);
        when(flowTaskUtil.getSchemaName(any())).thenReturn(schema);

        // When
        testee.start(1L, taskService, delegateExecution);

        // Then
        ArgumentCaptor<ScheduleEntity> scheduleEntityArgumentCaptor = ArgumentCaptor.forClass(ScheduleEntity.class);
        verify(scheduleService).create(scheduleEntityArgumentCaptor.capture());
        ScheduleEntity scheduleEntity = scheduleEntityArgumentCaptor.getValue();
        assertEquals(scheduleEntity.getJobType(), JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        assertEquals(scheduleEntity.getStatus(), ScheduleStatus.ENABLED);

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskParameters> paramArgumentCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskParameters.class);
        assertEquals(scheduleEntity.getJobParametersJson(), JsonUtils.toJson(paramArgumentCaptor.capture()));
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = paramArgumentCaptor.getValue();
        assertEquals(onlineSchemaChangeScheduleTaskParameters.getUid(), "testUid");
        assertEquals(onlineSchemaChangeScheduleTaskParameters.getConnectionConfig().getId(), connectionConfig.getId());
        assertEquals(onlineSchemaChangeScheduleTaskParameters.getSchema(), schema);

        when(scheduleTaskRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        testee.createScheduleTaskEntity(scheduleEntity.getId(), onlineSchemaChangeScheduleTaskParameters);

        verify(taskHandler).start(scheduleEntity.getId(), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        // Given
        testee.status = TaskStatus.DONE;

        // Then
        assertTrue(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        // Given
        testee.status = TaskStatus.FAILED;

        // Then
        assertTrue(testee.isFailure());
    }

    @Test
    public void testOnFailure() {
        // Given

        // When
        testee.onFailure(1L, taskService);

        // Then
        verify(taskService).cancel(1L);
    }

    @Test
    public void testOnSuccessful() {
        // Given

        // When
        testee.onSuccessful(1L, taskService);

        // Then
        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() {
        // Given

        // When
        testee.onTimeout(1L, taskService);

        // Then
        verify(taskService).cancel(1L);
    }

    @Test
    public void testOnProgressUpdate() {
        // Given
        List<ScheduleTaskEntity> tasks = generateTasks();

        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);

        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(page);

        // When
        testee.onProgressUpdate(1L, taskService);

        // Then
        verify(taskService, times(2)).update(any(TaskEntity.class));
    }

    private List<ScheduleTaskEntity> generateTasks() {
        return List.of(
                new ScheduleTaskEntity().setId(1L).setStatus(TaskStatus.RUNNING).setProgressPercentage(10),
                new ScheduleTaskEntity().setId(2L).setStatus(TaskStatus.FAILED).setProgressPercentage(0),
                new ScheduleTaskEntity().setId(3L).setStatus(TaskStatus.CANCELED).setProgressPercentage(100),
                new ScheduleTaskEntity().setId(4L).setStatus(TaskStatus.PREPARING).setProgressPercentage(0)
        );
    }
}