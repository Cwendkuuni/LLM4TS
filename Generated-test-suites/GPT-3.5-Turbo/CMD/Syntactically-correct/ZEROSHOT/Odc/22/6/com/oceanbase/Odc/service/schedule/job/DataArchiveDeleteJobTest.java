import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_DataArchiveNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity("1", "Test Job", "Test Group", TaskStatus.DONE, "{}", "{}");
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters("123");
        Mockito.when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class))
                .thenReturn(dataArchiveClearParameters);

        Mockito.when(scheduleTaskRepository.findById("123")).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveNotDone() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity("1", "Test Job", "Test Group", TaskStatus.DONE, "{}", "{}");
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters("123");
        Mockito.when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class))
                .thenReturn(dataArchiveClearParameters);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity("123", "Data Archive Job", "Data Archive Group", TaskStatus.RUNNING, "{}", "{}");
        Mockito.when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_Success() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity("1", "Test Job", "Test Group", TaskStatus.DONE, "{}", "{}");
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters("123");
        Mockito.when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class))
                .thenReturn(dataArchiveClearParameters);

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask("1", "Task 1", TaskStatus.DONE));
        taskUnits.add(new DlmTask("2", "Task 2", TaskStatus.DONE));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity("123", "Data Archive Job", "Data Archive Group", TaskStatus.DONE, "{}", JsonUtils.toJson(taskUnits));
        Mockito.when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }
}