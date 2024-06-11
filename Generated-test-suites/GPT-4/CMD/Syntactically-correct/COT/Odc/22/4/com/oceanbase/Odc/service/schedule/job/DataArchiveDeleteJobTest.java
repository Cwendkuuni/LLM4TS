package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @InjectMocks
    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    DlmTask dlmTask;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_success() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("1");
        when(scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(taskEntity.getResultJson()).thenReturn("{}");
        String[] taskUnits = { "task1", "task2", "task3" };
        when(JsonUtils.fromJson(any(), any())).thenReturn(Arrays.asList(taskUnits));
        when(dlmJobIdUtil.generateHistoryJobId(any(), any(), any(), any())).thenReturn("newJobId");
        when(getTaskStatus(any())).thenReturn(TaskStatus.PREPARING);
        dataArchiveDeleteJob.execute(context);
        verify(taskEntity).updateStatus();
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_dataArchiveTask_notFound_exception() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("1");
        when(scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_status_notDONE_exception() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("1");
        when(scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);
        dataArchiveDeleteJob.execute(context);
    }
    
    @Test
    public void testExecute_emptyTaskUnits_case() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("1");
        when(scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(taskEntity.getResultJson()).thenReturn("{}");
        when(JsonUtils.fromJson(any(), any())).thenReturn(new ArrayList<>());
        dataArchiveDeleteJob.execute(context);
        verify(dlmJobIdUtil, never()).generateHistoryJobId(any(), any(), any(), any());
    }
}
