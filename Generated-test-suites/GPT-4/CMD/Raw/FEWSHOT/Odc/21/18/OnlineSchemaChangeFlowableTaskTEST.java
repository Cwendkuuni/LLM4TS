package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
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
    protected DelegateExecution execution;

    @Mock
    private TaskService taskService;
    
    @Mock
    ConnectionConfig connectionConfig;

    @Mock
    User creator;

    @Mock
    OnlineSchemaChangeParameters parameter;

    @Mock
    ScheduleEntity schedule;
    
    @Mock
    Page<ScheduleTaskEntity> tasks;

    @Before
    public void setUp() {
        Mockito.when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        Mockito.when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        Mockito.when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        Mockito.when(creator.getId()).thenReturn(1L);
        Mockito.when(creator.getOrganizationId()).thenReturn(1L);
        Mockito.when(parameter.isContinueOnError()).thenReturn(true);
        Mockito.when(schedule.getId()).thenReturn(1L);
        Mockito.when(scheduleService.create(schedule)).thenReturn(schedule);
    }

    @Test
    public void testStart() {
        try {
            onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
        } catch(Exception e) {
            fail("Exception should not have occured");
        }
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
    }

    @Test
    public void testProgressStatusUpdate() {
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.PREPARING);
        double actual = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTask);
        assertEquals(0.0, actual, 0.0001);
    }

    @Test
    public void testOnProgressUpdate() {
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
    }

    @Test
    public void testCreateScheduleEntity() {
        ScheduleEntity entity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "test_schema");
        assertNotNull(entity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity entity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param);
        assertNotNull(entity);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}