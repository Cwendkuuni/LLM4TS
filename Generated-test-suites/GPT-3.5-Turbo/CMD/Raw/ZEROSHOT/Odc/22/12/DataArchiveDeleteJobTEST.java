package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        when(taskEntity.getParametersJson()).thenReturn("testParametersJson");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);

        List<DlmTask> taskUnits = mock(List.class);
        when(taskEntity.getResultJson()).thenReturn("testResultJson");

        dataArchiveDeleteJob.execute(jobExecutionContext);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(dataArchiveTaskOption);

        TaskStatus taskStatus = TaskStatus.DONE;
        when(dataArchiveTask.getStatus()).thenReturn(taskStatus);

        verify(scheduleTaskRepository).updateStatusById(any(Long.class), any(TaskStatus.class));
    }
}