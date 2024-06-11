package com.oceanbase.odc.service.onlineschemachange.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.common.json.JsonUtils;
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
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskResult;
import com.oceanbase.odc.service.task.TaskService;

@Slf4j
@ExtendWith(MockitoExtension.class)
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
    private OscTaskCompleteHandler taskHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testee;

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig connectionConfigMock = mock(ConnectionConfig.class);
        String schema = "schema1";
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        when(execution.getVariables()).thenReturn(Map.of("connectionConfig", connectionConfigMock));
        when(connectionConfigMock.getId()).thenReturn(1L);
        when(connectionConfigMock.getName()).thenReturn("connectionName");
        when(execution.getBusinessKey()).thenReturn("businessKey");
        when(execution.getId()).thenReturn(1L);
        when(execution.getProcessInstance()).thenReturn(createMockProcessInstance());
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(quartzJobService.addJob(anyString(), anyString(), anyString(), anyString(), anyList(), any(TriggerConfig.class), any(MisfireStrategy.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(1));

        testee.start(1L, mock(TaskService.class), execution);

        Assertions.assertNull(testee.getStatus());
        Assertions.assertNull(testee.getScheduleId());
        Assertions.assertEquals(1L, testee.getCreatorId());
        Assertions.assertEquals(1L, testee.getOrganizationId());
        Assertions.assertFalse(testee.isContinueOnError());
        Assertions.assertEquals(0.0, testee.getPercentage());
    }

    @Test
    public void testIsSuccessful() {
        testee.setStatus(TaskStatus.DONE);
        Assertions.assertTrue(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        testee.setStatus(TaskStatus.FAILED);
        Assertions.assertTrue(testee.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskService taskServiceMock = mock(TaskService.class);
        testee.onFailure(taskId, taskServiceMock);
        Assertions.assertFalse(testee.isSuccessful());
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskService taskServiceMock = mock(TaskService.class);
        testee.onSuccessful(taskId, taskServiceMock);
        Assertions.assertTrue(testee.isSuccessful());
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskService taskServiceMock = mock(TaskService.class);
        testee.onTimeout(taskId, taskServiceMock);
        Assertions.assertFalse(testee.isSuccessful());
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService taskServiceMock = mock(TaskService.class);
        ScheduleTaskService scheduleTaskServiceMock = mock(ScheduleTaskService.class);
        List<ScheduleTaskEntity> tasks = List.of(new ScheduleTaskEntity());
        when(scheduleTaskServiceMock.listTask(any(Pageable.class), eq(testee.getScheduleId()))).thenReturn(new PageImpl<>(tasks));

        testee.onProgressUpdate(taskId, taskServiceMock);

        Assertions.assertEquals(TaskStatus.RUNNING, testee.getStatus());
        Assertions.assertEquals(1L, testee.getScheduleId());
        Assertions.assertEquals(1L, testee.getCreatorId());
        Assertions.assertEquals(1L, testee.getOrganizationId());
        Assertions.assertFalse(testee.isContinueOnError());
        Assertions.assertEquals(0.0, testee.getPercentage());
    }

    @Test
    public void testCancel() {
        boolean mayInterruptIfRunning = false;
        Long taskId = 1L;
        TaskService taskServiceMock = mock(TaskService.class);
        testee.cancel(mayInterruptIfRunning, taskId, taskServiceMock);
        Assertions.assertTrue(testee.isCancelled());
    }

    @Test
    public void testIsCancelled() {
        testee.setStatus(TaskStatus.CANCELED);
        Assertions.assertTrue(testee.isCancelled());
    }

    private ProcessInstance createMockProcessInstance() {
        return mock(ProcessInstance.class);
    }
}