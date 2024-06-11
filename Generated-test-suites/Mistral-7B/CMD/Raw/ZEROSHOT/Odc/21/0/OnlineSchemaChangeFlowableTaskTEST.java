package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.model.ConnectionConfig;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfigDto;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegateTest;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.TaskServiceImpl;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest extends BaseODCFlowTaskDelegateTest<OnlineSchemaChangeFlowableTask> {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private TaskService taskService;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        taskService = new TaskServiceImpl();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTest.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTest.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTest.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTest.setTaskService(taskService);
    }

    @Test
    public void testStart() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfigDto().build();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        when(scheduleService.create(ArgumentMatchers.any(ScheduleEntity.class)))
                .thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(ArgumentMatchers.any(ScheduleTaskEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getVariable(BaseODCFlowTaskDelegate.CONNECTION_CONFIG_KEY))
                .thenReturn(connectionConfig);
        when(execution.getVariable(BaseODCFlowTaskDelegate.SCHEMA_NAME_KEY)).thenReturn(schema);

        OnlineSchemaChangeContextHolder.set(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.clear();

        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

        List<OnlineSchemaChangeScheduleTaskParameters> list = parameter.generateSubTaskParameters(connectionConfig, schema);
        Optional<ScheduleTaskEntity> taskEntityOptional = Optional.ofNullable(list.get(0)
                .generateScheduleTaskEntity(1L));

        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        testObj.start(1L, taskService, execution);

        assertEquals(TaskStatus.RUNNING, testObj.getStatus());
        assertEquals(1L, testObj.getScheduleId());
        assertEquals(1L, testObj.getCreatorId());
        assertEquals(1L, testObj.getOrganizationId());
        assertEquals(false, testObj.isContinueOnError());
        assertEquals(0.0, testObj.getPercentage(), 0.0);

        taskEntityOptional.ifPresent(taskEntity -> {
            assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
            assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskParameters()), taskEntity.getParametersJson());
        });
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        testObj.setStatus(TaskStatus.DONE);
        assertTrue(testObj.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        testObj.setStatus(TaskStatus.FAILED);
        assertTrue(testObj.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        taskService.start(taskId);
        testObj.onFailure(taskId, taskService);
        assertEquals(TaskStatus.FAILED, testObj.getStatus());
    }

    @Test
    public void testOnSuccessful() throws Exception {
        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        taskService.start(taskId);
        testObj.onSuccessful(taskId, taskService);
        assertEquals(TaskStatus.DONE, testObj.getStatus());
    }

    @Test
    public void testOnTimeout() throws Exception {
        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        testObj.onTimeout(taskId, taskService);
        assertEquals(TaskStatus.TIMEOUT, testObj.getStatus());
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        Page<ScheduleTaskEntity> page = new PageImpl<>(new ArrayList<>(), Pageable.unpaged(), 2);
        Page<ScheduleTaskEntity> tasks = page;
        when(scheduleTaskService.listTask(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.eq(1L)))
                .thenReturn(tasks);

        testObj.onProgressUpdate(taskId, taskService);
        assertEquals(TaskStatus.RUNNING, testObj.getStatus());
        assertEquals(1.0, testObj.getPercentage(), 0.0);

        tasks.getContent().forEach(taskEntity -> {
            assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
            assertEquals(0.0, taskEntity.getProgressPercentage(), 0.0);
        });
    }

    @Test
    public void testCancel() throws Exception {
        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        boolean result = testObj.cancel(true, taskId, taskService);
        assertTrue(result);
        assertEquals(TaskStatus.CANCELED, testObj.getStatus());
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask testObj = new OnlineSchemaChangeFlowableTask();
        testObj.setStatus(TaskStatus.CANCELED);
        assertTrue(testObj.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfigDto().build();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        ScheduleEntity scheduleEntity = testObj.createScheduleEntity(connectionConfig, parameter, schema);
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = testObj.createScheduleTaskEntity(scheduleId, param);
        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
    }
}