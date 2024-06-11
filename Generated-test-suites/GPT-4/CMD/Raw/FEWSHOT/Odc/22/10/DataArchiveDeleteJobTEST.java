
package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob deleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setup() {
        deleteJob = new DataArchiveDeleteJob();
        context = Mockito.mock(JobExecutionContext.class);
        taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
    
        when(taskEntity.getParametersJson()).thenReturn("");
        when(taskEntity.getId()).thenReturn(0L);
        when(context.getResult()).thenReturn(taskEntity);
    }

    @Test
    public void testDataArchiveDeleteJobTaskNotFound() {
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(0L);
        when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        deleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(Mockito.anyLong(), TaskStatus.FAILED);
    }

    @Test
    public void testDataArchiveDeleteJobTaskNotFinished() {
        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(0L);
        when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.IN_PROCESS);

        deleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(Mockito.anyLong(), TaskStatus.FAILED);
    }

    @Test
    public void testDataArchiveDeleteJobTaskExecute() {
        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        ArrayList<DlmTask> dlmTasks = new ArrayList<>();
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(0L);
        when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        deleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(Mockito.anyLong(), Mockito.any(TaskStatus.class));
    }
}
