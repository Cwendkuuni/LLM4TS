
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Test
    public void testExecute_success() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        List<DlmTask> taskUnits = List.of(new DlmTask());
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class))
                .thenReturn(new DataArchiveClearParameters());
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), List.class)).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(context);

        // Assert the result based on the expected behavior
        // Add assertions here
    }

    // Add more test methods for other public methods in DataArchiveDeleteJob class

}
