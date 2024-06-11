import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        // Setup
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");

        Mockito.when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        // Execute
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Verify
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotDone() {
        // Setup
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        Mockito.when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(1)).thenReturn(dataArchiveTask);

        // Execute
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Verify
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithPreparingTasks() {
        // Setup
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[{\"id\": 1, \"name\": \"Task 1\"}]");

        Mockito.when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(1)).thenReturn(dataArchiveTask);

        // Execute
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Verify
        assertEquals(JobType.DELETE, taskUnits.get(0).getJobType());
        assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
    }

}