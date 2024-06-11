
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.TaskListenerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private DelegateExecution execution;
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

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() throws Exception {
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
        User user = new User();
        user.setId(1L);
        user.setOrganizationId(1L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
        Long taskId = 2L;
        onlineSchemaChangeFlowableTask.start(taskId, new TaskService(), execution);
        assertNotNull(onlineSchemaChangeFlowableTask);
    }

    @Test
    public void testOnProgressUpdate() {
        TaskService taskService = mock(TaskService.class);
        Pageable pageable = mock(Pageable.class);
        ScheduleTaskEntity entity1 = new ScheduleTaskEntity();
        List<ScheduleTaskEntity> scheduleTaskEntities = Arrays.asList(entity1);
        Page<ScheduleTaskEntity> tasks = new PageImpl<>(scheduleTaskEntities);
        when(scheduleTaskService.listTask(pageable, 1L)).thenReturn(tasks);
        onlineSchemaChangeFlowableTask.onProgressUpdate(2L, taskService);
    }

    @Test
    public void testIsSuccessful() {
        assertNotNull(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertNotNull(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        TaskService taskService = new TaskService();
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
    }

    @Test
    public void testOnSuccessful() {
        TaskService taskService = new TaskService();
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
    }

    @Test
    public void testOnTimeout() {
        TaskService taskService = new TaskService();
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
    }

    @Test
    public void testCancel() {
        TaskService taskService = new TaskService();
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
    }

    @Test
    public void testIsCancelled() {
        assertNotNull(onlineSchemaChangeFlowableTask.isCancelled());
    }

}
