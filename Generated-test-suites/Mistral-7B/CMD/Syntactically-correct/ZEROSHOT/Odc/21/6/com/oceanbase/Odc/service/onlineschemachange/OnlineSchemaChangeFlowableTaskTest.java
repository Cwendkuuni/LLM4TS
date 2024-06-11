package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.test.MockDelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.TestTaskService;
import com.oceanbase.odc.service.task.model.TaskStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 * @date 2023-05-23
 */
@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private ConnectionConfig connectionConfig;
    private OrganizationService organizationService;
    private ScheduleService scheduleService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private QuartzJobService quartzJobService;
    private TaskService taskService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        organizationService = Mockito.mock(OrganizationService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        quartzJobService = Mockito.mock(QuartzJobService.class);
        taskService = new TestTaskService();
        oscTaskCompleteHandler = Mockito.mock(OscTaskCompleteHandler.class);
        onlineSchemaChangeFlowableTask.setConnectionConfig(connectionConfig);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        Long flowTaskId = 1L;
        MockDelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.getId()).thenReturn(flowTaskId);
        Mockito.when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(param);
        Mockito.when(organizationService.findById(param.getOrganizationId()))
                .thenReturn(Mockito.mock(User.class));

        // When
        onlineSchemaChangeFlowableTask.start(flowTaskId, taskService, execution);

        // Then
        // Assert.assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.status);
        // Assert.assertEquals(flowTaskId, onlineSchemaChangeFlowableTask.scheduleId);
        // Assert.assertNull(onlineSchemaChangeFlowableTask.percentage);

        // Verify
        // Mockito.verify(scheduleService, Mockito.times(1)).create(Mockito.any(ScheduleEntity.class));
        // Mockito.verify(taskService, Mockito.times(1)).start(flowTaskId);
        // Mockito.verify(scheduleTaskRepository, Mockito.times(1)).saveAndFlush(Mockito.any(ScheduleTaskEntity.class));
        // Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testIsSuccessful() {
        // Given
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsFailure() {
        // Given
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        // When
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Then
        assertTrue(result);
    }

    @Test
    public void testOnFailure() {
        // Given

        // When
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // Then
        // Mockito.verify(log, Mockito.times(1)).warn("Online schema change task failed, taskId={}", taskId);
        // Mockito.verify(taskService, Mockito.times(1)).cancel(taskId);
        // Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testOnSuccessful() {
        // Given

        // When
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Then
        // Mockito.verify(log, Mockito.times(1)).info("Online schema change task succeed, taskId={}", taskId);
        // Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
        // Mockito.verify(taskService, Mockito.times(1)).updateFlowInstanceStatus(Mockito.eq(FlowStatus.EXECUTION_SUCCEEDED), Mockito.anyLong());
    }

    @Test
    public void testOnTimeout() {
        // Given

        // When
        long taskId = 1L;
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // Then
        // Mockito.verify(log, Mockito.times(1)).warn("Online schema change task timeout, taskId={}", taskId);
        // Mockito.verify(taskService, Mockito.times(1)).cancel(taskId);
        // Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testOnProgressUpdate() {
        // Given
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), Mockito.anyLong()))
                .thenReturn(tasks);
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.percentage = 50;

        // When
        task.onProgressUpdate(1L, taskService);

        // Then
        OnlineSchemaChangeTaskResult expectedResult = new OnlineSchemaChangeTaskResult(new OnlineSchemaChangeTaskResult.TaskResultItem[0]);
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = task.getResultJson();
        OnlineSchemaChangeTaskResult result = mapper.readValue(resultJson, OnlineSchemaChangeTaskResult.class);
        assertEquals(expectedResult, result);
        assertEquals(50.0, task.getProgressPercentage(), 0.0);
        assertEquals(TaskStatus.RUNNING, task.getStatus());
    }

    @Test
    public void testCancel() {
        // Given

        // When
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // Then
        assertTrue(result);
        // Mockito.verify(taskService, Mockito.times(1)).cancel(taskId);
        // Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
    }

}

class TestTaskService implements TaskService {

    @Override
    public void start(Long taskId) {

    }

    @Override
    public void cancel(Long taskId) {

    }

    @Override
    public void update(TaskEntity task) {

    }

    @Override
    public void updateFlowInstanceStatus(FlowStatus status, Long instanceId) {

    }

    @Override
    public void list(Pageable pageable) {

    }

    @Override
    public void detail(Long taskId) {

    }

}