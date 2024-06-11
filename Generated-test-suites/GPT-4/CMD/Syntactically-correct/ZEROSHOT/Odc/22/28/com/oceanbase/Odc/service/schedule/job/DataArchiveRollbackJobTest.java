
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
   
    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
    }
    
    @Test
    public void testExecute() {
         when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
         when(taskEntity.getResultJson()).thenReturn("[{\"sourceDatabaseId\":1, \"targetDatabaseId\":2, \"status\":\"PREPARING\"}]");
         dataArchiveRollbackJob.execute(context);
         verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any());
    }
    
    @Test
    public void testExecuteNoTaskEntityFound() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        this.dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }
}
