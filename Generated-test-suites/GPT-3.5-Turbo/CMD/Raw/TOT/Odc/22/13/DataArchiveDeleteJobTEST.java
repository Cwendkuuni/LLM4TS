import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_DataArchiveTaskNotPresent() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{}");

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{}");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveDeleteJob.getScheduleTaskRepository().updateStatusById(anyLong(), any())).thenReturn(true);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().updateStatusById(anyLong(), any())).thenReturn(true);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().updateStatusById(anyLong(), any())).thenReturn(true);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().updateStatusById(anyLong(), any())).thenReturn(true);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assertion logic here
    }

}