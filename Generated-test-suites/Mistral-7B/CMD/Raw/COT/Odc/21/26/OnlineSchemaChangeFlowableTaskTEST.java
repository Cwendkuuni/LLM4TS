package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.FlowInstanceEntity;
import com.oceanbase.odc.service.task.model.TaskInstanceEntity;

@Slf4j
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
    private TaskService taskService;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private TaskInstanceEntity taskInstance;

    @Mock
    private ProcessInstance processInstance;

    @Mock
    private DelegateExecution execution;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(execution.getProcessInstanceId()).thenReturn(processInstance.getId());
        when(processInstance.getId()).thenReturn(1L);
        when(processInstance.getBusinessKey()).thenReturn("testBusinessKey");
        when(taskService.detail(anyLong())).thenReturn(taskInstance);
        when(taskService.start(anyLong())).thenReturn(null);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        when(taskInstance.getExecution().getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameter));
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
        verify(quartzJobService, times(1)).start(anyLong(), anyLong());
        verifyNoMoreInteractions(quartzJobService);
    }

    @Test
    public void testIsSuccessful() {
        TaskStatus status = TaskStatus.DONE;
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
        TaskStatus statusFailed = TaskStatus.FAILED;
        assertEquals(false, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        TaskStatus status = TaskStatus.FAILED;
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
        TaskStatus statusDone = TaskStatus.DONE;
        assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        doNothing().when(taskService).cancel(anyLong(), anyBoolean());
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        verify(taskService, times(1)).cancel(1L, true);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(taskService, times(1)).update(taskInstance);
    }

    @Test
    public void testOnTimeout() throws Exception {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
        verifyNoInteractions(taskService);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(Arrays.asList(createScheduleTaskEntity(1L), createScheduleTaskEntity(2L)));
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        verify(onlineSchemaChangeFlowableTask, times(1)).percentage = (double) 50;
        TaskStatus dbStatus = TaskStatus.DONE;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        TaskEntity flowTask = new TaskEntity();
        flowTask.setStatus(dbStatus);
        when(taskService.detail(anyLong())).thenReturn(flowTask);
        onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);
        verify(taskService, times(1)).update(taskInstance);
    }

    @Test
    public void testCancel() throws Exception {
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
        verify(taskService, times(1)).cancel(1L, true);
        verify(taskService, times(1)).update(taskInstance);
        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        TaskStatus taskStatus = TaskStatus.CANCELED;
        assertEquals(true, onlineSchemaChangeFlowableTask.isCancelled());
        TaskStatus taskStatusNotCancelled = TaskStatus.DONE;
        assertEquals(false, onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        verify(scheduleService, times(1)).create(scheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param);
        verify(scheduleTaskRepository, times(1)).saveAndFlush(scheduleTaskEntity);
    }

    private ScheduleTaskEntity createScheduleTaskEntity(Long scheduleId) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        return scheduleTaskEntity;
    }

    private void updateFlowInstanceStatus(FlowStatus flowStatus) {
        TaskInstanceEntity taskInstanceEntity = new TaskInstanceEntity();
        ProcessInstance processInstanceEntity = new ProcessInstance();
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceEntity.getId());
        flowInstanceEntity.setFlowInstanceStatus(flowStatus);
        when(taskInstance.getProcessInstance()).thenReturn(processInstanceEntity);
        when(processInstance.getBusinessKey()).thenReturn("testBusinessKey");
        when(FlowTaskUtil.getTaskInstance(processInstanceEntity.getId())).thenReturn(taskInstanceEntity);
        when(taskService.detail(processInstanceEntity.getId())).thenReturn(flowInstanceEntity);
        onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(flowStatus);
        verify(taskService, times(1)).update(flowInstanceEntity);
    }
}