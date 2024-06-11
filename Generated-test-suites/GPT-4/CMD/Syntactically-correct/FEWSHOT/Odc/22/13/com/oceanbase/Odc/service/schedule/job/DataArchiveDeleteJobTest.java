package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob job;
    private ScheduleTaskEntity taskEntity;
    private JobExecutionContext context;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private ScheduleTaskEntity dataArchiveTask;

    @Before
    public void setup() {
        job = mock(DataArchiveDeleteJob.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        context = mock(JobExecutionContext.class);
        dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("parametersJson");
        when(taskEntity.getResultJson()).thenReturn("resultJson");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("dataArchiveTaskId");
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
    }
 
    @Test(expected = JobExecutionException.class)
    public void testExecute_DataArchiveTaskNotFound_ThrowsException() {
        when(job.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());
        job.execute(context);
        verify(job, times(1)).scheduleTaskRepository.findById(anyString());
        verify(job, times(1)).scheduleTaskRepository.updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }
    
    @Test(expected = JobExecutionException.class)
    public void testExecute_DataArchiveTaskNotDone_ThrowsException() {
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.FAILED);
        when(job.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        job.execute(context);
        verify(job, times(1)).scheduleTaskRepository.updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_TaskPrepareSuccess_UpdatesTaskStatus() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = mock(DlmTask.class);
        taskUnits.add(taskUnit);
        when(taskUnit.getJobType()).thenReturn(JobType.DELETE);
        when(job.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        job.execute(context);
        verify(job, times(1)).scheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class));
    }  

    @Test(expected = JobExecutionException.class)
    public void testExecute_NoTasksProvided_ThrowsException() {
	    List<DlmTask> taskUnits = Collections.emptyList();
	    when(job.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
	    job.execute(context);
        verify(job, times(1)).executeTask(anyString(), anyList());
        verify(job, times(1)).getTaskStatus(anyList());
        verify(job, times(1)).scheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class));
    }
}