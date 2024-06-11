import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.Optional;

public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = createTaskEntity();
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);
        // simulation for a specific condition where dataArchiveTask is not found
    }

    @Test
    public void testExecute_dataArchiveTaskNotFinished() {
        ScheduleTaskEntity taskEntity = createTaskEntity();
        ScheduleTaskEntity dataArchiveTask = createTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.PREPARING);
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);
        // condition where dataArchiveTask is not finished
    }
    
    // Add more test cases as needed to cover all corner cases

    private ScheduleTaskEntity createTaskEntity() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId("testId");
        taskEntity.setResult(parameters);
        return taskEntity;
    }

}