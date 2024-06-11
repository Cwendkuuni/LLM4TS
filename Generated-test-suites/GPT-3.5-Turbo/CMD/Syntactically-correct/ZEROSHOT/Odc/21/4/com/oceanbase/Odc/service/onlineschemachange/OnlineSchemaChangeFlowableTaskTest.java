import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
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
    private OrganizationService organizationService;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private TaskService taskService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        DelegateExecution execution = mock(DelegateExecution.class);
        User user = new User(1L, "test", 1L);
        when(execution.getVariable("creator")).thenReturn(user);
        when(execution.getVariable("uid")).thenReturn("123");
        when(execution.getVariable("parameters")).thenReturn(new OnlineSchemaChangeParameters());
        when(execution.getVariable("connectionConfig")).thenReturn(new ConnectionConfig());
        when(execution.getVariable("schemaName")).thenReturn("test_schema");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);

        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        tasks.add(taskEntity);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new Page<ScheduleTaskEntity>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return tasks.size();
            }

            @Override
            public <U> Page<U> map(Function<? super ScheduleTaskEntity, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return tasks.size();
            }

            @Override
            public int getNumberOfElements() {
                return tasks.size();
            }

            @Override
            public List<ScheduleTaskEntity> getContent() {
                return tasks;
            }

            @Override
            public boolean hasContent() {
                return !tasks.isEmpty();
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return true;
            }

            @Override
            public boolean isLast() {
                return true;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<ScheduleTaskEntity> iterator() {
                return tasks.iterator();
            }
        });

        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);
        verify(taskService, times(1)).start(taskId);
        verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
        verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), anyLong());
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());

        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);
        when(taskService.detail(taskId)).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        assertEquals(FlowStatus.EXECUTION_SUCCEEDED, taskEntity.getStatus());
        verify(taskService, times(1)).update(taskEntity);
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.FAILED);
        when(taskService.detail(taskId)).thenReturn(taskEntity);

        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        verify(taskService, times(1)).update(taskEntity);
    }

    @Test
    public void testOnTimeout() {
        Long taskId = 1L;
        
        onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

        // No specific logic to test, just checking if the method runs without errors
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.RUNNING);
        when(taskService.detail(taskId)).thenReturn(taskEntity);
        
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        ScheduleTaskEntity task1 = new ScheduleTaskEntity();
        task1.setStatus(TaskStatus.DONE);
        task1.setProgressPercentage(50.0);
        ScheduleTaskEntity task2 = new ScheduleTaskEntity();
        task2.setStatus(TaskStatus.RUNNING);
        task2.setProgressPercentage(30.0);
        tasks.addAll(Arrays.asList(task1, task2));
        
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new Page<ScheduleTaskEntity>() {
            // Implement necessary methods
        });

        onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

        // Assert some conditions based on the logic in the method
    }

    @Test
    public void testCancel() {
        Long taskId = 1L;
        
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService));
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
        verify(taskService, times(1)).cancel(taskId);
    }

}