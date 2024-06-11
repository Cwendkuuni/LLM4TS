import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("notFoundId");
        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("notFinishedId");
        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        Mockito.when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PROCESSING);

        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("finishedId");
        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        List<DlmTask> dlmTasks = new ArrayList<>();
        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        Mockito.when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        Mockito.when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(new JobExecutionContext());

        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.COMPLETED);
    }

    // Add more test cases for edge cases and exception handling scenarios here.
}