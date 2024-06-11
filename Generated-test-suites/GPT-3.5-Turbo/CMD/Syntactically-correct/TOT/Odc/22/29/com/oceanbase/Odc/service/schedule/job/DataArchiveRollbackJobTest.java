import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.springframework.test.util.ReflectionTestUtils;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_Success() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        // add mock data for taskEntity and rollBackParameters
        ReflectionTestUtils.setField(taskEntity, "parametersJson", "{\"dataArchiveTaskId\":1}");
        when(context.getResult()).thenReturn(taskEntity);

        // mock data archive task
        ScheduleTaskEntity mockDataArchiveTask = new ScheduleTaskEntity();
        mockDataArchiveTask.setId(1L);
        // add mock data for mockDataArchiveTask
        List<DlmTask> taskUnits = new ArrayList<>();
        // add some mock taskUnits
        ReflectionTestUtils.setField(mockDataArchiveTask, "resultJson", "[{\"sourceDatabaseId\":1,\"targetDatabaseId\":2,\"status\":\"PREPARING\"}]");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(mockDataArchiveTask));

        dataArchiveRollbackJob.execute(context);

        // add verification here based on expected behavior
    }

    // Add more test methods for other scenarios

}