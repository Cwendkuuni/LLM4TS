package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import java.util.Arrays;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity, dataArchiveTask;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Test
    public void testDataArchiveDeleteJobExecution() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
        when(lastTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn("[{}]");

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).findById(any());
        verify(scheduleTaskRepository).updateStatusById(any(), any());
    }

    @Test
    public void testDataArchiveDeleteJobExecutionNoTaskFound() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
        when(lastTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testDataArchiveDeleteJobExecutionTaskNotDone() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
        when(lastTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.IN_PROGRESS);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
}