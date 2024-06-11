import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_Success() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);

        DataArchiveDeleteJob mockedDataArchiveDeleteJob = mock(DataArchiveDeleteJob.class);
        when(mockedDataArchiveDeleteJob.scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));
        
        DataArchiveDeleteJob mockedDataArchiveDeleteJob = mock(DataArchiveDeleteJob.class);
        when(mockedDataArchiveDeleteJob.scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.PROCESSING);

        DataArchiveDeleteJob mockedDataArchiveDeleteJob = mock(DataArchiveDeleteJob.class);
        when(mockedDataArchiveDeleteJob.scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

}