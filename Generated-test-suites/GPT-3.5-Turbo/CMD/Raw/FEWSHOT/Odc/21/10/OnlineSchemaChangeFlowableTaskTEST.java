package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
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

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleTaskService scheduleTaskService;
    @Mock
    private QuartzJobService quartzJobService;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private OrganizationService organizationService;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void testStart() throws Exception {
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        taskService.start(1L);
        User creator = mock(User.class);
        assertEquals(1L, onlineSchemaChangeFlowableTask.start(1L, taskService, execution));
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnSuccessful() {
        TaskService taskService = mock(TaskService.class);
        taskService.start(1L);
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
    }

    @Test
    public void testOnFailure() {
        TaskService taskService = mock(TaskService.class);
        taskService.start(1L);
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
    }

    @Test
    public void testOnTimeout() {
        TaskService taskService = mock(TaskService.class);
        taskService.start(1L);
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
    }

    @Test
    public void testCancel() {
        TaskService taskService = mock(TaskService.class);
        taskService.start(1L);
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertEquals(true, onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "schema");
        assertNotNull(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters param = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param);
        assertNotNull(scheduleTaskEntity);
    }

}