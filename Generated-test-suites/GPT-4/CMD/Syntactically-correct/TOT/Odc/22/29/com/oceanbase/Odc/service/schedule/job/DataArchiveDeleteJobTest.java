
package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    DataArchiveDeleteJob dataArchiveDeleteJob;
    JobExecutionContext jobExecutionContext;
    ScheduleTaskEntity taskEntity;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
    }

    @Test
    public void execute_noArchiveTaskFound() {
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId(1);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(1)).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(dataArchiveDeleteJob.scheduleTaskRepository).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
    }

    @Test
    public void execute_archiveTaskNotDone() {
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId(1);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(1)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(dataArchiveDeleteJob.scheduleTaskRepository).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
    }

    /* Similarly, we can add valid scenario(s) and various error scenarios etc.*/
}
