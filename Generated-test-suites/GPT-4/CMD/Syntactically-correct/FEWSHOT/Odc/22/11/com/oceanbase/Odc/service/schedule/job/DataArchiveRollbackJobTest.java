package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {
    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    @Mock
    private DlmTask dlmTask;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
        context = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        dlmTask = mock(DlmTask.class);
        when(taskEntity.getResultJson()).thenReturn(Arrays.asList(dlmTask));
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
    }

    @Test
    public void testExecute() {
        dataArchiveRollbackJob.execute(context);
        verify(dataArchiveRollbackJob, times(1)).executeTask(any(), any());
        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(1)).updateStatusById(any(), any());
        verify(dlmTask, times(1)).setStatus(TaskStatus.DONE);
    }

    @Test
    public void testExecute_ArchiveTaskNotFound() {
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(dataArchiveRollbackJob, never()).executeTask(any(), any());
        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
        verify(dlmTask, never()).setStatus(TaskStatus.DONE);
    }

    @Test
    public void testExecute_DlmTaskNotPreparing() {
        when(dlmTask.getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveRollbackJob.execute(context);

        verify(dlmTask, times(1)).setStatus(TaskStatus.PREPARING);
    }
}