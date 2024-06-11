import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class DataArchiveDeleteJobTest {
    ScheduleTaskEntity taskEntity;
    JobExecutionContext jobExecutionContext;
    DataArchiveClearParameters dataArchiveClearParameters;
    DlmTask mockDlmTask;

    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        jobExecutionContext = Mockito.mock(JobExecutionContext.class);
        dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
        mockDlmTask = Mockito.mock(DlmTask.class);
    }

    @Test
    public void executeArchiveTaskNotPresent() {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void executeArchiveTaskNotDone() {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void executeSuccess() {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(List.of(mockDlmTask)));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), any());
    }
}