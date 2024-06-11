
package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1);

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("");
        when(taskEntity.getId()).thenReturn(1);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        when(taskEntity.getResultJson()).thenReturn(taskUnits);
        
        ScheduleTaskEntity mockTaskEntity = mock(ScheduleTaskEntity.class);

        when(scheduleTaskRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockTaskEntity));

        
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(Integer.class), any(TaskStatus.class));
    }
}
