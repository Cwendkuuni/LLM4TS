
package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
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
    private TaskService taskService;
    @Mock
    private OrganizationService organizationService;
    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() throws Exception {
        // Init Mocks here if required
    }

    @After
    public void tearDown() throws Exception {
        reset(scheduleTaskRepository, scheduleService, scheduleTaskService, quartzJobService, taskHandler, completeHandler, taskService, organizationService, execution);
    }

    @Test
    public void testStart() {
        Long taskId = 1L;
        User creator = mock(User.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
        List<OnlineSchemaChangeScheduleTaskParameters> parameters = new ArrayList<>();
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);

        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(parameter.generateSubTaskParameters(any(ConnectionConfig.class), any(String.class))).thenReturn(parameters);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(ticketService.activateTicket(any(Long.class))).thenReturn(true);

        Assert.assertNotNull(jobController.activateJob(1L));

        verify(ticketService, times(1)).activateTicket(1L);
    }

    // Add more test cases to cover all the methods
}
