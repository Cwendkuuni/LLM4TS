import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

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
    private TaskService taskService;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
        onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        DelegateExecution execution = mock(DelegateExecution.class);
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("mockedId");
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameters);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schema");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
        List<OnlineSchemaChangeScheduleTaskParameters> tasks = Arrays.asList(mock(OnlineSchemaChangeScheduleTaskParameters.class));
        when(organizationService.trace(user.getId(), taskId, user.getOrganizationId())).thenReturn(true);
        when(parameters.generateSubTaskParameters(eq(connectionConfig), eq("schema"))).thenReturn(tasks);

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        verify(taskService, times(1)).start(taskId);
        verify(scheduleService, times(1)).create(scheduleEntity);
        verify(taskHandler, times(1)).start(scheduleEntity.getId(), tasks.get(0).getId());
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.getStatus = TaskStatus.DONE;
        assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.getStatus = TaskStatus.FAILED;
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        // Add specific assertions here
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        // Add specific assertions here
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
        // Add specific assertions here
    }

    @Test
    public void testOnProgressUpdate() {
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(scheduleTaskService.listTask(Pageable.unpaged(), onlineSchemaChangeFlowableTask.getScheduleId())).thenReturn(tasks);
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
        // Add specific assertions here
    }

    @Test
    public void testCancel() {
        // Add specific assertions here
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.getStatus = TaskStatus.CANCELED;
        assertEquals(true, onlineSchemaChangeFlowableTask.isCancelled());
    }

}