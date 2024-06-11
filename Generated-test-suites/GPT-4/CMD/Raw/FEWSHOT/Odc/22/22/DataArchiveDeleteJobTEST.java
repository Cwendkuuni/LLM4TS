package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Captor
    private ArgumentCaptor<TaskStatus> taskStatusArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(2)).updateStatusById(any(), taskStatusArgumentCaptor.capture());
        assertFalse(taskStatusArgumentCaptor.getAllValues().contains(TaskStatus.DONE));
    }

    @Test
    public void testExecuteDataArchiveTaskNotPresent() throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(), taskStatusArgumentCaptor.capture());
        assertEquals(TaskStatus.FAILED, taskStatusArgumentCaptor.getValue());
    }

    @Test
    public void testExecuteDataArchiveTaskNotDone() throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(), taskStatusArgumentCaptor.capture());
        assertEquals(TaskStatus.FAILED, taskStatusArgumentCaptor.getValue());
    }
}