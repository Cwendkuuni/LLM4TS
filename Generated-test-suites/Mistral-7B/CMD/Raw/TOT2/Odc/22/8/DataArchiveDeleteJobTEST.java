import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.quartz.JobExecutionContext;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {

    @org.junit.Test
    public void testExecute() {
        // Arrange
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
        scheduleTaskEntity.setStatus(TaskStatus.CREATED);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // Act
        dataArchiveDeleteJob.execute(context);

        // Assert
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }
}