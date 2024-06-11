
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    List<DlmTask> taskUnits;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnits.add(taskUnit);
    }

    @Test
    public void testExecute_nonExistentArchiveTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(taskEntity.getId()).thenReturn(1L);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);
        
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_unfinishedArchiveTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(taskEntity.getId()).thenReturn(1L);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.PROCESSING);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_taskPrepared() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(taskEntity.getId()).thenReturn(1L);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setResultJson("[]");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}
