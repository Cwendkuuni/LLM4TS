//imports
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

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

    @Test
    public void testStart() {
        //Mock objects and setup data
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
        onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);

        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        //Test start method
        assertThrows(Exception.class, () -> {onlineSchemaChangeFlowableTask.start(null, taskService, execution);});
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        verify(taskService, times(1)).start(1L);
    }

    @Test
    public void testOnSuccessful() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(taskService, times(1)).detail(1L);
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.onTimeout(1L, null);
    }

    @Test
    public void testOnProgressUpdate() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        when(taskService.detail(1L)).thenReturn(new TaskEntity());
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        String schema = "schema";
        assertNotNull(onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, schema));
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        assertNotNull(onlineSchemaChangeFlowableTask.createScheduleTaskEntity(scheduleId, param));
    }

}