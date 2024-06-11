package com.oceanbase.odc.service.onlineschemachange;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.model.User;

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
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private OrganizationService organizationService;
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Test
    public void startTest() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        User creator = new User();
        creator.setId(1L);
        creator.setOrganizationId(1L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn("testId");
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("testSchema");
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(schedule.getId()).thenReturn(2L);
        when(scheduleService.create(any())).thenReturn(schedule);
        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);
        
        verify(taskService, times(1)).start(taskId);
    }

    // Add more tests such as isSuccessfulTest, isFailureTest etc...
}
