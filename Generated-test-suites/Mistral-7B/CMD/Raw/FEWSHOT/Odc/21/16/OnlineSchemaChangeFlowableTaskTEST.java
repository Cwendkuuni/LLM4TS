package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.test.AbstractFlowableTest;
import org.flowable.engine.impl.test.MockProcessEngineTestCase;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerType;
import com.oceanbase.odc.service.task.TaskService;

@MockitoAnnotations.MockitoSettings(mockitoVersion = "2.2.5")
public class OnlineSchemaChangeFlowableTaskTest extends AbstractFlowableTest {

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private OrganizationService organizationService;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void init() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
    }

    @After
    public void clearMock() {
        reset(connectionConfig, onlineSchemaChangeParameters, scheduleService, scheduleTaskRepository,
                quartzJobService, scheduleTaskService, taskHandler, completeHandler, organizationService);
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#createScheduleEntity(ConnectionConfig, OnlineSchemaChangeParameters, String)}
     */
    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeContextHolder.setCreatorId(1L);
        OnlineSchemaChangeContextHolder.setOrganizationId(1L);
        String schema = "testSchema";

        ScheduleEntity createScheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                onlineSchemaChangeParameters, schema);

        assertEquals(scheduleService, createScheduleEntity.getScheduleService());
        assertEquals(connectionConfig.getId(), createScheduleEntity.getConnectionId());
        assertEquals(schema, createScheduleEntity.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, createScheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, createScheduleEntity.getStatus());
        assertEquals(false, createScheduleEntity.isAllowConcurrent());
        assertEquals(1L, createScheduleEntity.getCreatorId());
        assertEquals(1L, createScheduleEntity.getOrganizationId());
        // todo project id database id
        assertEquals(1L, createScheduleEntity.getProjectId());
        assertEquals(1L, createScheduleEntity.getDatabaseId());
        assertEquals(1L, createScheduleEntity.getModifierId());

        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression(checkTaskCronExpression);
        assertEquals(JsonUtils.toJson(triggerConfig), createScheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING,
                createScheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeParameters), createScheduleEntity.getJobParametersJson());
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#createScheduleTaskEntity(Long, OnlineSchemaChangeScheduleTaskParameters)}
     */
    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeContextHolder.setCreatorId(1L);
        OnlineSchemaChangeContextHolder.setOrganizationId(1L);

        OnlineSchemaChangeScheduleTaskParameters param = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
        OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult(Arrays.asList(param));
        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schemaName"))
                .thenReturn(Arrays.asList(param));

        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(1L);

        ScheduleTaskEntity createScheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param);

        assertEquals(TaskStatus.PREPARING, createScheduleTaskEntity.getStatus());
        assertEquals("1", createScheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), createScheduleTaskEntity.getJobGroup());
        assertEquals(0.0, createScheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(result), createScheduleTaskEntity.getResultJson());
        assertEquals(JsonUtils.toJson(param), createScheduleTaskEntity.getParametersJson());
        assertEquals(new Date(), createScheduleTaskEntity.getFireTime());

        verify(scheduleTaskRepository, times(1)).saveAndFlush(createScheduleTaskEntity);
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#isCancelled()}
     */
    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeContextHolder.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#cancel(boolean, Long, TaskService)}
     */
    @Test
    public void testCancel() {
        Long taskId = 1L;
        OnlineSchemaChangeContextHolder.setStatus(TaskStatus.RUNNING);

        ProcessInstance processInstance = mock(ProcessInstance.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        TaskService taskService = mock(TaskService.class);
        when(processInstance.getId()).thenReturn(1L);
        when(processInstance.getExecution()).thenReturn(execution);
        when(execution.getId()).thenReturn(taskId);

        assertTrue(onlineSchemaChangeFlowableTask.cancel(false, taskId, taskService));
        verify(taskService, times(1)).cancel(taskId);

        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.setStatus(TaskStatus.FAILED);
        assertFalse(onlineSchemaChangeFlowableTask.cancel(false, taskId, taskService));
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#isSuccessful()}
     */
    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeContextHolder.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#isFailure()}
     */
    @Test
    public void testIsFailure() {
        OnlineSchemaChangeContextHolder.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#onSuccessful(Long, TaskService)}
     */
    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);
        when(taskService.detail(taskId)).thenReturn(taskEntity);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        doNothing().when(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        verify(taskService, times(1)).update(taskEntity);
        verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#onFailure(Long, TaskService)}
     */
    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);
        when(taskService.detail(taskId)).thenReturn(taskEntity);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        doNothing().when(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_FAILED);

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService, times(1)).update(taskEntity);
        verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_FAILED);
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#onTimeout(Long, TaskService)}
     */
    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);
        when(taskService.detail(taskId)).thenReturn(taskEntity);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        doNothing().when(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.TIMEOUT);

        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        verify(taskService, times(1)).update(taskEntity);
        verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(FlowStatus.TIMEOUT);
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#onProgressUpdate(Long, TaskService)}
     */
    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);

        Page<ScheduleTaskEntity> tasks = Arrays.asList(
                new ScheduleTaskEntity(), new ScheduleTaskEntity()).map(task -> {
                    task.setStatus(TaskStatus.RUNNING);
                    task.setProgressPercentage(50.0);
                    return task;
                }).collect(Collectors.toList());
        Pageable pageable = Pageable.unpaged();
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        when(scheduleTaskService.listTask(pageable, 1L)).thenReturn(tasks);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
        doNothing().when(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        verify(taskService, times(1)).update(taskEntity);
        verify(onlineSchemaChangeFlowableTask, times(1)).progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, atLeast(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#progressStatusUpdate(Page<ScheduleTaskEntity>)}.
     */
    @Test
    public void testProgressStatusUpdate() {
        Page<ScheduleTaskEntity> tasks = Arrays.asList(
                new ScheduleTaskEntity(), new ScheduleTaskEntity(), new ScheduleTaskEntity()).map(task -> {
                    task.setStatus(TaskStatus.RUNNING);
                    if (task.getId() == 1L) {
                        task.setStatus(TaskStatus.DONE);
                    }
                    return task;
                }).collect(Collectors.toList());

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.getStatus());
    }

    /**
     * Test method for {@link OnlineSchemaChangeFlowableTask#singleTaskPercentage(ScheduleTaskEntity)}
     */
    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        switch (scheduleTaskEntity.getStatus()) {
            case PREPARING:
                assertEquals(0.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity));
                break;
            case RUNNING:
                assertEquals(scheduleTaskEntity.getProgressPercentage() / 100.0,
                        onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity));
                break;
            default:
                assertEquals(1.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity));
                break;
        }
    }
}