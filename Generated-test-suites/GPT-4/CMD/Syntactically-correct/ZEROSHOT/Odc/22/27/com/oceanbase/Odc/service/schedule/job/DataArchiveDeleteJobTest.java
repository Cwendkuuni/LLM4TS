
package com.oceanbase.odc.service.schedule.job;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{}");
        Mockito.when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dataArchiveTask));
        Mockito.when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        Mockito.when(taskEntity.getId()).thenReturn(1L);
        Mockito.when(dataArchiveTask.getResultJson()).thenReturn("{tasks:[]}");
 
        dataArchiveDeleteJob.execute(context);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.any());
    }

    @Test
    public void testExecuteWithNoDataArchiveTask() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{}");
        Mockito.when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotDone() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{}");
        Mockito.when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dataArchiveTask));
        Mockito.when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

        dataArchiveDeleteJob.execute(context);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
    }
}
