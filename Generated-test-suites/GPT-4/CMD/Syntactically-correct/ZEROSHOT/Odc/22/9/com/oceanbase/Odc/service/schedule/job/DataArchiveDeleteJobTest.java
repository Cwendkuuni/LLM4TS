package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    
    private DataArchiveClearParameters dataArchiveClearParameters;
    
    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskEntity = new ScheduleTaskEntity();
        dataArchiveClearParameters = new DataArchiveClearParameters();
    }
    
    @Test
    public void testExecuteWithFoundTask() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, never()).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
    
    @Test
    public void testExecuteWithNotFoundTask() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
    
    @Test
    public void testExecuteWithNotDoneTask() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
    
    @Test
    public void testExecuteWithTaskUnits() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnits.add(taskUnit);

        scheduleTaskEntity.setResultJson(JsonUtils.toJson(taskUnits));

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
    }
}
