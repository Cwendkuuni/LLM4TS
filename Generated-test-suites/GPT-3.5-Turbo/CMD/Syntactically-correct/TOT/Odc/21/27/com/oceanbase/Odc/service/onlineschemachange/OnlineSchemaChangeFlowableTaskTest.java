import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private TaskService taskService;

    @Mock
    private DelegateExecution execution;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private Long taskId = 1L;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleId(1L);
        onlineSchemaChangeFlowableTask.setCreatorId(1L);
        onlineSchemaChangeFlowableTask.setOrganizationId(1L);
        onlineSchemaChangeFlowableTask.setContinueOnError(true);
        onlineSchemaChangeFlowableTask.setPercentage(0.0);
    }

    @Test
    public void testStart() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);

        when(taskService.start(taskId)).thenReturn(taskId);
        when(execution.getVariable("creator")).thenReturn(user);
        when(execution.getVariable("parameter")).thenReturn(new OnlineSchemaChangeParameters());
        when(execution.getVariable("connectionConfig")).thenReturn(new ConnectionConfig());
        when(execution.getVariable("schema")).thenReturn("testSchema");
        when(organizationService.trace(1L, taskId, 1L)).thenReturn(true);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);

        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        List<OnlineSchemaChangeScheduleTaskParameters> paramList = new ArrayList<>();
        paramList.add(param);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(paramList));

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getScheduleId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getCreatorId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getOrganizationId());
    }

    // Add more test cases for other methods in the class

}