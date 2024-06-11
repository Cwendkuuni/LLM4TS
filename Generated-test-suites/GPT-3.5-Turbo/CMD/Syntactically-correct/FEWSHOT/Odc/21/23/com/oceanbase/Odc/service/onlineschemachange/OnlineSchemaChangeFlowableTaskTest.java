package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private OnlineSchemaChangeTaskHandler taskHandler;
    private OscTaskCompleteHandler completeHandler;
    private OrganizationService organizationService;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeFlowableTask.scheduleTaskRepository = scheduleTaskRepository;
        scheduleService = mock(ScheduleService.class);
        onlineSchemaChangeFlowableTask.scheduleService = scheduleService;
        scheduleTaskService = mock(ScheduleTaskService.class);
        onlineSchemaChangeFlowableTask.scheduleTaskService = scheduleTaskService;
        quartzJobService = mock(QuartzJobService.class);
        onlineSchemaChangeFlowableTask.quartzJobService = quartzJobService;
        taskHandler = mock(OnlineSchemaChangeTaskHandler.class);
        onlineSchemaChangeFlowableTask.taskHandler = taskHandler;
        completeHandler = mock(OscTaskCompleteHandler.class);
        onlineSchemaChangeFlowableTask.completeHandler = completeHandler;
        organizationService = mock(OrganizationService.class);
        onlineSchemaChangeFlowableTask.organizationService = organizationService;
    }

    @Test
    public void testStart() throws Exception {
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        Long taskId = 1L;
        when(taskService.start(taskId)).thenReturn(null);
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("uid");
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schema");
        ScheduleEntity schedule = new ScheduleEntity();
        when(scheduleService.create(schedule)).thenReturn(schedule);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        when(scheduleTaskRepository.saveAndFlush(taskEntity)).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        // Add assertions here
    }

    @Test
    public void testOnSuccessful() {
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        // Add assertions here
    }

    @Test
    public void testOnTimeout() {
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
        // Add assertions here
    }

    @Test
    public void testOnProgressUpdate() {
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        List<ScheduleTaskEntity> taskList = new ArrayList<>();
        taskList.add(new ScheduleTaskEntity());
        taskList.get(0).setStatus(TaskStatus.PREPARING);
        taskList.get(0).setProgressPercentage(0.0);
        when(tasks.getSize()).thenReturn(1);
        when(tasks.getContent()).thenReturn(taskList);
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        // Add assertions here
    }

    @Test
    public void testCancel() {
        TaskService taskService = mock(TaskService.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(new Page<>());
        onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService);
        // Add assertions here
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }

    private double calculateTaskPercentage(ScheduleTaskEntity scheduleTask) {
        double percentage;
        switch (scheduleTask.getStatus()) {
            case TaskStatus.PREPARING:
                percentage = 0;
                break;
            case TaskStatus.RUNNING:
                percentage = scheduleTask.getProgressPercentage() / 100;
                break;
            default:
                percentage = 1;
        }
        return percentage;
    }
}