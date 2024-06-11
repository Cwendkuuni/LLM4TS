import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 123 }");
        taskEntity.setId(1);

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(123L)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Add assertions here to verify the functionality of the execute method
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 123 }");
        taskEntity.setId(1);

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(123L)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Add assertions here to verify the functionality when dataArchiveTask is not found
    }

    // Add more test cases for edge cases and exceptions as needed

}