import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("Test Job");
        scheduleTaskEntity.setJobGroup("Test Group");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        scheduleTaskEntity.setResultJson("{}");
    }

    @Test
    public void testExecute() {
        // Set up
        dataArchiveRollbackJob.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        // Execute the method
        dataArchiveRollbackJob.execute(context);

        // Verify the results
        verify(dataArchiveRollbackJob.scheduleTaskRepository).findById(2L);
    }

    @Test
    public void testExecuteWithInvalidDataArchiveTaskId() {
        ScheduleTaskEntity invalidTaskEntity = new ScheduleTaskEntity();
        invalidTaskEntity.setId(1L);
        invalidTaskEntity.setJobName("Invalid Job");
        invalidTaskEntity.setJobGroup("Invalid Group");
        invalidTaskEntity.setParametersJson("{}");
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(dataArchiveRollbackJob.scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testPrepareTasksForRollback() {
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        dataArchiveRollbackJob.executeTask(1L, taskUnits);

        assertEquals(JobType.ROLLBACK, taskUnits.get(0).getJobType());
    }

    // Add more test cases for other methods as needed

}