import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
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
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_Success() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("jobName");
        scheduleTaskEntity.setJobGroup("jobGroup");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson("[{\"id\": 1, \"sourceDatabaseId\": 1, \"targetDatabaseId\": 2, \"jobType\": \"ROLLBACK\", \"status\": \"PREPARING\"}]");

        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask(1L, 1L, 2L, JobType.ROLLBACK, TaskStatus.PREPARING));

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveRollbackJob.getScheduleTaskRepository().findById(2L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveRollbackJob.getScheduleTaskRepository().updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        TaskStatus expectedStatus = TaskStatus.DONE;
        verify(dataArchiveRollbackJob.getScheduleTaskRepository(), times(1)).updateStatusById(1L, expectedStatus);
    }
    
    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("jobName");
        scheduleTaskEntity.setJobGroup("jobGroup");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveRollbackJob.getScheduleTaskRepository().findById(2L)).thenReturn(Optional.empty());
        when(dataArchiveRollbackJob.getScheduleTaskRepository().updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        TaskStatus expectedStatus = TaskStatus.FAILED;
        verify(dataArchiveRollbackJob.getScheduleTaskRepository(), times(1)).updateStatusById(1L, expectedStatus);
    }

    // Add more test cases for edge cases and error scenarios

}