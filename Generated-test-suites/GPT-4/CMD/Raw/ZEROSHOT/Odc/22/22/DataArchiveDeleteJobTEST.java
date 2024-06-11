
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DlmTask taskUnit;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("someTaskId");
        when(scheduleTaskRepository.findById("someTaskId")).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn("[{}]");
        when(taskEntity.getId()).thenReturn("JobId");
        when(taskEntity.getJobName()).thenReturn("JobName");
        when(taskEntity.getJobGroup()).thenReturn("JobGroup");
        when(taskEntity.getJobName()).thenReturn("JobName");
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveDeleteJob.execute(context);

        verify(taskUnit).setId(DlmJobIdUtil.generateHistoryJobId("JobName", "JobGroup", "JobId", 1));
        verify(taskUnit).setJobType(JobType.DELETE);
        verify(taskUnit).setStatus(TaskStatus.PREPARING);
    }
}
