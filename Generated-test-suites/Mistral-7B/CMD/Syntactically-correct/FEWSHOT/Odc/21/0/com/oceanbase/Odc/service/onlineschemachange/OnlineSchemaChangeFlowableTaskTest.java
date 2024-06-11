package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

/**
 * Test case for {@link OnlineSchemaChangeFlowableTask}.
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

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

    @Mock
    private BaseODCFlowTaskDelegate<Void> baseODCFlowTaskDelegate;

    @Mock
    private FlowTaskUtil flowTaskUtil;

    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private User user;

    @Mock
    private ProcessInstance processInstance;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private MockHttpServletRequest request;

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeContextHolder.set(new OnlineSchemaChangeContext(1L, 1L, 1L));
        MockitoAnnotations.initMocks(this);

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "test_schema";
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);

        Mockito.when(connectionConfig.id()).thenReturn("connection_id");
        Mockito.when(onlineSchemaChangeParameters.getSchemaName()).thenReturn(schema);
        Mockito.when(onlineSchemaChangeParameters.getGenerateSubTaskParameters(ArgumentMatchers.any(ConnectionConfig.class),
                ArgumentMatchers.eq(schema))).thenReturn(new ArrayList<>());

        Mockito.when(scheduleService.create(ArgumentMatchers.isA(ScheduleEntity.class))).thenReturn(scheduleEntity);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
                onlineSchemaChangeParameters, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals("connection_id", scheduleEntity.getConnectionId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertFalse(scheduleEntity.isAllowConcurrent());
        assertEquals(creatorId, scheduleEntity.getCreatorId());
        assertEquals(organizationId, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(creatorId, scheduleEntity.getModifierId());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeContextHolder.set(new OnlineSchemaChangeContext(1L, 1L, 1L));
        MockitoAnnotations.initMocks(this);

        OnlineSchemaChangeScheduleTaskParameters taskParameters = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);

        Mockito.when(scheduleRepository.saveAndFlush(ArgumentMatchers.isA(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);
        Mockito.when(taskService.detail(ArgumentMatchers.anyLong())).thenReturn(processInstance);
        Mockito.when(processInstance.getId()).thenReturn(1L);
        Mockito.when(taskService.update(ArgumentMatchers.isA(TaskEntity.class))).thenReturn(processInstance);
        Mockito.when(flowTaskUtil.getTaskCreator(ArgumentMatchers.any(DelegateExecution.class)))
                .thenReturn(user);
        Mockito.when(user.getId()).thenReturn(creatorId);
        Mockito.when(user.getOrganizationId()).thenReturn(organizationId);
        Mockito.when(scheduleTaskRepository.saveAndFlush(ArgumentMatchers.isA(ScheduleTaskEntity.class)))
                .thenReturn(scheduleTaskEntity);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleEntity.getId(),
                taskParameters);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("test_job_name", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(taskParameters), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(taskParameters)),
                scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }

    @Test
    public void testSingleTaskPercentage() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(new ScheduleTaskEntity());
        assertEquals(0.0, percentage, 0.0);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        scheduleTaskEntity.setProgressPercentage(25);

        percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);
        assertEquals(0.25, percentage, 0.001);
    }

    @Test
    public void testProgressStatusUpdate() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());

        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.status);
    }

    @Test
    public void testOnProgressUpdate() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        long taskId = 1;

        Pageable pageable = Pageable.unpaged();
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        Mockito.when(taskService.listTask(pageable, ArgumentMatchers.eq(scheduleId))).thenReturn(tasks);
        Mockito.when(tasks.getSize()).thenReturn(7);
        Mockito.when(tasks.getContent()).thenReturn(tasks.getContent());

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.status);
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();

        OnlineSchemaChangeContextHolder.clear();

        MockitoAnnotations.initMocks(this);

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "test_schema";
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);

        Mockito.when(connectionConfig.id()).thenReturn("connection_id");
        Mockito.when(onlineSchemaChangeParameters.getSchemaName()).thenReturn(schema);
        Mockito.when(onlineSchemaChangeParameters.getGenerateSubTaskParameters(ArgumentMatchers.any(ConnectionConfig.class),
                ArgumentMatchers.eq(schema))).thenReturn(new ArrayList<>());

        Mockito.when(scheduleTaskRepository.listTask(ArgumentMatchers.unresolved(Pageable.class), ArgumentMatchers.eq(scheduleId)))
                .thenReturn(new Page<ScheduleTaskEntity>() {
                    @Override
                    public boolean hasContent() {
                        return true;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public int getSize() {
                        return 1;
                    }

                    @Override
                    public List<ScheduleTaskEntity> getContent() {
                        return new ArrayList<>();
                    }

                    @Override
                    public Pageable getPageable() {
                        return null;
                    }
                });

        Mockito.when(quartzJobService.terminate(scheduleId, ArgumentMatchers.anyLong())).thenReturn(true);

        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        boolean cancel = onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

        assertTrue(cancel);

        Mockito.verify(quartzJobService, Mockito.times(1)).terminate(scheduleId, ArgumentMatchings.anyLong());
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    private Long scheduleId = 1L;
    private Long taskId = 1L;
    private Long creatorId = 1L;
    private Long organizationId = 1L;

}