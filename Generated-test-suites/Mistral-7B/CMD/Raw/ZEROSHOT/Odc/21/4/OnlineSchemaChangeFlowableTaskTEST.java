package com.oceanbase.odc.service.onlineschemachange.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.MisfireStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.exception.TaskServiceException;

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
    private OrganizationService organizationService;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private DelegateExecution execution;
    @Mock
    private TaskService taskService;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        // given
        when(organizationService.getCurrentOrganizationId()).thenReturn(1L);
        when(organizationService.getCurrentUserId()).thenReturn(1L);
        when(connectionConfig.getId()).thenReturn(1L);
        when(connectionConfig.getName()).thenReturn("test");
        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("onlineschemaChangeParameters")).thenReturn(new OnlineSchemaChangeParameters());
        when(execution.getVariable("schema")).thenReturn("testSchema");
        when(execution.getVariable("continueOnError")).thenReturn(true);

        // when
        testee.start(1L, taskService, execution);

        // then
        // Check the call order and result of each method in the start method
        verify(scheduleService).create(any(ScheduleEntity.class));
        verify(taskService).start(1L);
        verify(taskHandler).start(anyLong(), anyLong());
    }

    @Test(expected = TaskServiceException.class)
    public void testOnFailure() throws Exception {
        // given
        when(taskService.detail(anyLong())).thenThrow(new TaskServiceException("test error"));

        // when
        testee.onFailure(1L, taskService);

        // then
        // Method onFailure should not be called when the task is still running
        // as the testee will be cancelled automatically in this case
        // Verify that the exception is thrown
    }

    @Test
    public void testOnSuccessful() throws Exception {
        // given
        when(taskService.detail(anyLong())).thenReturn(new TaskEntity());

        // when
        testee.onSuccessful(1L, taskService);

        // then
        // Check the call order and result of each method in the onSuccessful method
        verify(taskService).update(any(TaskEntity.class));
        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testIsSuccessful() {
        // given
        TaskStatus expectedStatus = TaskStatus.DONE;

        // when
        testee.setStatus(expectedStatus);

        // then
        // Check the result of isSuccessful method
        assertThat(testee.isSuccessful(), is(true));
    }

    @Test
    public void testIsFailure() {
        // given
        TaskStatus expectedStatus = TaskStatus.FAILED;

        // when
        testee.setStatus(expectedStatus);

        // then
        // Check the result of isFailure method
        assertThat(testee.isFailure(), is(true));
    }

    @Test
    public void testCancel() throws Exception {
        // given
        when(taskService.cancel(anyLong())).thenReturn(true);

        // when
        testee.cancel(true, 1L, taskService);

        // then
        // Check the call order and result of each method in the cancel method
        verify(taskService).cancel(anyLong());
        verify(taskHandler).terminate(anyLong(), anyLong());
        verify(taskService).update(any(TaskEntity.class));
    }

    @Test
    public void testIsCancelled() {
        // given
        TaskStatus expectedStatus = TaskStatus.CANCELED;

        // when
        testee.setStatus(expectedStatus);

        // then
        // Check the result of isCancelled method
        assertThat(testee.isCancelled(), is(true));
    }

    @Test
    public void testListScheduleTasks() {
        // given
        when(scheduleTaskRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity())));

        // when
        List<ScheduleTaskEntity> result = testee.listScheduleTasks(Pageable.unpaged());

        // then
        // Check the result of listScheduleTasks method
        assertThat(result, notNullValue());
        assertThat(result.size(), is(2));
    }

    @Test
    public void testGetScheduleTaskByID() {
        // given
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));

        // when
        ScheduleTaskEntity result = testee.getScheduleTaskByID(1L);

        // then
        // Check the result of getScheduleTaskByID method
        assertThat(result, notNullValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScheduleTaskByIDNotFound() {
        // given
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        testee.getScheduleTaskByID(1L);

        // then
        // Verify that an IllegalStateException is thrown
    }

    @Test
    public void testUpdateScheduleTask() throws Exception {
        // given
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));

        // when
        testee.updateScheduleTask(new ScheduleTaskEntity());

        // then
        // Check the call order and result of each method in the updateScheduleTask method
        verify(scheduleTaskRepository).saveAndFlush(any(ScheduleTaskEntity.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateScheduleTaskNotFound() throws Exception {
        // given
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        testee.updateScheduleTask(new ScheduleTaskEntity());

        // then
        // Verify that an IllegalStateException is thrown
    }

    @Test
    public void testDeleteScheduleTask() throws Exception {
        // given
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));

        // when
        testee.deleteScheduleTask(1L);

        // then
        // Check the call order and result of each method in the deleteScheduleTask method
        verify(scheduleTaskRepository).deleteById(anyLong());
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteScheduleTaskNotFound() throws Exception {
        // given
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        testee.deleteScheduleTask(1L);

        // then
        // Verify that an IllegalStateException is thrown
    }

    @Test
    public void testCreateScheduleEntity() {
        // given
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "testSchema";
        boolean continueOnError = true;

        when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
        when(execution.getVariable("onlineschemaChangeParameters")).thenReturn(onlineSchemaChangeParameters);
        when(execution.getVariable("schema")).thenReturn(schema);
        when(execution.getVariable("continueOnError")).thenReturn(continueOnError);

        // when
        ScheduleEntity result = testee.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, schema);

        // then
        // Check the result of createScheduleEntity method
        assertThat(result, notNullValue());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // given
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        // when
        ScheduleTaskEntity result = testee.createScheduleTaskEntity(scheduleId, param);

        // then
        // Check the result of createScheduleTaskEntity method
        assertThat(result, notNullValue());
    }
}