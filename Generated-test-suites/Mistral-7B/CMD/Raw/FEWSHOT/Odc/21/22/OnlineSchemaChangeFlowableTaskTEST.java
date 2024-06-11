package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persist.EntityManager;
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
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
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
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private EntityManager entityManager;

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
    private OscTaskCompleteHandler taskHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionConfig.id()).thenReturn(1L);
        when(entityManager.merge(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(scheduleTaskRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(flowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(new OnlineSchemaChangeParameters());
        when(flowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(flowTaskUtil.getSchemaName(execution)).thenReturn("schemaName");
        when(taskService.start(anyLong())).thenAnswer(invocation -> invocation.getArgument(0));
        when(organizationService.getCreator(anyLong())).thenReturn(new User());
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(Page.empty());

        testee.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        assertTrue(testee.isSuccessful());

        testee.setStatus(TaskStatus.FAILED);
        assertFalse(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(testee.isFailure());

        testee.setStatus(TaskStatus.FAILED);
        assertTrue(testee.isFailure());
    }

    @Test
    public void testOnFailure() {
        long taskId = 1L;
        testee.onFailure(taskId, taskService);

        verify(taskService, times(1)).onFailure(taskId, any());
    }

    @Test
    public void testOnSuccessful() {
        long taskId = 1L;
        testee.onSuccessful(taskId, taskService);

        verify(log, times(1)).info("Online schema change task succeed, taskId={}", taskId);
        verify(taskService, times(1)).update(any());
    }

    @Test
    public void testOnTimeout() {
        long taskId = 1L;
        testee.onTimeout(taskId, taskService);

        verify(log, times(1)).warn("Online schema change task timeout, taskId={}", taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        long taskId = 1L;
        Page<ScheduleTaskEntity> tasks = Page.of(Arrays.asList(
            new ScheduleTaskEntity(),
            new ScheduleTaskEntity()
        ));

        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(tasks);

        testee.onProgressUpdate(taskId, taskService);

        verify(tasks.get(0), times(1)).setStatus(TaskStatus.DONE);
        verify(tasks.get(1), times(1)).setStatus(TaskStatus.FAILED);
        verify(taskService, times(1)).update(any());

        testee.setPercentage(50.0);
        testee.onProgressUpdate(taskId, taskService);

        verify(taskService, times(1)).update(any());
    }

    @Test
    public void testCancel() throws Exception {
        boolean isCancelled = true;
        long taskId = 1L;

        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong()))
                .thenReturn(Page.of(new ScheduleTaskEntity()));
        when(taskHandler.terminate(anyLong(), anyLong())).thenAnswer(invocation -> null);

        testee.cancel(isCancelled, taskId, taskService);

        verify(taskService, times(1)).cancel(taskId);
        verify(quartzJobService, times(1)).terminateJob(anyString(), any());
        verify(testee, times(1)).setStatus(TaskStatus.CANCELED);
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "schemaName";

        ScheduleEntity scheduleEntity = testee.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(scheduleEntity.getCreatorId(), testee.getCreatorId());
        assertEquals(scheduleEntity.getOrganizationId(), testee.getOrganizationId());
        assertEquals("schemaName", scheduleEntity.getDatabaseName());
        assertNotNull(scheduleEntity.getTriggerConfigJson());
        assertNotNull(scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = testee.createScheduleTaskEntity(1L, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("1", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertNotNull(scheduleTaskEntity.getParametersJson());
        assertNotNull(scheduleTaskEntity.getResultJson());
    }
}