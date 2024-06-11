package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import  com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {
    
    @Test
    public void testExecute() throws JobExecutionException {
        
        // Arrange
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = mock(DataArchiveRollbackParameters.class);
        DlmTask dlmTask = new DlmTask();

        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(3L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);

        String taskId = "task-id";
        List<DlmTask> taskUnits = Arrays.asList(dlmTask);

        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(taskEntity.getId()).thenReturn(taskId);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("taskID");
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.getDataArchiveTask);
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

        // Act
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        job.execute(context);

        // Assert
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, getTaskStatus(taskUnits));
    }
}