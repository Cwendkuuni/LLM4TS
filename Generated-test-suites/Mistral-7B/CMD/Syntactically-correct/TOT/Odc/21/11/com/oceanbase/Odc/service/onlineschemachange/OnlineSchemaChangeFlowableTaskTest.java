package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

/**
 * Test class for OnlineSchemaChangeFlowableTask.
 */
public class OnlineSchemaChangeFlowableTaskTest {

    private static final long SCHEDULE_ID = 1L;
    private static final long TASK_ID = 1L;
    private static final long FLOW_INSTANCE_ID = 1L;
    private static final long USER_ID = 1L;
    private static final long ORGANIZATION_ID = 1L;
    private static final String SCHEMA = "testSchema";
    private static final boolean CONTINUE_ON_ERROR = false;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private BaseODCFlowTaskDelegate baseODCFlowTaskDelegateMock;
    private OrganizationService organizationServiceMock;
    private ScheduleService scheduleServiceMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    private QuartzJobService quartzJobServiceMock;
    private TaskService taskServiceMock;

    @Autowired
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        baseODCFlowTaskDelegateMock = mock(BaseODCFlowTaskDelegate.class);
        when(baseODCFlowTaskDelegateMock.isSuccessful()).thenReturn(true);
        when(baseODCFlowTaskDelegateMock.isFailure()).thenReturn(false);
        when(baseODCFlowTaskDelegateMock.onSuccessful(anyLong(), any(TaskService.class)))
                .thenReturn(null);
        when(baseODCFlowTaskDelegateMock.onFailure(anyLong(), any(TaskService.class)))
                .thenReturn(null);
        when(baseODCFlowTaskDelegateMock.onTimeout(anyLong(), any(TaskService.class))).thenReturn(null);
        when(baseODCFlowTaskDelegateMock.onProgressUpdate(anyLong(), any(TaskService.class)))
                .thenReturn(null);
        when(baseODCFlowTaskDelegateMock.cancel(anyBoolean(), anyLong(), any(TaskService.class)))
                .thenReturn(true);

        organizationServiceMock = mock(OrganizationService.class);
        when(organizationServiceMock.getUserById(USER_ID)).thenReturn(new User());

        scheduleServiceMock = mock(ScheduleService.class);
        when(scheduleServiceMock.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());

        scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);

        quartzJobServiceMock = mock(QuartzJobService.class);

        taskServiceMock = mock(TaskService.class);
        when(taskServiceMock.start(anyLong())).thenReturn(null);
        when(taskServiceMock.detail(anyLong())).thenReturn(new ExecutionEntity());
        when(taskServiceMock.update(any(TaskEntity.class))).thenReturn(new TaskEntity());
        when(taskServiceMock.cancel(anyLong())).thenReturn(null);

        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setBaseODCFlowTaskDelegate(baseODCFlowTaskDelegateMock);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationServiceMock);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleServiceMock);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepositoryMock);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobServiceMock);
        onlineSchemaChangeFlowableTask.setTaskService(taskServiceMock);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);
    }

    @Test
    public void testStartSuccess() throws Exception {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(scheduleServiceMock.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepositoryMock.saveAndFlush(any(ScheduleTaskEntity.class)))
                .thenReturn(new ScheduleTaskEntity());

        onlineSchemaChangeFlowableTask.start(FLOW_INSTANCE_ID, taskServiceMock, new DelegateExecution());

        ArgumentCaptor<TaskEntity> taskEntityCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskServiceMock).start(taskEntityCaptor.capture());
        TaskEntity taskEntity = taskEntityCaptor.getValue();
        assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
    }

    @Test
    public void testStartFailed() throws Exception {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(scheduleServiceMock.create(any(ScheduleEntity.class))).thenThrow(new RuntimeException());

        onlineSchemaChangeFlowableTask.start(FLOW_INSTANCE_ID, taskServiceMock, new DelegateExecution());

        ArgumentCaptor<TaskEntity> taskEntityCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskServiceMock).start(taskEntityCaptor.capture());
        TaskEntity taskEntity = taskEntityCaptor.getValue();
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testProgressUpdate() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleServiceMock.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
        when(scheduleTaskRepositoryMock.saveAndFlush(any(ScheduleTaskEntity.class)))
                .thenReturn(new ScheduleTaskEntity());
        when(taskServiceMock.listTask(any(Pageable.class), SCHEDULE_ID)).thenReturn(new PageImpl<>(
                ListUtils.emptyIfNull(ListUtils.emptyIfNull(List.of(new ScheduleTaskEntity()))),
                Pageable.unpaged(), 0));

        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(
                ListUtils.emptyIfNull(List.of(new ScheduleTaskEntity())));

        when(quartzJobServiceMock.execute(anyString(), anyMap())).thenReturn(onlineSchemaChangeTaskResult);

        onlineSchemaChangeFlowableTask.start(FLOW_INSTANCE_ID, taskServiceMock, new DelegateExecution());

        ArgumentCaptor<TaskEntity> taskEntityCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskServiceMock).update(taskEntityCaptor.capture());
        TaskEntity taskEntity = taskEntityCaptor.getValue();
        assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());

        onlineSchemaChangeFlowableTask.onProgressUpdate(TASK_ID, taskServiceMock);

        ArgumentCaptor<TaskEntity> taskEntityCaptor2 = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskServiceMock).update(taskEntityCaptor2.capture());
        TaskEntity taskEntity2 = taskEntityCaptor2.getValue();
        assertEquals(TaskStatus.RUNNING, taskEntity2.getStatus());

        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult2 = new OnlineSchemaChangeTaskResult(
                ListUtils.emptyIfNull(ListUtils.emptyIfNull(List.of(new ScheduleTaskEntity(), new ScheduleTaskEntity()))));

        when(quartzJobServiceMock.execute(anyString(), anyMap())).thenReturn(onlineSchemaChangeTaskResult2);

        onlineSchemaChangeFlowableTask.onProgressUpdate(TASK_ID, taskServiceMock);

        ArgumentCaptor<TaskEntity> taskEntityCaptor3 = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskServiceMock).update(taskEntityCaptor3.capture());
        TaskEntity taskEntity3 = taskEntityCaptor3.getValue();
        assertEquals(TaskStatus.DONE, taskEntity3.getStatus());
    }

    @Test
    public void testCancel() throws Exception {
        when(taskServiceMock.cancel(anyLong())).thenReturn(null);
        onlineSchemaChangeFlowableTask.cancel(true, TASK_ID, taskServiceMock);
        verify(taskServiceMock).cancel(TASK_ID);
    }

    @Test
    public void testIsCancelled() throws Exception {
        when(taskServiceMock.detail(anyLong())).thenReturn(new ExecutionEntity());
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.CANCELED);
        when(taskServiceMock.detail(anyLong())).thenReturn(taskEntity);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

}