package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
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
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * Test class for OnlineSchemaChangeFlowableTask
 *
 * @author yaobin
 * @date 2023-05-23
 * @since 4.2.0
 */
@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private TaskService taskService;
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
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // Clear the mocks so that they're ready for the next test.
        MockitoAnnotations.reset(taskService, scheduleTaskRepository, scheduleService, scheduleTaskService,
                quartzJobService, taskHandler, completeHandler, organizationService, onlineSchemaChangeFlowableTask);
    }

    @Test
    public void testStart() throws Exception {
        // Prepare mocks and arguments
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig connectionConfigMock = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfigMock.id()).thenReturn(connectionConfig.getId());
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskParameters scheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        String schema = "schema_name";
        Mockito.when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(parameters);
        Mockito.when(execution.getVariable("onlineSchemaChangeScheduleTaskParameters")).thenReturn(scheduleTaskParameters);
        Mockito.when(execution.getVariable("connectionConfig")).thenReturn(connectionConfigMock);
        Mockito.when(execution.getVariable("schemaName")).thenReturn(schema);
        Mockito.when(execution.getVariable("continueOnError")).thenReturn(true);
        Long flowTaskId = 1L;
        User creator = new User();
        creator.setId(1L);
        creator.setOrganizationId(1L);

        Mockito.when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        Mockito.when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);

        Mockito.when(taskService.detail(flowTaskId)).thenReturn(new TaskEntity());
        Mockito.when(taskService.create(scheduleEntity)).thenReturn(scheduleEntity);
        Mockito.when(taskService.start(flowTaskId)).thenReturn(true);

        // Call the method being tested
        onlineSchemaChangeFlowableTask.start(flowTaskId, taskService, execution);

        // Verify the results
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getScheduleId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getCreatorId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getOrganizationId());
        assertTrue(FlowTaskUtil.getCloudMainAccountId(execution).equals(""));
        assertTrue(onlineSchemaChangeFlowableTask.continueOnError);
        assertNull(onlineSchemaChangeFlowableTask.percentage);
        OnlineSchemaChangeContextHolder.assertTraceEquals(1L, flowTaskId, 1L);

        Mockito.verify(taskService).create(scheduleEntity);
        Mockito.verify(taskService).start(flowTaskId);
        Mockito.verify(taskHandler, Mockito.times(1)).start(scheduleEntity.getId(), scheduleTaskEntity.getId());
    }

    @Test
    public void testIsSuccessful() {
        // Prepare the mock object
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        // Call the method being tested
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Verify the results
        assertTrue(result);
    }

    @Test
    public void testIsFailure() {
        // Prepare the mock object
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

        // Call the method being tested
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Verify the results
        assertTrue(result);
    }

    @Test
    public void testOnFailure() {
        // Prepare the mock objects and arguments
        Long taskId = 1L;
        TaskService taskServiceMock = Mockito.mock(TaskService.class);

        // Call the method being tested
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskServiceMock);

        // Verify the results
        Mockito.verify(taskServiceMock).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testOnSuccessful() {
        // Prepare the mock objects and arguments
        Long taskId = 1L;
        TaskService taskServiceMock = Mockito.mock(TaskService.class);

        // Call the method being tested
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskServiceMock);

        // Verify the results
        Mockito.verify(taskServiceMock).update(Mockito.any(TaskEntity.class));
        Mockito.verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    public void testOnTimeout() {
        // Prepare the mock objects and arguments
        Long taskId = 1L;
        TaskService taskServiceMock = Mockito.mock(TaskService.class);

        // Call the method being tested
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskServiceMock);

        // Verify the results
        Mockito.verify(taskServiceMock, Mockito.times(0)).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testOnProgressUpdate() {
        // Prepare the mocks and arguments
        Long taskId = 1L;
        List<ScheduleTaskEntity> tasks = null;
        Page<ScheduleTaskEntity> page = Mockito.mock(Page.class);
        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(page);
        Mockito.when(page.getSize()).thenReturn(2);
        Mockito.when(page.getContent()).thenReturn(tasks);
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
        onlineSchemaChangeFlowableTask.setScheduleId(1L);
        onlineSchemaChangeFlowableTask.setCreatorId(1L);
        onlineSchemaChangeFlowableTask.setOrganizationId(1L);
        onlineSchemaChangeFlowableTask.setContinueOnError(true);
        onlineSchemaChangeFlowableTask.setPercentage(0.5);

        // Call the method being tested
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskServiceMock);

        // Verify the results
        Mockito.verify(taskServiceMock, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
        TaskEntity taskEntity = Mockito.any(TaskEntity.class);
        assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
        assertEquals(new OnlineSchemaChangeTaskResult(tasks), taskEntity.getResultJson());
        assertEquals(1, taskEntity.getProgressPercentage());

        Mockito.verify(onlineSchemaChangeFlowableTask).setPercentage(1.0);
    }

    @Test
    public void testCancel() {
        // Prepare the mock objects and arguments
        Long taskId = 1L;
        boolean mayInterruptIfRunning = true;
        TaskService taskServiceMock = Mockito.mock(TaskService.class);

        // Call the method being tested
        boolean result = onlineSchemaChangeFlowableTask.cancel(mayInterruptIfRunning, taskId, taskServiceMock);

        // Verify the results
        assertTrue(result);
        Mockito.verify(taskServiceMock, Mockito.times(1)).cancel(taskId);
    }

    @Test
    public void testIsCancelled() {
        // Prepare the mock object
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

        // Call the method being tested
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Verify the results
        assertTrue(result);
    }
}