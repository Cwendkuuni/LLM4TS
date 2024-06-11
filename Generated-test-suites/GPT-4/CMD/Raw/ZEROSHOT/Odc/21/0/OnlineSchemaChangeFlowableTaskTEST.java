
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.flowable.engine.delegate.DelegateExecution;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private TaskService taskService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private DelegateExecution execution;
    @Mock
    private User creator;
    @Mock
    private ScheduleEntity schedule;
    
    OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

    @BeforeEach
    public void setup() {
        task.scheduleTaskRepository = scheduleTaskRepository;
        task.taskService = taskService;
    }

    @Test
    public void testStart() throws Exception {
        when(creator.getId()).thenReturn(1L);
        when(execution.getVariable("taskCreator")).thenReturn(creator);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        parameter.setContinueOnError(true);
        when(execution.getVariable("onlineSchemaChangeParameter")).thenReturn(parameter);

        task.start(1L, taskService, execution);
        verify(taskService, times(1)).start(any(Long.class));
    }

    @Test
    public void testIsSuccessful() {
        task.status = TaskStatus.DONE;
        Assertions.assertTrue(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        task.status = TaskStatus.FAILED;
        Assertions.assertTrue(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        task.onFailure(1L, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnSuccessful() {
        task.onSuccessful(1L, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() {
        task.onTimeout(1L, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testOnProgressUpdate() {
        task.onProgressUpdate(1L, taskService);
        verify(taskService, times(1)).update(any(TaskEntity.class));
    }

    @Test
    public void testCancel() {
        boolean result = task.cancel(true, 1L, taskService);
        verify(taskService, times(1)).cancel(any(Long.class));
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsCancelled() {
        task.status = TaskStatus.CANCELED;
        Assertions.assertTrue(task.isCancelled());
    }

}
