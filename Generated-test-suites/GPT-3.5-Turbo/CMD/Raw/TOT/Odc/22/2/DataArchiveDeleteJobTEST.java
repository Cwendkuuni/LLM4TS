import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_WhenDataArchiveTaskIsNotFound() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{ \"dataArchiveTaskId\": 123 }");
        when(scheduleTaskRepository.findById(123)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_WhenDataArchiveTaskIsNotDone() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{ \"dataArchiveTaskId\": 123 }");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(123)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    // Additional test cases can be added here for other scenarios

}