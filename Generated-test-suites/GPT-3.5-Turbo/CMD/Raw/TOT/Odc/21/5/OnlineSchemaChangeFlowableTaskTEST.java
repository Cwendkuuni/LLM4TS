import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
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
    private QuartzJobService quartzJobService;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private TaskService taskService;

    @Autowired
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
    }

    @Test
    public void testStart() throws Exception {
        // Setup
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());

        // Execution
        onlineSchemaChangeFlowableTask.start(1L, taskService, any(DelegateExecution.class));

        // Verification
        verify(taskService, times(1)).start(1L);
        verify(taskService, times(1)).detail(1L);
    }

    @Test
    public void testIsSuccessful() {
        // Setup
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

        // Execution
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Verification
        assertEquals(true, result);
    }

    @Test
    public void testIsFailure() {
        // Setup
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

        // Execution
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Verification
        assertEquals(true, result);
    }

    @Test
    public void testCancel() {
        // Setup
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(
                new PageImpl<>(Collections.singletonList(new ScheduleTaskEntity())));

        // Execution
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

        // Verification
        assertEquals(true, result);
    }

    @Test
    public void testOnProgressUpdate() {
        // Setup
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        List<ScheduleTaskEntity> tasks = Collections.singletonList(scheduleTaskEntity);
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(page);

        // Execution
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Verification
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testProgressStatusUpdate() {
        // Setup
        ScheduleTaskEntity successfulTask = new ScheduleTaskEntity();
        successfulTask.setStatus(TaskStatus.DONE);
        ScheduleTaskEntity failedTask = new ScheduleTaskEntity();
        failedTask.setStatus(TaskStatus.FAILED);
        List<ScheduleTaskEntity> tasks = Arrays.asList(successfulTask, failedTask);

        // Execution
        onlineSchemaChangeFlowableTask.progressStatusUpdate(new PageImpl<>(tasks));

        // Verification
        assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testIsCancelled() {
        // Setup
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

        // Execution
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Verification
        assertEquals(true, result);
    }
}