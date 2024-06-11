import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.TaskStatus;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private TaskService taskService;

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
    }

    @Test
    public void testProgressStatusUpdate() {
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        ScheduleTaskEntity task1 = new ScheduleTaskEntity();
        task1.setStatus(TaskStatus.DONE);
        ScheduleTaskEntity task2 = new ScheduleTaskEntity();
        task2.setStatus(TaskStatus.RUNNING);
        tasks.add(task1);
        tasks.add(task2);

        onlineSchemaChangeFlowableTask.progressStatusUpdate(new Page<>(tasks, Pageable.unpaged()));

        assert onlineSchemaChangeFlowableTask.getStatus() == TaskStatus.RUNNING;
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.RUNNING);
        scheduleTask.setProgressPercentage(50.0);

        double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTask);

        assert percentage == 0.5;
    }

    // Add more test methods for other functionalities of OnlineSchemaChangeFlowableTask

}