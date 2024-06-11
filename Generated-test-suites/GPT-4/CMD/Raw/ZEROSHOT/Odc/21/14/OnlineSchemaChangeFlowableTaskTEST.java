
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {
    
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;
   
    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testStartMethod() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        User creator = mock(User.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameters = mock(OnlineSchemaChangeParameters.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        OnlineSchemaChangeScheduleTaskParameters param = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        List<OnlineSchemaChangeScheduleTaskParameters> parameterList = new ArrayList<>();
        parameterList.add(param);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        List<ScheduleTaskEntity> entityList = new ArrayList<>();
        entityList.add(taskEntity);

        when(FlowTaskUtil.getTaskCreator(eq(execution))).thenReturn(creator);
        when(FlowTaskUtil.getCloudMainAccountId(eq(execution))).thenReturn("uid");
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(eq(execution))).thenReturn(parameters);
        when(FlowTaskUtil.getConnectionConfig(eq(execution))).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(eq(execution))).thenReturn("schema");
        when(parameters.isContinueOnError()).thenReturn(true);
        when(parameters.generateSubTaskParameters(eq(connectionConfig), eq("schema"))).thenReturn(parameterList);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(taskEntity);
        when(taskHandler.start(anyLong(), anyLong())).thenAnswer(invocation -> null);

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);
        
        verify(taskService).start(taskId);
    }
    
    @Test
    public void testIsSuccessfulMethod() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        boolean isSuccessful = onlineSchemaChangeFlowableTask.isSuccessful();
        assertEquals(true, isSuccessful);

        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        isSuccessful = onlineSchemaChangeFlowableTask.isSuccessful();
        assertEquals(false, isSuccessful);
    }

    @Test
    public void testIsFailureMethod() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        boolean isFailure = onlineSchemaChangeFlowableTask.isFailure();
        assertEquals(true, isFailure);

        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        isFailure = onlineSchemaChangeFlowableTask.isFailure();
        assertEquals(false, isFailure);
    }

}
