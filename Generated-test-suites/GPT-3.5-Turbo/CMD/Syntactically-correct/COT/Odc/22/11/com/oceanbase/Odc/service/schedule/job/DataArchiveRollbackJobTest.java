
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;
import org.junit.Test;

import java.util.Optional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        JobExecutionContext context = mock(JobExecutionContext.class);

        // Mocking ScheduleTaskEntity
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("");
        taskEntity.setId(1L);

        // Mocking DataArchiveRollbackParameters
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        // Mocking dataArchiveTask
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson("[]");

        Optional<ScheduleTaskEntity> optionalDataArchiveTask = Optional.of(dataArchiveTask);

        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(optionalDataArchiveTask);

        dataArchiveRollbackJob.execute(context);

        // Add assertions here for the expected behavior of the execute method
    }
}
