import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
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
    public void testExecute() throws JsonProcessingException {
        // Mock data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters(1L)));
        taskEntity.setResultJson(JsonUtils.toJson(new ArrayList<DlmTask>()));

        // Mock behavior
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        // Execute the method
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Verify behavior
        verify(jobExecutionContext, times(1)).getResult();
    }

    @Test
    public void testExecuteWithNoDataArchiveTask() throws JsonProcessingException {
        // Mock data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters(1L)));
        taskEntity.setResultJson(JsonUtils.toJson(new ArrayList<DlmTask>()));

        // Mock behavior
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        // Execute the method
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Verify task status
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    // Add more individual test cases for other methods as needed

}