import com.oceanbase.odc.service.onlineschemachange.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @Mock
    private DelegateExecution execution;
    
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.scheduleTaskRepository = scheduleTaskRepository;
        onlineSchemaChangeFlowableTask.scheduleService = scheduleService;
        onlineSchemaChangeFlowableTask.scheduleTaskService = scheduleTaskService;
        onlineSchemaChangeFlowableTask.quartzJobService = quartzJobService;
        onlineSchemaChangeFlowableTask.taskHandler = taskHandler;
        onlineSchemaChangeFlowableTask.completeHandler = completeHandler;
        onlineSchemaChangeFlowableTask.organizationService = organizationService;
    }

    @Test
    public void testStart() throws Exception {
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User());
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("test");
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(new OnlineSchemaChangeParameters());
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(new ConnectionConfig());
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schema");
        when(scheduleService.create(any())).thenReturn(new ScheduleEntity());
        doNothing().when(taskHandler).start(anyLong(), anyLong());

        onlineSchemaChangeFlowableTask.start(1L, new TaskService(), execution);
        verify(taskHandler, times(1)).start(anyLong(), anyLong());
    }


    // Other test methods here...
}