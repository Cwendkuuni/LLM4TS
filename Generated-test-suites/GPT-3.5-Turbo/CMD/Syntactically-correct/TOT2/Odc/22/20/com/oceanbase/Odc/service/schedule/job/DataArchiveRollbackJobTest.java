import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        taskEntity.setResultJson("[{\"id\": 1, \"sourceDatabaseId\": 1, \"targetDatabaseId\": 2, \"status\": \"PREPARING\"}]");

        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(Optional.of(taskEntity));

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecute_TaskNotFound() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_StatusAlreadyDone() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        taskEntity.setResultJson("[{\"id\": 1, \"sourceDatabaseId\": 1, \"targetDatabaseId\": 2, \"status\": \"DONE\"}]");

        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(Optional.of(taskEntity));

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

}