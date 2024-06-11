import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.Optional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        // Any setup needed before the tests run
    }

    @Test
    public void testExecute_Success() {
        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));
        taskEntity.setId(1L);
        
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

        DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class);
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnit.setJobType(JobType.DELETE);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

        dataArchiveDeleteJob.execute(context);

        // Add assertions here for the success case
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));
        taskEntity.setId(1L);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        // Add assertions here for the data archive task not found case
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));
        taskEntity.setId(1L);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        // Add assertions here for the data archive task not done case
    }

}