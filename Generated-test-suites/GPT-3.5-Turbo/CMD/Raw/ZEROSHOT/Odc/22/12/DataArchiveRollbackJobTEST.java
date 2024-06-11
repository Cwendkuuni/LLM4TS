import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask())));
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        when(dataArchiveRollbackJob.getScheduleTaskRepository().findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Verify if the methods were called as expected
        verify(dataArchiveRollbackJob.getScheduleTaskRepository(), times(1)).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.DONE));
    }
}