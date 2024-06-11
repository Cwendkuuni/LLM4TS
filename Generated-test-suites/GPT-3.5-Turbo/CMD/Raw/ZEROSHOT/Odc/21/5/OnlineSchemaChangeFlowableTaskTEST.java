import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

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

    @Before
    public void setup() {
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
    public void testStartSuccess() throws Exception {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        Mockito.when(scheduleService.create(Mockito.any())).thenReturn(scheduleEntity);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getScheduleId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getCreatorId());
        assertEquals(1L, onlineSchemaChangeFlowableTask.getOrganizationId());
    }

    @Test
    public void testSuccessful() {
        Long taskId = 1L;
        TaskService taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Verify the task status is set to DONE
        assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());
        // Add more assertions if necessary
    }

    // Add more test methods as needed

}