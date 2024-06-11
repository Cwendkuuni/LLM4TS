package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext mockContext;

    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson("");

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnits.add(taskUnit);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
        Mockito.when(dataArchiveDeleteJob.scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);
        Mockito.when(dataArchiveDeleteJob.scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED))
                .thenReturn(true);

        dataArchiveDeleteJob.execute(mockContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}