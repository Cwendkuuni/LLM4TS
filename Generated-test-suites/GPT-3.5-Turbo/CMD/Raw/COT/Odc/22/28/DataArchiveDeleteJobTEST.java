
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private List<DlmTask> dlmTasks;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
        dlmTasks = new ArrayList<>();
    }

    @Test
    public void testExecute() {
        // Prepare test data
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(dlmTasks));

        // Test case for successful execution
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
        dataArchiveDeleteJob.execute(jobExecutionContext);
        // Add assertions based on the logic in execute() method

        // Test case for dataArchiveTaskOption not present
        dataArchiveTaskOption = Optional.empty();
        dataArchiveDeleteJob.execute(jobExecutionContext);
        // Add assertions based on the logic in execute() method

        // Test case for dataArchiveTask status not DONE
        scheduleTaskEntity.setStatus(TaskStatus.IN_PROGRESS);
        dataArchiveDeleteJob.execute(jobExecutionContext);
        // Add assertions based on the logic in execute() method
    }
}
