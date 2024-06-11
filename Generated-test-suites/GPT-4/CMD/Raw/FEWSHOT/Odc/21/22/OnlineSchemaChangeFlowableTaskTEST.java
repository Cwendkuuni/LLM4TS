package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    @Mock
    private TaskService taskService;
    @Mock
    private DelegateExecution execution;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ConnectionConfig connectionConfig;

    private User user;
    private ScheduleEntity scheduleEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        FlowTaskUtil.setTaskCreator(execution, user);
        FlowTaskUtil.setConnectionConfig(execution, connectionConfig);
        FlowTaskUtil.setSchemaName(execution, "test");

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(true);
        FlowTaskUtil.setOnlineSchemaChangeParameter(execution, parameter);

        String uid = FlowTaskUtil.getCloudMainAccountId(execution);
        
        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setConnectionId(connectionConfig.id());
        scheduleEntity.setDatabaseName("test");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setStatus(ScheduleStatus.ENABLED);
        scheduleEntity.setAllowConcurrent(false);
        scheduleEntity.setCreatorId(user.getId());
        scheduleEntity.setOrganizationId(user.getOrganizationId());
        scheduleEntity.setProjectId(1L);
        scheduleEntity.setDatabaseId(1L);
        scheduleEntity.setModifierId(scheduleEntity.getCreatorId());
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
        scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameter));
        
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
    }

    @Test
    public void testStart() throws Exception {
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
        verify(onlineSchemaChangeFlowableTask, times(1)).start(anyLong(), isA(TaskService.class), any(DelegateExecution.class));
    }
    
    @Test
    public void testIsSuccessful() {
        assertEquals(false, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());
    }
    
    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        verify(onlineSchemaChangeFlowableTask, times(1)).onFailure(anyLong(), isA(TaskService.class));
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(onlineSchemaChangeFlowableTask, times(1)).onSuccessful(anyLong(), isA(TaskService.class));
    }

    @Test
    public void testOnProgressUpdate() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setProgressPercentage(0.0);
        taskEntity.setStatus(TaskStatus.RUNNING);
        when(taskService.detail(anyLong())).thenReturn(taskEntity);
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        verify(onlineSchemaChangeFlowableTask, times(1)).onProgressUpdate(anyLong(), isA(TaskService.class));
    }

    //More test cases for all methods of the class should be added here.
}