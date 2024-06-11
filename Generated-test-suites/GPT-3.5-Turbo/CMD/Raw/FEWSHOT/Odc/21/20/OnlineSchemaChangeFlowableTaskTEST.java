package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    @Mock
    private ScheduleEntity scheduleEntity;
    @Mock
    private DelegateExecution execution;
    @Mock
    private User creator;
    @Mock
    private TaskService taskService;

    private static final String schema = "testSchema";
    private static final String uid = "testUid";

    @Before
    public void setUp() {
        when(creator.getId()).thenReturn(1L);
        when(creator.getOrganizationId()).thenReturn(1L);
        when(creator.getOrganizationId()).thenReturn(1L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn(uid);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);

        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);

        when(onlineSchemaChangeParameters.isContinueOnError()).thenReturn(true);
    }

    @Test
    public void testStart() throws Exception {
        when(scheduleEntity.getId()).thenReturn(1L);
        when(onlineSchemaChangeParameters.generateSubTaskParameters(eq(connectionConfig), eq(schema)))
                .thenReturn(List.of(new OnlineSchemaChangeScheduleTaskParameters()));

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
        verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
        verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
        verify(taskHandler, times(1)).start(eq(1L), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }
    
    @Test
    public void testCancel() {
        ScheduleTaskEntity runningTask = new ScheduleTaskEntity();
        runningTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(List.of(runningTask));

        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        verify(taskService, times(1)).cancel(1L);
        verify(taskHandler, times(1)).terminate(anyLong(), anyLong());
        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testProgressStatusUpdate() {
        List<ScheduleTaskEntity> tasks = List.of(new ScheduleTaskEntity(TaskStatus.DONE), 
                                                new ScheduleTaskEntity(TaskStatus.FAILED), 
                                                new ScheduleTaskEntity(TaskStatus.RUNNING));

        onlineSchemaChangeFlowableTask.progressStatusUpdate(new PageImpl<>(tasks));

        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());

        tasks = List.of(new ScheduleTaskEntity(TaskStatus.FAILED), 
                        new ScheduleTaskEntity(TaskStatus.FAILED), 
                        new ScheduleTaskEntity(TaskStatus.PREPARING));

        onlineSchemaChangeFlowableTask.progressStatusUpdate(new PageImpl<>(tasks));

        assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.getStatus());

        tasks = List.of(new ScheduleTaskEntity(TaskStatus.CANCELED), 
                        new ScheduleTaskEntity(TaskStatus.DONE), 
                        new ScheduleTaskEntity(TaskStatus.PREPARING));

        onlineSchemaChangeFlowableTask.progressStatusUpdate(new PageImpl<>(tasks));

        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setStatus(TaskStatus.RUNNING);
        task.setProgressPercentage(50.0);

        double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(task);

        assertEquals(0.5, percentage, 0.001);
    }
}