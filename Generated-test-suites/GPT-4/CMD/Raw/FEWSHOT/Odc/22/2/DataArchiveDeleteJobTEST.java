package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_NotFoundDataArchiveTask() {
        when(taskEntity.getParametersJson()).thenReturn("paramsJson");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_TaskNotDone() {
        when(taskEntity.getParametersJson()).thenReturn("paramsJson");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.PENDING);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_TaskDone() {
        when(taskEntity.getParametersJson()).thenReturn("paramsJson");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        scheduleTaskEntity.setResultJson("resultJson");

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(Long.class), any(TaskStatus.class));
    }
}