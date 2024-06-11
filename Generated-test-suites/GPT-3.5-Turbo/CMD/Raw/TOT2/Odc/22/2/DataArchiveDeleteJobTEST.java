import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    public void testExecute_SuccessfulTask() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters(123);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask("1", "TestTask", TaskStatus.DONE));

        when(scheduleTaskRepository.findById(eq(123L))).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(eq(1L), eq(TaskStatus.PREPARING))).thenReturn(true);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).findById(eq(123L));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.PREPARING));
    }

    @Test
    public void testExecute_TaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters(123);
        
        when(scheduleTaskRepository.findById(eq(123L))).thenReturn(Optional.empty());
        when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.FAILED))).thenReturn(false);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).findById(eq(123L));
        verify(scheduleTaskRepository, never()).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    // Add more test cases for edge cases and error scenarios 

}