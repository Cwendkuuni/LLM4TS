
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;
    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(taskEntity.getParametersJson()).thenReturn("");
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(taskEntity.getId()).thenReturn(1L);

        dataArchiveDeleteJob.execute(context);

        verify(taskEntity, times(1)).getId();
    }

    @Test
    public void testExecuteTaskNotDone() {
        when(taskEntity.getParametersJson()).thenReturn("");
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(taskEntity.getId()).thenReturn(1L);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(context);

        verify(taskEntity, times(2)).getId();
        verify(dataArchiveTask, times(1)).getStatus();
    }

    @Test
    public void testExecuteTaskPass() {
        when(taskEntity.getParametersJson()).thenReturn("");
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(taskEntity.getId()).thenReturn(1L);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn("");

        dataArchiveDeleteJob.execute(context);

        verify(taskEntity, times(2)).getId();
        verify(dataArchiveTask, times(1)).getResultJson();
    }
}
