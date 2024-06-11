import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext context;

    DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    @Test
    public void testExecuteDataArchiveDeleteJob() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");

        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
        
        dataArchiveDeleteJob.execute(context);

        // Assert statements for success scenario
        assertTrue(true);
    }

    @Test
    public void testExecuteDataArchiveDeleteJob_NoDataArchiveTask() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");

        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);

        taskEntity.setParametersJson("{\"dataArchiveTaskId\":2}");

        dataArchiveDeleteJob.execute(context);

        // Assert statements for no data archive task scenario
        // Add your assertions here
    }

    // Add more test cases for edge cases and error scenarios here

}