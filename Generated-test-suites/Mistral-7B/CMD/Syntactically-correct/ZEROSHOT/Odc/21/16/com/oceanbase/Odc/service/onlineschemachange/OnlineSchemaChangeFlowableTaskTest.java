package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.identity.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private QuartzJobService quartzJobServiceMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    private ScheduleTaskService scheduleTaskServiceMock;
    private ScheduleService scheduleServiceMock;
    private TaskService taskServiceMock;
    private OrganizationService organizationServiceMock;
    private ConnectionConfig connectionConfigMock;
    private OnlineSchemaChangeParameters onlineSchemaChangeParametersMock;

    @Before
    public void setUp() {
        quartzJobServiceMock = mock(QuartzJobService.class);
        scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
        scheduleTaskServiceMock = mock(ScheduleTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        taskServiceMock = mock(TaskService.class);
        organizationServiceMock = mock(OrganizationService.class);
        connectionConfigMock = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParametersMock = Mockito.mock(OnlineSchemaChangeParameters.class);

        when(connectionConfigMock.getId()).thenReturn(1L);
        when(onlineSchemaChangeParametersMock.isContinueOnError()).thenReturn(true);

        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobServiceMock);
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepositoryMock);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskServiceMock);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleServiceMock);
        onlineSchemaChangeFlowableTask.setTaskService(taskServiceMock);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationServiceMock);
    }

    @Test
    public void testStart() throws Exception {
        User creator = Mockito.mock(User.class);
        when(creator.getId()).thenReturn(1L);

        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(Arrays.asList(new ScheduleTaskEntity()));
        when(taskServiceMock.detail(anyLong())).thenReturn(new TaskEntity());
        when(taskServiceMock.start(anyLong())).thenReturn(null);

        when(organizationServiceMock.getOrganizationId(any())).thenReturn(1L);
        when(flowTaskUtilMock.getSchemaName(any())).thenReturn("my_schema");

        when(scheduleServiceMock.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());

        when(scheduleTaskRepositoryMock.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(quartzJobServiceMock.createJobDetail(any(String.class), any(String.class), any(Class.class))).thenReturn(null);
        when(quartzJobServiceMock.scheduleJob(any(String.class), eq(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name()), anyLong(), anyLong()))
                .thenReturn(null);

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getVariables()).thenReturn(Arrays.asMap());
        when(execution.setVariable(anyString(), any())).thenReturn(null);
        when(execution.getProcessBusinessKey()).thenReturn("12345");

        onlineSchemaChangeFlowableTask.start(1L, taskServiceMock, execution);

        verify(taskServiceMock).start(1L);
        verify(organizationServiceMock).getOrganizationId(eq("12345"));
        verify(flowTaskUtilMock).getSchemaName(execution);
        verify(quartzJobServiceMock).createJobDetail(anyString(), eq(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name()).toString(),
                eq(OnlineSchemaChangeTaskHandler.class));
        verify(quartzJobServiceMock).scheduleJob(anyString(), eq(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name()).toString(), anyLong(), anyLong());
        verify(connectionConfigMock, never()).getId();
        verify(onlineSchemaChangeParametersMock, never()).isContinueOnError();
        verifyNoMoreInteractions(quartzJobServiceMock, scheduleServiceMock, scheduleTaskRepositoryMock, taskServiceMock,
                organizationServiceMock, connectionConfigMock, onlineSchemaChangeParametersMock);
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus taskStatus = TaskStatus.DONE;
        when(taskServiceMock.detail(anyLong())).thenReturn(new TaskEntity().setStatus(taskStatus));
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus taskStatus = TaskStatus.FAILED;
        when(taskServiceMock.detail(anyLong())).thenReturn(new TaskEntity().setStatus(taskStatus));
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testCancel() throws Exception {
        when(taskServiceMock.cancel(anyLong(), anyBoolean(), any(), any())).thenReturn(true);
        OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult(Arrays.asList(new ScheduleTaskEntity()));
        when(scheduleTaskServiceMock.listTask(any(Pageable.class), anyLong())).thenReturn(new PageablePageImpl<>(
                Arrays.asList(new ScheduleTaskEntity().setStatus(TaskStatus.RUNNING)), new PageImpl.PageMetadata()));
        when(scheduleTaskRepositoryMock.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(quartzJobServiceMock.terminate(anyLong(), anyLong())).thenReturn(null);

        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskServiceMock);

        verify(taskServiceMock).cancel(1L, true, any(), any());
        verify(scheduleTaskServiceMock).listTask(any(Pageable.class), eq(1L));
        verify(scheduleTaskRepositoryMock).saveAndFlush(any(ScheduleTaskEntity.class));
        verify(quartzJobServiceMock).terminate(anyLong(), anyLong());
        verifyNoMoreInteractions(taskServiceMock, scheduleTaskServiceMock, scheduleTaskRepositoryMock, quartzJobServiceMock);
    }

    @Test
    public void testIsCancelled() throws Exception {
        TaskStatus taskStatus = TaskStatus.CANCELED;
        when(taskServiceMock.detail(anyLong())).thenReturn(new TaskEntity().setStatus(taskStatus));
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "my_schema";

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
        assertEquals(OnlineSchemaChangeFlowableTask.JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleEntity.getJobType());
        assertEquals(taskStatus.GET_NAME(), scheduleEntity.getStatus());
        assertEquals(1L, scheduleEntity.getCreatorId());
        assertEquals(1L, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(JsonUtils.toJson(triggerConfig), scheduleEntity.getTriggerConfigJson());
        assertEquals(JsonUtils.toJson(onlineSchemaChangeParameters), scheduleEntity.getJobParametersJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
        verifyNoMoreInteractions(connectionConfig, onlineSchemaChangeParameters);
    }

    @Test
    public void testCreateScheduleTaskEntity() throws Exception {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId,
                onlineSchemaChangeScheduleTaskParameters);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals("my_job_name", scheduleTaskEntity.getJobName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0);
        assertEquals(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters), scheduleTaskEntity.getParametersJson());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(onlineSchemaChangeScheduleTaskParameters)),
                scheduleTaskEntity.getResultJson());
        assertEquals(new Date(), scheduleTaskEntity.getFireTime());
        verifyNoMoreInteractions(scheduleId, onlineSchemaChangeScheduleTaskParameters);
    }
}

class PageablePageImpl<T> implements Page<T> {
    private PageImpl<T> pageImpl;

    public PageablePageImpl(List<T> content, PageImpl<T> pageImpl) {
        this.pageImpl = pageImpl;
    }

    @Override
    public int getSize() {
        return pageImpl.getSize();
    }

    @Override
    public int getNumber() {
        return pageImpl.getNumber();
    }

    @Override
    public int getNumberOfElements() {
        return pageImpl.getNumberOfElements();
    }

    @Override
    public List<T> getContent() {
        return pageImpl.getContent();
    }

    @Override
    public Page<T> nextPageable() {
        return pageImpl.nextPageable();
    }

    @Override
    public boolean hasNext() {
        return pageImpl.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return pageImpl.hasPrevious();
    }

    @Override
    public boolean isFirst() {
        return pageImpl.isFirst();
    }

    @Override
    public boolean isLast() {
        return pageImpl.isLast();
    }

    @Override
    public Page<T> previousPageable() {
        return pageImpl.previousPageable();
    }

    @Override
    public Page<T> firstPageable() {
        return pageImpl.getFirstPage();
    }

    @Override
    public long getTotalElements() {
        return pageImpl.getTotalElements();
    }

    @Override
    public long getNumberOfElements() {
        return pageImpl.getNumberOfElements();
    }

    @Override
    public boolean hasContent() {
        return pageImpl.hasContent();
    }
}