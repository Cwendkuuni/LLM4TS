import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskRepository mockScheduleTaskRepository;
    @Mock
    private ScheduleService mockScheduleService;
    @Mock
    private ScheduleTaskService mockScheduleTaskService;
    @Mock
    private QuartzJobService mockQuartzJobService;
    @Mock
    private OnlineSchemaChangeTaskHandler mockTaskHandler;
    @Mock
    private OscTaskCompleteHandler mockCompleteHandler;
    @Mock
    private OrganizationService mockOrganizationService;
    
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(mockScheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(mockScheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(mockScheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(mockQuartzJobService);
        onlineSchemaChangeFlowableTask.setTaskHandler(mockTaskHandler);
        onlineSchemaChangeFlowableTask.setCompleteHandler(mockCompleteHandler);
        onlineSchemaChangeFlowableTask.setOrganizationService(mockOrganizationService);
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        TaskService mockTaskService = mock(TaskService.class);
        DelegateExecution mockExecution = mock(DelegateExecution.class);
        User mockUser = mock(User.class);
        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getOrganizationId()).thenReturn(1L);
        when(mockExecution.getCurrentFlowableUserId()).thenReturn("1");
        when(mockExecution.getVariable("onlineSchemaChangeParameter")).thenReturn(mock(OnlineSchemaChangeParameters.class));
        when(mockExecution.getVariable("connectionConfig")).thenReturn(mockConnectionConfig);
        when(mockExecution.getVariable("schema")).thenReturn("testSchema");
        
        onlineSchemaChangeFlowableTask.start(taskId, mockTaskService, mockExecution);
        
        verify(mockTaskService).start(taskId);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskService mockTaskService = mock(TaskService.class);
        
        onlineSchemaChangeFlowableTask.onFailure(taskId, mockTaskService);
        
        verify(mockTaskService).onFailure(taskId);
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskService mockTaskService = mock(TaskService.class);
        
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, mockTaskService);
        
        verify(mockTaskService).onSuccessful(taskId);
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        TaskService mockTaskService = mock(TaskService.class);
        
        onlineSchemaChangeFlowableTask.onTimeout(taskId, mockTaskService);
        
        verify(mockTaskService).onTimeout(taskId);
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService mockTaskService = mock(TaskService.class);
        Page<ScheduleTaskEntity> mockTasks = mock(Page.class);
        
        when(mockScheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(mockTasks);
        
        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, mockTaskService);
        
        verify(mockTaskService).detail(taskId);
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;
        TaskService mockTaskService = mock(TaskService.class);
        
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, taskId, mockTaskService));
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}