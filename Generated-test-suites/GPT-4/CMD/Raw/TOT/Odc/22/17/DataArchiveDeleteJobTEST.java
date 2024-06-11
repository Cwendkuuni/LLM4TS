import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import org.quartz.impl.JobExecutionContextImpl;
import org.mockito.Mockito;
import java.util.Optional;
import java.util.ArrayList;

public class DataArchiveDeleteJobTest {

    ScheduleTaskRepository taskRepo;
    DataArchiveDeleteJob job;
    JobExecutionContextImpl context;
    ScheduleTaskEntity task;
    DataArchiveClearParameters params;

    // Pre-setup for all the test cases.
    @Before
    public void setUp() {
        job = new DataArchiveDeleteJob();
        taskRepo = Mockito.mock(ScheduleTaskRepository.class);
        task = new ScheduleTaskEntity();
        params = new DataArchiveClearParameters();
        context = new JobExecutionContextImpl(null, null, null, null, null, null, null);
    }

    // Test execute with empty data archive task option in context.
    @Test
    public void testExecuteEmptyDataArchiveTaskOption() {
        params.setDataArchiveTaskId("123");
        task.setParametersJson(JsonUtils.toJson(params));
        context.setResult(task);
        Mockito.when(taskRepo.findById("123")).thenReturn(Optional.empty());

        job.execute(context);
        Mockito.verify(taskRepo).updateStatusById(task.getId(), TaskStatus.FAILED);
    }

    // Test execute with data archive task status that is not done.
    @Test
    public void testExecuteDataArchiveTaskNotDone() {
        params.setDataArchiveTaskId("123");
        task.setParametersJson(JsonUtils.toJson(params));
        context.setResult(task);
        ScheduleTaskEntity archiveTask = new ScheduleTaskEntity();
        archiveTask.setStatus(TaskStatus.RUNNING);
        Mockito.when(taskRepo.findById("123")).thenReturn(Optional.of(archiveTask));

        job.execute(context);
        Mockito.verify(taskRepo).updateStatusById(task.getId(), TaskStatus.FAILED);
    }

    // Test execute with task units that failed execution.
    @Test
    public void testExecuteFailedTaskUnits() {
        params.setDataArchiveTaskId("123");
        task.setParametersJson(JsonUtils.toJson(params));
        context.setResult(task);
        ScheduleTaskEntity archiveTask = new ScheduleTaskEntity();
        archiveTask.setStatus(TaskStatus.DONE);
        List<DlmTask> taskUnits = new ArrayList<DlmTask>();
        taskUnits.add(new DlmTask());
        archiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        Mockito.when(taskRepo.findById("123")).thenReturn(Optional.of(archiveTask));
        job.execute(context); // Expecting an exception as executeTask hasn't been mocked properly
    }
}