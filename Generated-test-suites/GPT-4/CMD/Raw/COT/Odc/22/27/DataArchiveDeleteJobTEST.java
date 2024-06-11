
package com.oceanbase.odc.service.schedule.job;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {
  
    @Test
    public void testExecute() {
        
        //create instances for testing
        DataArchiveDeleteJob dadJob = new DataArchiveDeleteJob();        
        
        JobExecutionContext jec = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity ste = Mockito.mock(ScheduleTaskEntity.class);

        //Mimic the calls and returns in real environment
        Mockito.when(jec.getResult()).thenReturn(ste);

        Mockito.when(ste.getParametersJson()).thenReturn(someJsonString);
        Mockito.when(ste.getResultJson()).thenReturn(someListJsonString);
         
        Mockito.when(ste.getStatus()).thenReturn(TaskStatus.DONE);
        Mockito.when(ste.getId()).thenReturn(1L);
        Mockito.when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(ste));
        
         //call the method to be tested
        dadJob.execute(jec);
        
        // verify the calls
        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.PREPARING);
        
        // with edge case where ScheduleTaskEntity is not present
        Mockito.when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        
        // call the execute method again
        dadJob.execute(jec);
        
        // verify the failure
        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
    }
}
