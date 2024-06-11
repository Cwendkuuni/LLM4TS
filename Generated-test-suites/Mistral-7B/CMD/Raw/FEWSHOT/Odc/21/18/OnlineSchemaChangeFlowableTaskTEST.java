package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.cli.OnlineSchemaChangeParameters;
import org.apache.commons.cli.OnlineSchemaChangeScheduleTaskParameters;
import org.apache.commons.cli.ScheduleEntity;
import org.apache.commons.cli.ScheduleTaskEntity;
import org.apache.commons.cli.TriggerStrategy;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.JobType;
import com.oceanbase.odc.core.shared.constant.ScheduleStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.task.TaskService;

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
    private TaskRepository taskRepository;
    @Mock
    private OrganizationService organizationService;
    @Mock
    private TaskService taskService;
    @Mock
    private BaseODCFlowTaskDelegate<Void> baseODCFlowTaskDelegate;
    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(scheduleService.create(any(ScheduleEntity.class))).thenAnswer(new Answer<ScheduleEntity>() {
            @Override
            public ScheduleEntity answer(Mjava.lang.Object o) {
                return ((ScheduleEntity) o);
            }
        });
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenAnswer(new Answer<ScheduleTaskEntity>() {
            @Override
            public ScheduleTaskEntity answer(Mjava.lang.Object o) {
                return ((ScheduleTaskEntity) o);
            }
        });
    }

    @Test
    public void testStart() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "testSchema";
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);

        OnlineSchemaChangeContextHolder.clear();

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema);
        verify(scheduleService, times(1)).create(schedule);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameter.generateSubTaskParameters(connectionConfig, schema);
        List<ScheduleTaskEntity> tasks = subTaskParameters.stream().map(param -> onlineSchemaChangeFlowableTask.createScheduleTaskEntity(schedule.getId(), param))
                .collect(Collectors.toList());

        verify(scheduleTaskRepository, times(subTaskParameters.size())).saveAndFlush(any(ScheduleTaskEntity.class));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testIsSuccessful() {
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testProgressUpdate() {
        long taskId = 1L;
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(scheduleTaskService.listTask(any(Pageable.class), eq(onlineSchemaChangeFlowableTask.getScheduleId()))).thenReturn(tasks);
        OnlineSchemaChangeFlowableTask testInstance = spy(onlineSchemaChangeFlowableTask);

        testInstance.progressStatusUpdate(tasks);

        verify(testInstance, times(1)).singleTaskPercentage(any(ScheduleTaskEntity.class));
        // Verify that the status is updated correctly
        TaskEntity flowTask = mock(TaskEntity.class);
        when(taskService.detail(taskId)).thenReturn(flowTask);
        when(flowTask.getStatus()).thenReturn(TaskStatus.RUNNING);
        testInstance.onProgressUpdate(taskId, taskService);

        verify(flowTask, times(1)).setStatus(eq(TaskStatus.DONE));
        verify(flowTask, times(1)).setProgressPercentage(anyInt());
        verify(flowTask, times(1)).setResultJson(anyString());
    }
}