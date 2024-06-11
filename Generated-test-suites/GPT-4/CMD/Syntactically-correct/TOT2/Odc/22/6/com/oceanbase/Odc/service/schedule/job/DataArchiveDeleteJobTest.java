package com.oceanbase.odc.service.schedule.job;

import org.junit.*;
import static org.junit.Assert.*;

import org.mockito.*;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.service.schedule.job.*;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testExecute_FoundDataArchiveTask() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson())
               .thenReturn("{}");
               
        // Mock for a found data archive task with a completed status
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(taskEntity));
        Mockito.when(dataArchiveTask.getStatus())
               .thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyInt(), Mockito.eq(TaskStatus.DONE));
    }
    
    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{}");
               
        // Mock for a not found data archive task
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyInt(), Mockito.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{}");
               
        // Mock for a found data archive task with a not completed status
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(taskEntity));
        Mockito.when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyInt(), Mockito.eq(TaskStatus.FAILED));
    }
}
