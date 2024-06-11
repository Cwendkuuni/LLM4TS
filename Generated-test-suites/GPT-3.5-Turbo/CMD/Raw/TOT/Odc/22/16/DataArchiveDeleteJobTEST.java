import static org.junit.Assert.*;
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
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        taskEntity = new ScheduleTaskEntity();
        dataArchiveClearParameters = new DataArchiveClearParameters();
        taskUnits = new ArrayList<>();
    }

    @Test
    public void testExecute() {
        // Typical use case
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class))).thenReturn(dataArchiveClearParameters);
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(context);

        // Edge case
        taskEntity.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        dataArchiveDeleteJob.execute(context);

        // Error scenario
        taskEntity.setStatus(TaskStatus.ERROR);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(3)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}