package com.oceanbase.odc.service.schedule.job;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;

import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;

public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob jobUnderTest;
    private JobExecutionContext context;

    @Before
    public void setUp() {
        jobUnderTest = new DataArchiveDeleteJob();
        context = mock(JobExecutionContext.class);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_NullContext_ThrowsException() {
        jobUnderTest.execute(null);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_NullTaskEntity_ThrowsException() {
        when(context.getResult()).thenReturn(null);
        jobUnderTest.execute(context);
    }

    @Test 
    public void testExecute_TaskEntityNotFound_ShouldFailFast() {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getScheduler()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(new DataArchiveClearParameters().toJson());
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        jobUnderTest.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test 
    public void testExecute_DataArchiveTaskNotDone_ShouldFailFast() {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getScheduler()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(new DataArchiveClearParameters().toJson());
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PROCESSING);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        jobUnderTest.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_ValidInput_ExecutesSuccessfully() {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId(dataArchiveTask.getId());
        when(context.getScheduler()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(params.toJson());
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(Collections.singletonList(new DlmTask())));
        jobUnderTest.execute(context);
        verify(scheduleTaskRepository, atLeastOnce()).updateStatusById(anyString(), any());
    }
}