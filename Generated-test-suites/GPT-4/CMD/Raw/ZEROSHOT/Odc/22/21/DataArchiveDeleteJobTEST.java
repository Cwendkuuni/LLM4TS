
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;
import java.util.Arrays;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import java.util.Optional;
import java.util.ArrayList;
import static org.junit.Assert.*;


public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private ArrayList<DlmTask> taskUnits;

    @Before
    public void setUp() throws Exception {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
        taskUnits = new ArrayList<>(Arrays.asList(mock(DlmTask.class)));

        when(taskEntity.getId()).thenReturn("1");
        when(taskEntity.getParametersJson()).thenReturn("json");
        when(taskEntity.getResultJson()).thenReturn("json");
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("1");
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
    }

    @Test
    public void testExecute() {
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(jobExecutionContext, times(1)).getResult();
    }

    @Test
    public void testExecuteWhenDataArchiveTaskNotFound() {
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenDataArchiveTaskStatusNotDone() {
        when(taskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenDataArchiveTaskStatusDone() {
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), any(TaskStatus.class));
    }
}
