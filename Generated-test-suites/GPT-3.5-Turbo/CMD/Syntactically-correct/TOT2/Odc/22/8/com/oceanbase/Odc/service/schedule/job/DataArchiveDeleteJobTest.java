import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobText {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute() {
        // Test if data archive task is not found
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(params));
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));

        // Test if data archive task is not done
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.PENDING);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository, times(2)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));

        // Test the main functionality of execute method
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask())));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.DONE));
    }

    // Additional test cases for edge cases and exceptions can be added here

}