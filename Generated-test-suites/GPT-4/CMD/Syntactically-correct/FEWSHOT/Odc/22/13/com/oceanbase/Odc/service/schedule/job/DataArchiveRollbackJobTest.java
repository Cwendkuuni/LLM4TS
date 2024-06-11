
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {
    @Test
    public void testExecute() {
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId("archiveTaskId");
        DlmTask mockDlmTask = new DlmTask();
        mockDlmTask.setSourceDatabaseId(1L);
        mockDlmTask.setTargetDatabaseId(2L);
        mockDlmTask.setStatus(TaskStatus.PREPARING);
        ScheduleTaskEntity mockDataArchiveTask = new ScheduleTaskEntity();
        mockDataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(mockDlmTask)));
        
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
        when(scheduleTaskRepository.findById("archiveTaskId")).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(mockDlmTask)));
        when(context.getResult()).thenReturn(mockDataArchiveTask);
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn("generatedId");
                
        job.execute(context);
        
        verify(scheduleTaskRepository, times(1)).findById("archiveTaskId");
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.PREPARING));
    }
}
