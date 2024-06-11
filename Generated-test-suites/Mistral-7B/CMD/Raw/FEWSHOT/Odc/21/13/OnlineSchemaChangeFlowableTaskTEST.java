package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.onlineschemachange.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    @Test
    public void testStart() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "test_schema";
        OnlineSchemaChangeContextHolder.clear();
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

        ScheduleEntity scheduleEntity = new OnlineSchemaChangeFlowableTask().createScheduleEntity(connectionConfig, parameter, schema);
        ScheduleTaskEntity scheduleTaskEntity = new OnlineSchemaChangeFlowableTask().createScheduleTaskEntity(scheduleEntity.getId(), Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class));

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setScheduleTaskRepository(Mockito.mock(ScheduleTaskRepository.class));
        task.setScheduleService(Mockito.mock(ScheduleService.class));
        task.setScheduleTaskService(Mockito.mock(ScheduleTaskService.class));
        task.setQuartzJobService(Mockito.mock(QuartzJobService.class));
        task.setTaskHandler(Mockito.mock(OnlineSchemaChangeTaskHandler.class));
        task.setCompleteHandler(Mockito.mock(OscTaskCompleteHandler.class));
        task.setOrganizationService(Mockito.mock(OrganizationService.class));

        Mockito.when(execution.getVariables()).thenReturn(Mockito.mock(Map.class));
        Mockito.when(task.getTaskService()).thenReturn(Mockito.mock(TaskService.class));

        task.start(1L, task.getTaskService(), execution);

        Mockito.verify(task.getTaskService()).start(1L);
        Mockito.verify(task.getOrganizationService()).save(Mockito.any());
        Mockito.verify(task.getQuartzJobService()).start(scheduleEntity.getId(), scheduleTaskEntity.getId());
        Mockito.verify(task.getTaskHandler()).start(scheduleEntity.getId(), scheduleTaskEntity.getId());
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.DONE);
        assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.FAILED);
        assertTrue(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        task.setTaskService(taskService);

        task.onFailure(taskId, taskService);

        Mockito.verify(taskService).update(Mockito.argThat(f -> f.getId().equals(taskId) && f.getStatus() == TaskStatus.FAILED));
    }

    @Test
    public void testOnSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);
        task.setTaskService(taskService);

        task.onSuccessful(taskId, taskService);

        Mockito.verify(taskService).update(Mockito.argThat(f -> f.getId().equals(taskId) && f.getResultJson() != null
                && f.getStatus() == TaskStatus.DONE));
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);
        task.setTaskService(taskService);

        task.onTimeout(taskId, taskService);

        Mockito.verify(taskService).update(Mockito.argThat(f -> f.getId().equals(taskId) && f.getStatus() == TaskStatus.TIMEOUT));
    }

    @Test
    public void testOnProgressUpdate() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);
        task.setTaskService(taskService);

        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
        Mockito.when(task.getScheduleTaskService().listTask(Mockito.any(Pageable.class), Mockito.eq(1L))).thenReturn(tasks);

        task.onProgressUpdate(taskId, taskService);

        Mockito.verify(taskService).update(Mockito.argThat(f -> f.getId().equals(taskId) && f.getProgressPercentage() > 0));
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
        String schema = "test_schema";

        ScheduleEntity scheduleEntity = new OnlineSchemaChangeFlowableTask().createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(1L, scheduleEntity.getCreatorId());
        assertEquals(1L, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(1L, scheduleEntity.getId());
        assertNull(scheduleEntity.getModifierId());
        assertEquals("test_schema", scheduleEntity.getDatabaseName());
        assertEquals(JsonUtils.toJson(new TriggerConfig()), scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        assertEquals(JsonUtils.toJson(parameter), scheduleEntity.getJobParametersJson());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;

        OnlineSchemaChangeScheduleTaskParameters param = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
        ScheduleTaskEntity scheduleTaskEntity = new OnlineSchemaChangeFlowableTask().createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("1", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
        assertEquals(JsonUtils.toJson(param), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(param)), scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
        assertSame(scheduleId, scheduleTaskEntity.getScheduleId());
    }

    @Test
    public void testProgressStatusUpdate() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);

       task.progressStatusUpdate(tasks);

        int successfulTask = 0;
        int failedTask = 0;
        boolean canceled = false;

        for (ScheduleTaskEntity task : tasks) {
            TaskStatus taskStatus = task.getStatus();
            if (taskStatus == TaskStatus.DONE) {
                successfulTask++;
            } else if (taskStatus == TaskStatus.FAILED) {
                failedTask++;
            } else if (taskStatus == TaskStatus.CANCELED) {
                canceled = true;
            }
        }

        if (canceled) {
            assertEquals(TaskStatus.CANCELED, task.getStatus());
        } else if ((successfulTask + failedTask) == tasks.getSize()) {
            if (failedTask == 0) {
                assertEquals(TaskStatus.DONE, task.getStatus());
            } else {
                assertEquals(TaskStatus.FAILED, task.getStatus());
            }
        } else if (failedTask > 0 && !task.isContinueOnError()) {
            assertEquals(TaskStatus.FAILED, task.getStatus());
        }
    }

    @Test
    public void testSingleTaskPercentage() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setProgressPercentage(50);
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        double percentage = task.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(0.5, percentage, 0.001);

        scheduleTaskEntity.setStatus(TaskStatus.DONE);

        percentage = task.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(1.0, percentage, 0.001);
    }
}