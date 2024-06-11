import org.apache.commons.collections4.OptionalUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig(classes = OnlineSchemaChangeFlowableTask.class)
@ActiveProfiles("test")
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    @Qualifier("scheduleTaskRepository")
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    @Qualifier("scheduleService")
    private ScheduleService scheduleService;

    @Mock
    @Qualifier("scheduleTaskService")
    private ScheduleTaskService scheduleTaskService;

    @Mock
    @Qualifier("quartzJobService")
    private QuartzJobService quartzJobService;

    @Mock
    @Qualifier("taskHandler")
    private OscTaskCompleteHandler taskHandler;

    @Mock
    @Qualifier("completeHandler")
    private OscTaskCompleteHandler completeHandler;

    @Mock
    @Qualifier("organizationService")
    private OrganizationService organizationService;

    @Mock
    @Qualifier("taskService")
    private TaskService taskService;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        onlineSchemaChangeScheduleTaskParameters = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
        scheduleEntity = Mockito.mock(ScheduleEntity.class);
        scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(connectionConfig.id()).thenReturn(1L);
        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema")).thenReturn(
                List.of(onlineSchemaChangeScheduleTaskParameters));
    }

    @Test
    public void testStart() throws Exception {
        ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
        when(taskService.detail(anyLong())).thenReturn(processInstance);

        when(scheduleService.create(any()))
                .thenAnswer(new Answer<ScheduleEntity>() {
                    @Override
                    public ScheduleEntity answer(ScheduleEntity scheduleEntity) throws Exception {
                        return scheduleEntity;
                    }
                });

        when(scheduleTaskRepository.saveAndFlush(any()))
                .thenAnswer(new Answer<ScheduleTaskEntity>() {
                    @Override
                    public ScheduleTaskEntity answer(ScheduleTaskEntity scheduleTaskEntity) throws Exception {
                        return scheduleTaskEntity;
                    }
                });

        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getId()).thenReturn(1L);
        when(execution.getProcessInstanceId()).thenReturn(1L);

        Executable start = () -> testInstance.start(1L, taskService, execution);

        Mockito.verify(scheduleService).create(eq(connectionConfig));

        start.execute();
        verify(taskService).start(1L);
        verify(taskHandler).start(scheduleId.eq(scheduleEntity.getId()), eq(onlineSchemaChangeScheduleTaskParameters.getId()));
        verify(quartzJobService).createJob(any(), any(), any());
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, testInstance.isSuccessful(1L, taskService, taskEntity));
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

        assertEquals(TaskStatus.FAILED, testInstance.isFailure(1L, taskService, taskEntity));
    }

    @Test
    public void testOnFailure() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        Executable onFailure = () -> testInstance.onFailure(1L, taskService);

        onFailure.execute();
        verify(log).warn("Online schema change task failed, taskId={}", 1L);
    }

    @Test
    public void testOnSuccessful() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        Executable onSuccessful = () -> testInstance.onSuccessful(1L, taskService);

        onSuccessful.execute();
        verify(taskService).update(any());
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        Executable onTimeout = () -> testInstance.onTimeout(1L, taskService);

        onTimeout.execute();
        verify(log).warn("Online schema change task timeout, taskId={}", 1L);
    }

    @Test
    public void testOnProgressUpdate() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;
        Page<ScheduleTaskEntity> scheduleTasks = Mockito.mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId.eq(1L))).thenReturn(scheduleTasks);

        when(scheduleTasks.getSize()).thenReturn(3);
        when(scheduleTasks.getContent()).thenReturn(List.of(scheduleTaskEntity, scheduleTaskEntity, scheduleTaskEntity));

        when(singleTaskPercentage(scheduleTaskEntity)).thenReturn(0.5);

        Executable onProgressUpdate = () -> testInstance.onProgressUpdate(1L, taskService);

        onProgressUpdate.execute();
        verify(taskService).update(any());
        verify(flowTask).setProgressPercentage(anyDouble());
    }

    @Test
    public void testCancel() throws Exception {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        Executable cancel = () -> testInstance.cancel(true, 1L, taskService);

        cancel.execute();
        verify(taskService).cancel(anyLong());
        verify(scheduleTaskRepository).deleteAll(any());
        verify(organizationService).removeUserSchemaAccess(any(), any());
        verify(quartzJobService).deleteJob(anyLong(), any());
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.CANCELED);

        assertTrue(testInstance.isCancelled(taskEntity));
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        Executable createScheduleEntity = () -> testInstance.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schema");

        createScheduleEntity.execute();
        verify(scheduleService).create(any());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        Executable createScheduleTaskEntity = () -> testInstance.createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters);

        createScheduleTaskEntity.execute();
        verify(scheduleTaskRepository).saveAndFlush(any());
    }

    @Test
    public void testSingleTaskPercentage() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

        double result = testInstance.singleTaskPercentage(scheduleTaskEntity);

        assertEquals(0.5, result);
    }

    @Test
    public void testProgressStatusUpdate() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;
        Page<ScheduleTaskEntity> scheduleTasks = Mockito.mock(Page.class);

        when(scheduleTasks.getContent()).thenReturn(List.of(scheduleTaskEntity, scheduleTaskEntity, scheduleTaskEntity));

        testInstance.progressStatusUpdate(scheduleTasks);

        verify(testInstance).status = TaskStatus.DONE;
    }

    @Test
    public void testCancelScheduleTasks() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        Executable cancelScheduleTasks = () -> testInstance.cancelScheduleTasks(scheduleId);

        cancelScheduleTasks.execute();
        verify(taskHandler).terminate(scheduleId, anyLong());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        OnlineSchemaChangeFlowableTask testInstance = onlineSchemaChangeFlowableTask;

        Executable updateFlowInstanceStatus = () -> testInstance.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

        updateFlowInstanceStatus.execute();
        verify(taskService).update(any());
    }
}