import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext jobExecutionContext;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecuteWithValidDataArchiveTaskId() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask());
        ScheduleTaskEntity savedDataArchiveTask = new ScheduleTaskEntity();
        savedDataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(savedDataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert that the status of the task is updated to TaskStatus.DONE
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecuteWithInvalidDataArchiveTaskId() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.FAILED))).thenReturn(true);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert that the status of the task is updated to TaskStatus.FAILED
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    // Add more test cases for other methods as needed

}