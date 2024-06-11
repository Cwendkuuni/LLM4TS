
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

    @MockBean
    private JobExecutionContext context;
    @MockBean
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    @MockBean
    private ScheduleTaskEntity taskEntity;
    @MockBean
    private DataArchiveClearParameters dataArchiveClearParameters;
    @MockBean
    private List<DlmTask> dlmTasks;

    @Before
    public void setup() {
        dlmTasks = new ArrayList<>();
        DlmTask task = new DlmTask();
        task.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(task);
    }

    @Test
    public void testExecute() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn(anyString());
        Mockito.when(JsonUtils.fromJson(anyString(), DataArchiveClearParameters.class)).
                thenReturn(dataArchiveClearParameters);
        Mockito.when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
        Mockito.when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        Mockito.when(JsonUtils.fromJson(anyString(),new TypeReference<List<DlmTask>>(){})).thenReturn(dlmTasks);

        try {
            dataArchiveDeleteJob.execute(context);
        } catch (Exception e) {
            fail("Failed to execute job.");
        }

        assertEquals("DELETE",dlmTasks.get(0).getJobType().toString());
        assertEquals("PREPARING",dlmTasks.get(0).getStatus().toString());
    }
}
