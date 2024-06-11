import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Test
    public void testExecute() {
        MockitoAnnotations.initMocks(this);

        // setup test data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        List<DlmTask> taskUnits = List.of(new DlmTask());
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        // method execution
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // assertions
        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
    }
}