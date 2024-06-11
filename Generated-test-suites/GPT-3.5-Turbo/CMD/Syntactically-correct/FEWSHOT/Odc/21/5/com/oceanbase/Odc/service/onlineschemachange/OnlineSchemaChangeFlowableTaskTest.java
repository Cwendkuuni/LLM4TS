package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.springframework.data.domain.Pageable;

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

public class OnlineSchemaChangeFlowableTaskTest {
    private OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

    @Test
    public void testStart() {
        TaskService mockTaskService = mock(TaskService.class);
        DelegateExecution mockExecution = mock(DelegateExecution.class);
        
        try {
            OnlineSchemaChangeFlowableTask.Execution execution = new OnlineSchemaChangeFlowableTask.Execution(1L, mockExecution);
            task.start(1L, mockTaskService, execution);
            
            // Assert statements here
            assertNotNull(TaskStatus.RUNNING, task.getStatus());
            assertEquals(1L, task.getCreatorId());
            assertEquals(1L, task.getOrganizationId());
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    public void testIsSuccessful() {
        task.setStatus(TaskStatus.DONE);
        assertEquals(true, task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.setStatus(TaskStatus.FAILED);
        assertEquals(true, task.isFailure());
    }

    @Test
    public void testOnFailure() {
        TaskService mockTaskService = mock(TaskService.class);
        task.onFailure(1L, mockTaskService);
        
        // Assertion statements here
    }

    @Test
    public void testOnSuccessful() {
        TaskService mockTaskService = mock(TaskService.class);
        task.onSuccessful(1L, mockTaskService);
        
        // Assertion statements here
    }

    @Test
    public void testOnTimeout() {
        TaskService mockTaskService = mock(TaskService.class);
        task.onTimeout(1L, mockTaskService);
        
        // Assertion statements here
    }

    @Test
    public void testOnProgressUpdate() {
        TaskService mockTaskService = mock(TaskService.class);
        task.onProgressUpdate(1L, mockTaskService);
        
        // Assertion statements here
    }

    @Test
    public void testCancel() {
        TaskService mockTaskService = mock(TaskService.class);
        assertEquals(true, task.cancel(true, 1L, mockTaskService));
        
        // Assertion statements here
    }

    @Test
    public void testIsCancelled() {
        task.setStatus(TaskStatus.CANCELED);
        assertEquals(true, task.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        
        ScheduleEntity entity = task.createScheduleEntity(connectionConfig, parameter, schema);
        
        // Assertion statements here
        assertNotNull(entity);
        assertEquals(connectionConfig.id(), entity.getConnectionId());
        assertEquals(schema, entity.getDatabaseName());
        // Add more assertions as needed
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        
        ScheduleTaskEntity entity = task.createScheduleTaskEntity(scheduleId, param);
        
        // Assertion statements here
        assertNotNull(entity);
        assertEquals(scheduleId.toString(), entity.getJobName());
        // Add more assertions as needed
    }
}