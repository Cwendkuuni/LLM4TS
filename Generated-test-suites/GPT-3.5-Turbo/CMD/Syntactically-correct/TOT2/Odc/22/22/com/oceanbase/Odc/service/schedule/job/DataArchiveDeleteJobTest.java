import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 123 }");
        context.setResult(taskEntity);
    }

    @Test
    public void testExecute_foundDataArchiveTask() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(123)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);

        assertEquals(TaskStatus.PREPARING, dataArchiveTask.getStatus());
        // Add more assertions as needed
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(123)).thenReturn(Optional.empty());
        
        dataArchiveDeleteJob.execute(context);

        // Verify proper error handling and status update
    }

    @Test
    public void testExecute_dataArchiveTaskNotDone() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.CREATED);
        when(scheduleTaskRepository.findById(123)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);

        // Verify proper error handling and status update
    }
}