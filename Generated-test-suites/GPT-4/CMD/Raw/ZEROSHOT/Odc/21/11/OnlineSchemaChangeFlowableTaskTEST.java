
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
    private DelegateExecution delegateExecution;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private long taskId = 123L;

    @Before
    public void setUp() throws Exception {
        when(taskService.start(any(Long.class))).thenReturn(null);
    }

    @Test
    public void testStart() throws Exception {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setOrganizationId(1L);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setContinueOnError(true);
        
        Mockito.when(FlowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(testUser);
        Mockito.when(FlowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(onlineSchemaChangeParameters);
        Mockito.when(FlowTaskUtil.getConnectionConfig(delegateExecution)).thenReturn(connectionConfig);
        Mockito.when(FlowTaskUtil.getSchemaName(delegateExecution)).thenReturn("schema");
        Mockito.when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());

        onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);
    }

    @Test
    public void testIsSuccessful() {
        when(onlineSchemaChangeFlowableTask.isSuccessful()).thenReturn(true);
    }

    @Test
    public void testIsFailure() {
        when(onlineSchemaChangeFlowableTask.isFailure()).thenReturn(false);
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        PageImpl<ScheduleTaskEntity> tasks = new PageImpl<>(new ArrayList<>());
        when(scheduleTaskService.listTask(any(Pageable.class), any(Long.class))).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);
    }

    @Test
    public void testCancel() {
        ScheduleTaskEntity runningEntity = new ScheduleTaskEntity();
        runningEntity.setStatus(TaskStatus.RUNNING);
        ArrayList<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(runningEntity);

        PageImpl<ScheduleTaskEntity> tasksPage = new PageImpl<>(tasks);
        when(scheduleTaskService.listTask(any(Pageable.class), any(Long.class))).thenReturn(tasksPage);
        when(taskService.cancel(any(Long.class))).thenReturn(null);

        onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);
    }

    @Test
    public void testIsCancelled() {
        when(onlineSchemaChangeFlowableTask.isCancelled()).thenReturn(false);
    }
}
