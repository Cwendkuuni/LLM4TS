package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity archiveTaskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private ArrayList<DlmTask> taskUnits;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        archiveTaskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveClearParameters = mock(DataArchiveClearParameters.class);

        taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

    }

    @Test
    public void testExecuteDataArchiveTask_NotPresent() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getId()).thenReturn(1);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": 123}");
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteDataArchiveTask_Finished() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": 123}");
        when(scheduleTaskEntity.getId()).thenReturn(1);
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(Optional.of(archiveTaskEntity));
        when(archiveTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(archiveTaskEntity.getResultJson()).thenReturn("[{\"status\": \"PREPARING\"}]");

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), any(TaskStatus.class));
    }

    @Test
    public void testExecuteDataArchiveTask_NotFinished() {

        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": 123}");
        when(scheduleTaskEntity.getId()).thenReturn(1);
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(Optional.of(archiveTaskEntity));
        when(archiveTaskEntity.getStatus()).thenReturn(TaskStatus.IN_PROGRESS);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
        verify(scheduleTaskRepository, never()).updateStatusById(anyInt(), any(TaskStatus.class));
    }

}
