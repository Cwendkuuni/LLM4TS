import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

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
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_Success() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        // Initialize taskEntity and dataArchiveClearParameters as required

        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyLong())).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(context);

        // Add assertions for successful execution
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        // Initialize taskEntity and dataArchiveClearParameters as required

        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyLong())).thenReturn(null);

        dataArchiveDeleteJob.execute(context);

        // Add assertions for scenario where data archive task is not found
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        // Initialize taskEntity and dataArchiveClearParameters as required

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTask);

        dataArchiveDeleteJob.execute(context);

        // Add assertions for scenario where data archive task is not done
    }

    @Test
    public void testExecute_TaskUnitsPreparation() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        // Initialize taskEntity and dataArchiveClearParameters as required

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        // Add sample task units to the list

        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTask);
        when(dataArchiveDeleteJob.scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

        dataArchiveDeleteJob.execute(context);

        // Add assertions for task units preparation
    }
}