import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_SuccessfulRollback() {
        // Arrange
        JobExecutionContext context = mock(JobExecutionContext.class);
        
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        
        when(context.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask())));

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask());
        when(dataArchiveTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        // Act
        dataArchiveRollbackJob.execute(context);

        // Assert
        assertEquals(TaskStatus.DONE, dataArchiveTask.getStatus());
    }

    @Test
    public void testExecute_FailedRollback_TaskNotFound() {
        // Arrange
        JobExecutionContext context = mock(JobExecutionContext.class);
        
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        
        when(context.getResult()).thenReturn(taskEntity);

        when(dataArchiveTaskRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        dataArchiveRollbackJob.execute(context);

        // Assert
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    // Additional test cases for edge cases and error scenarios should be added here

}