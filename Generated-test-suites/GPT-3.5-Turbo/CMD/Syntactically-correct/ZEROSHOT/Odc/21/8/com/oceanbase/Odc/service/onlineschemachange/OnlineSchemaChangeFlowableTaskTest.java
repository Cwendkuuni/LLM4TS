import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

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
    public void testStart() throws Exception {
        // Arrange
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

        // Act
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // Assert
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
        assertTrue(onlineSchemaChangeFlowableTask.getCreatorId() > 0);
    }

    @Test
    public void testIsSuccessful() {
        // Act
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testIsFailure() {
        // Act
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Assert
        assertEquals(false, result);
    }

    @Test
    public void testOnProgressUpdate() {
        // Arrange
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        List<ScheduleTaskEntity> taskList = new ArrayList<>();
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setStatus(TaskStatus.RUNNING);
        task.setProgressPercentage(50.0);
        taskList.add(task);
        when(tasks.getSize()).thenReturn(taskList.size());
        when(tasks.getContent()).thenReturn(taskList);
        when(scheduleTaskService.listTask(any(), anyLong())).thenReturn(tasks);

        // Act
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Assert
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testCancel() {
        // Arrange
        when(scheduleTaskService.listTask(any(), anyLong())).thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // Assert
        assertEquals(true, result);
        assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
    }

    @Test
    public void testIsCancelled() {
        // Act
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Assert
        assertEquals(false, result);
    }

}