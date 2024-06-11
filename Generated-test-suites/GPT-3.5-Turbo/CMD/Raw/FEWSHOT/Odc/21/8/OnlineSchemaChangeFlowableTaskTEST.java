package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
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

    @InjectMocks
    private OnlineSchemaChangeFlowableTask task;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        when(taskService.start(taskId)).thenReturn(true);
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("1");
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        ScheduleEntity schedule = new ScheduleEntity();
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(schedule);
        
        assertNotNull(task.start(taskId, taskService, execution));
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
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onFailure(taskId, taskService);
        verify(taskService, times(1)).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onSuccessful(taskId, taskService);
        verify(taskService, times(1)).update(any());
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        task.onTimeout(taskId, taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);
        task.onProgressUpdate(taskId, taskService);
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(List.of(mock(ScheduleTaskEntity.class)));
        assertTrue(task.cancel(true, taskId, taskService));
    }

    @Test
    public void testIsCancelled() {
        task.setStatus(TaskStatus.CANCELED);
        assertEquals(true, task.isCancelled());
    }
}