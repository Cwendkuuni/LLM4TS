package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
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
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.Execution;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private QuartzJobService quartzJobService;
    private OrganizationService organizationService;
    private TaskService taskService;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        quartzJobService = mock(QuartzJobService.class);
        organizationService = mock(OrganizationService.class);
        taskService = mock(TaskService.class);
        scheduleService = mock(ScheduleService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);

        when(quartzJobService.scheduleJob(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(TriggerConfig.class), ArgumentMatchers.any(MisfireStrategy.class)))
                .thenReturn(1L);
        when(quartzJobService.triggerJob(ArgumentMatchers.anyLong()))
                .thenReturn(new ExecutionEntity().getId());
        when(scheduleService.create(ArgumentMatchers.any(ScheduleEntity.class)))
                .thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(ArgumentMatchers.any(ScheduleTaskEntity.class)))
                .thenReturn(new ScheduleTaskEntity());

        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);
    }

    @Test
    public void testStart() throws Exception {
        User creator = new User();
        creator.setId(1L);
        creator.setOrganizationId(1L);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setContinueOnError(true);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        String schema = "test_schema";

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getVariables()).thenReturn(Arrays.asList(new Object() {
            {
                put("creator", creator);
                put("parameters", parameters);
                put("connectionConfig", connectionConfig);
                put("schema", schema);
            }
        }));

        when(organizationService.getCreator(ArgumentMatchers.anyLong()))
                .thenReturn(creator);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.create(ArgumentMatchers.any(ScheduleEntity.class)))
                .thenReturn(scheduleEntity);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        when(scheduleTaskRepository.saveAndFlush(ArgumentMatchers.any(ScheduleTaskEntity.class)))
                .thenReturn(scheduleTaskEntity);

        Pageable pageable = mock(Pageable.class);
        Page<ScheduleTaskEntity> page = mock(Page.class);
        when(page.getContent()).thenReturn(new ArrayList<ScheduleTaskEntity>() {{
            add(scheduleTaskEntity);
        }});
        when(scheduleTaskService.listTask(pageable, ArgumentMatchers.anyLong()))
                .thenReturn(page);

        OnlineSchemaChangeContextHolder.clear();

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        BDDMockito.then(quartzJobService).should().scheduleJob("OnlineSchemaChangeTask", 1L,
                ArgumentMatchers.eq(new TriggerConfig()), ArgumentMatchers.eq(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING));

        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getScheduleId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getCreatorId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getOrganizationId());
        assertEquals(true, onlineSchemaChangeFlowableTask.isContinueOnError());
        assertEquals(0.0, onlineSchemaChangeFlowableTask.getPercentage(), 0.0);
        OnlineSchemaChangeContextHolder.assertTraceEquals(1L, 1L, 1L);
    }

}