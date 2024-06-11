import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
    
    @Mock
    private JobExecutionContext jobExecutionContext;
    
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;
    
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(jobExecutionContext).getResult();
    }

    @Test
    public void testPrepareTaskUnits() {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(3L);
        dlmTask.setJobType(JobType.DATA_ARCHIVE);
        dlmTask.setStatus(TaskStatus.PREPARING);
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        dataArchiveRollbackJob.prepareTaskUnits(dlmTasks, 5L, "jobGroup", "jobName");
        Long expectedSourceDatabaseId = dlmTasks.get(0).getTargetDatabaseId();
        Long expectedTargetDatabaseId = dlmTasks.get(0).getSourceDatabaseId();
        for(DlmTask taskUnit: dlmTasks){
            assertEquals(JobType.ROLLBACK, taskUnit.getJobType());
            assertEquals(TaskStatus.DONE, taskUnit.getStatus());
            assertEquals(expectedSourceDatabaseId, taskUnit.getSourceDatabaseId());
            assertEquals(expectedTargetDatabaseId, taskUnit.getTargetDatabaseId());
        }
    }

    @Test
    public void testGetTaskStatus() {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(TaskStatus.SUCCESS);
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        TaskStatus status = dataArchiveRollbackJob.getTaskStatus(dlmTasks);
        assertEquals(TaskStatus.SUCCESS, status);
    }
}