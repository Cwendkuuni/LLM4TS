package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import java.util.Arrays;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DlmTask dlmTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBeFailedWhenDataArchiveTaskNotFound() {
        when(context.getResult()).thenReturn(taskEntity);

        //change your taskId for testing
        String taskId = "testTaskId1";
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void shouldBeFailedWhenDataArchiveTaskNotDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);

        //For your task id
        String taskId = "testTaskId2";
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void shouldUpdateTaskStatusWhenDataArchiveTaskDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        
        String taskId = "testTaskId3";
        List<DlmTask> taskUnits = Arrays.asList(dlmTask);
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), getTaskStatus(taskUnits));
    }
}