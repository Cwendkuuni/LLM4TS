package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;
import com.oceanbase.odc.service.task.model.TaskStatusUpdate;

@SuppressWarnings("unchecked")
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

    @Mock
    private BaseODCFlowTaskDelegate<Void> mockDelegate;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private TaskEntity mockTaskEntity;

    @Mock
    private TaskService taskService;

    @Mock
    private ProcessInstance mockProcessInstance;

    @Mock
    private DelegateExecution mockExecution;

    @Mock
    private User mockUser;

    @Mock
    private OnlineSchemaChangeParameters mockParameters;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() {
        when(mockDelegate.start(anyLong(), eq(taskService), eq(mockExecution))).thenReturn(null);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(mockExecution.getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameters));

        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionConfig.id()).thenReturn(1L);
        when(mockExecution.getVariable("ConnectionConfig")).thenReturn(connectionConfig);

        when(mockExecution.getProcessInstance()).thenReturn(mockProcessInstance);
        when(mockProcessInstance.getId()).thenReturn(1L);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskService taskService = invocation.getArgument(1);
                taskService.start(1L);
                return null;
            }
        }).when(taskService).start(anyLong());

        when(mockUser.getId()).thenReturn(1L);
        when(mockExecution.getVariable("taskCreator")).thenReturn(mockUser);

        when(organizationService.findById(1L)).thenReturn(mockUser);
        when(mockExecution.getVariable("OrganizationService")).thenReturn(organizationService);

        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.clear();

        onlineSchemaChangeFlowableTask.start(1L, taskService, mockExecution);

        verify(mockDelegate, times(1)).start(1L, taskService, mockExecution);
        verify(taskService, times(1)).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(mockExecution.getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameters));

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.RUNNING);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.DONE);

        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.FAILED);

        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(mockExecution.getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameters));

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.RUNNING);

        assertFalse(onlineSchemaChangeFlowableTask.isFailure());

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.FAILED);

        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(mockExecution.getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameters));

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.RUNNING);

        doThrow(new RuntimeException("Test Exception")).when(onlineSchemaChangeFlowableTask).onFailure(anyLong(), eq(taskService));

        TaskResult result = new TaskResult();
        TaskStatusUpdate statusUpdate = new TaskStatusUpdate();
        statusUpdate.setStatus(TaskStatus.FAILED);
        result.setStatusUpdate(statusUpdate);

        when(taskService.detail(anyLong())).thenReturn(mockTaskEntity);
        when(mockTaskEntity.getResult()).thenReturn(result);

        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        verify(mockTaskEntity, times(1)).setResult(result);
    }

    @Test
    public void testOnSuccessful() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(mockExecution.getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameters));

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.RUNNING);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskService taskService = invocation.getArgument(1);
                TaskStatusUpdate statusUpdate = new TaskStatusUpdate();
                statusUpdate.setStatus(TaskStatus.DONE);
                taskService.update(mockTaskEntity, statusUpdate);
                return null;
            }
        }).when(onlineSchemaChangeFlowableTask).onSuccessful(anyLong(), eq(taskService));

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.DONE);

        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.clear();

        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

        verify(taskService, times(1)).update(mockTaskEntity, argThat(new ArgumentCaptor<TaskStatusUpdate>() {
            @Override
            public boolean matches(TaskStatusUpdate argument) {
                return argument.getStatus() == TaskStatus.DONE;
            }
        }));
        verify(mockTaskEntity, times(1)).setStatus(TaskStatus.DONE);
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(mockExecution.getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameters));

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.RUNNING);

        onlineSchemaChangeFlowableTask.onTimeout(anyLong(), eq(taskService));

        verify(mockExecution, times(1)).setVariable("status", TaskStatus.FAILED);
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(mockExecution.getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameters));

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.RUNNING);

        when(scheduleTaskRepository.listTask(any(Pageable.class), eq(1L))).thenReturn(Page.empty());
        when(taskHandler.terminate(anyLong(), anyLong())).thenReturn(true);

        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        OnlineSchemaChangeContextHolder.clear();

        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        verify(mockExecution, times(1)).setVariable("status", TaskStatus.CANCELED);
        verify(taskHandler, times(1)).terminate(1L, 1L);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(mockExecution.getVariables()).thenReturn(Map.of("OnlineSchemaChangeParameters", parameters));

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.RUNNING);

        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());

        when(mockExecution.getVariable("status")).thenReturn(TaskStatus.CANCELED);

        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";

        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameters, schema);

        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
        assertEquals(1L, scheduleEntity.getConnectionId());
        assertEquals(schema, scheduleEntity.getDatabaseName());
        assertEquals(TaskStatus.ENABLED, scheduleEntity.getStatus());
        assertEquals(1L, scheduleEntity.getCreatorId());
        assertEquals(1L, scheduleEntity.getOrganizationId());
        assertEquals(1L, scheduleEntity.getProjectId());
        assertEquals(1L, scheduleEntity.getDatabaseId());
        assertEquals(JsonUtils.toJson(parameters), scheduleEntity.getJobParametersJson());
        assertEquals("0/10 * * * * ?", scheduleEntity.getTriggerConfigJson());
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING, scheduleEntity.getMisfireStrategy());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param);

        assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
        assertEquals(scheduleId, scheduleTaskEntity.getScheduleId());
        assertEquals(param, JsonUtils.parse(scheduleTaskEntity.getParametersJson(), OnlineSchemaChangeScheduleTaskParameters.class));
        assertEquals("", scheduleTaskEntity.getResultJson());
        assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.0);
    }
}