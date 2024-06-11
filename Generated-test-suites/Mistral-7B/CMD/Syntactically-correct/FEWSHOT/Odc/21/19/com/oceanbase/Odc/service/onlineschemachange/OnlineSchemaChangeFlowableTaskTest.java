package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.metadb.task.TaskService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskExecution;
import com.oceanbase.odc.service.task.TaskServiceImpl;
import com.oceanbase.odc.service.task.model.TaskResult;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask testee;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private TaskService taskService;
    private OscTaskCompleteHandler taskHandler;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class, Answers.RETURNS_DEEP_STUBS);
        scheduleService = mock(ScheduleService.class, Answers.RETURNS_DEEP_STUBS);
        scheduleTaskService = mock(ScheduleTaskService.class, Answers.RETURNS_DEEP_STUBS);
        quartzJobService = mock(QuartzJobService.class, Answers.RETURNS_DEEP_STUBS);
        organizationService = mock(OrganizationService.class, Answers.RETURNS_DEEP_STUBS);
        taskService = new TaskServiceImpl();
        taskHandler = mock(OscTaskCompleteHandler.class, Answers.RETURNS_DEEP_STUBS);
        testee = new OnlineSchemaChangeFlowableTask() {
            @Autowired
            public void setScheduleTaskRepository(ScheduleTaskRepository scheduleTaskRepository) {
                this.scheduleTaskRepository = scheduleTaskRepository;
            }

            @Autowired
            public void setScheduleService(ScheduleService scheduleService) {
                this.scheduleService = scheduleService;
            }

            @Autowired
            public void setScheduleTaskService(ScheduleTaskService scheduleTaskService) {
                this.scheduleTaskService = scheduleTaskService;
            }

            @Autowired
            public void setQuartzJobService(QuartzJobService quartzJobService) {
                this.quartzJobService = quartzJobService;
            }

            @Autowired
            public void setOrganizationService(OrganizationService organizationService) {
                this.organizationService = organizationService;
            }

            @Autowired
            public void setTaskService(TaskService taskService) {
                this.taskService = taskService;
            }

            @Autowired
            public void setTaskHandler(OscTaskCompleteHandler taskHandler) {
                this.taskHandler = taskHandler;
            }
        };
    }

    @After
    public void tearDown() {
        testee = null;
        scheduleTaskRepository = null;
        scheduleService = null;
        scheduleTaskService = null;
        quartzJobService = null;
        organizationService = null;
        taskService = null;
        taskHandler = null;
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class, Answers.RETURNS_DEEP_STUBS);
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameter);
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("schemaName")).thenReturn(schema);
        when(execution.getId()).thenReturn(1L);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.RUNNING);
        when(taskService.detail(1L)).thenReturn(taskEntity);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        subTaskParameters.add(subTaskParameter);
        when(parameter.generateSubTaskParameters(connectionConfig, schema)).thenReturn(subTaskParameters);

        when(taskHandler.start(1L, subTaskParameters.get(0).getId())).thenReturn(true);
        when(quartzJobService.getJobManager()).thenReturn(mock(org.quartz.Scheduler.class));

        testee.start(1L, taskService, execution);

        verify(quartzJobService).getJobManager();
        verify(scheduleService).create(scheduleEntity);
        verify(scheduleTaskRepository).saveAndFlush(scheduleTaskEntity);
        verify(taskHandler).start(1L, subTaskParameters.get(0).getId());
        verify(taskService).start(1L);
        verify(taskService).update(taskEntity);
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus status = TaskStatus.DONE;
        assertTrue(testee.isSuccessful());
        status = TaskStatus.FAILED;
        assertFalse(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus status = TaskStatus.DONE;
        assertFalse(testee.isFailure());
        status = TaskStatus.FAILED;
        assertTrue(testee.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        Long taskId = 1L;
        testee.onFailure(taskId, taskService);
        verify(taskService).cancel(taskId);
        verify(taskService).update(taskEntity);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        Long taskId = 1L;
        testee.onSuccessful(taskId, taskService);
        TaskResult result = new TaskResult();
        result.setStatus(FlowStatus.EXECUTION_SUCCEEDED.toString());
        when(taskService.detail(taskId)).thenReturn(new ExecutionEntity());
        when(taskService.update(any(ExecutionEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(taskService.detail(taskId)).thenReturn(new ExecutionEntity());
        when(taskService.update(any(ExecutionEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        testee.onSuccessful(taskId, taskService);
        verify(taskService).cancel(taskId);
        verify(taskService).update(any(ExecutionEntity.class));
        verify(taskService).detail(taskId);
        verify(taskService).update(any(ExecutionEntity.class));
        verify(taskService).update(argThat(e -> StringUtils.equals(e.getResultJson(), JsonUtils.toJson(new OnlineSchemaChangeTaskResult(Collections.singletonList(new ScheduleTaskEntity()))))));
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        testee.onTimeout(taskId, taskService);
        verify(taskService).cancel(taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = Page.empty();
        testee.onProgressUpdate(tasks);
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        when(connectionConfig.id()).thenReturn(1L);
        when(parameter.generateSubTaskParameters(connectionConfig, schema)).thenReturn(Collections.emptyList());

        ScheduleEntity scheduleEntity = testee.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(scheduleEntity.getProjectId(), 1L);
        assertEquals(scheduleEntity.getDatabaseId(), 1L);
        assertEquals(scheduleEntity.getDatabaseName(), schema);
        assertEquals(scheduleEntity.getConnectionId(), connectionConfig.id());
        assertEquals(scheduleEntity.getStatus(), ScheduleStatus.ENABLED);
        assertEquals(scheduleEntity.getAllowConcurrent(), false);
        assertEquals(scheduleEntity.getCreatorId(), 1L);
        assertEquals(scheduleEntity.getOrganizationId(), 1L);
        assertEquals(scheduleEntity.getJobParametersJson(), JsonUtils.toJson(parameter));
        assertEquals(scheduleEntity.getTriggerConfigJson(), JsonUtils.toJson(new TriggerConfig()));
        assertEquals(scheduleEntity.getMisfireStrategy(), MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        assertEquals(scheduleEntity.getCreatorId(), 1L);
        assertEquals(scheduleEntity.getModifierId(), 1L);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = testee.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(1L, scheduleTaskEntity.getJobGroup());
        assertEquals(scheduleId.toString(), scheduleTaskEntity.getJobName());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
    }

}