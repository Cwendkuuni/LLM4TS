import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    OnlineSchemaChangeFlowableTask task;

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
    private DelegateExecution execution;
    @Mock
    private TaskService taskService;
    @Mock
    private ScheduleEntity scheduleEntity;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private OnlineSchemaChangeParameters parameter;
    @Mock
    private OnlineSchemaChangeScheduleTaskParameters scheduleTaskParameters;
    @Mock
    private Page<ScheduleTaskEntity> tasks;
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        task = new OnlineSchemaChangeFlowableTask();
        task.scheduleTaskRepository = scheduleTaskRepository;
        task.scheduleService = scheduleService;
        task.scheduleTaskService = scheduleTaskService;
        task.quartzJobService = quartzJobService;
        task.taskHandler = taskHandler;
        task.completeHandler = completeHandler;
        task.organizationService = organizationService;
    }

    @Test
    public void testStart() throws Exception {
        when(execution.getVariable(eq("taskId"))).thenReturn(1L);
        when(FlowTaskUtil.getTaskCreator(eq(execution))).thenReturn(new User(1L, "testUser", 1L));
        when(FlowTaskUtil.getCloudMainAccountId(eq(execution))).thenReturn("123456");
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(eq(execution))).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(eq(execution))).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(eq(execution))).thenReturn("testSchema");
        when(parameter.isContinueOnError()).thenReturn(false);
        when(task.createScheduleEntity(eq(connectionConfig), eq(parameter), eq("testSchema"))).thenReturn(scheduleEntity);
        when(scheduleEntity.getId()).thenReturn(1L);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);
        when(scheduleTaskService.listTask(isA(Pageable.class), eq(1L))).thenReturn(tasks);
        when(tasks.getSize()).thenReturn(1);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskEntity.getProgressPercentage()).thenReturn(50.0);
        
        task.start(1L, taskService, execution);

        verify(taskService, times(1)).start(1L);
        verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
        verify(taskHandler, times(1)).start(eq(1L), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        task.status = TaskStatus.DONE;
        assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.status = TaskStatus.FAILED;
        assertTrue(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        task.onFailure(1L, taskService);
        // Add your verification here
    }

    @Test
    public void testOnSuccessful() {
        task.onSuccessful(1L, taskService);
        // Add your verification here
    }

    @Test
    public void testOnTimeout() {
        task.onTimeout(1L, taskService);
        // Add your verification here
    }

    @Test
    public void testOnProgressUpdate() {
        // Add your test code here
    }

    @Test
    public void testCancel() {
        assertTrue(task.cancel(true, 1L, taskService));
        // Add your verification here
    }

    @Test
    public void testIsCancelled() {
        task.status = TaskStatus.CANCELED;
        assertTrue(task.isCancelled());
    }

    @Test
    public void testProgressStatusUpdate() {
        // Add your test code here
    }

    @Test
    public void testSingleTaskPercentage() {
        // Add your test code here
    }
}