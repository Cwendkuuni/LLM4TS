
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[]");

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert that the task status is updated correctly to TaskStatus.PREPARING
        assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 999}");

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert that the task status is updated correctly to TaskStatus.FAILED
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotDone() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[]");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.QUEUED);
        
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(dataArchiveTaskRepository.findById(1)).thenReturn(dataArchiveTask);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert that the task status is updated correctly to TaskStatus.FAILED
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}
