
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @Mock
    private DataArchiveRollbackParameters rollbackParameters; 

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteNullTaskEntity() {
        when(context.getResult()).thenReturn(null);
        dataArchiveRollbackJob.execute(context);

        verify(context, times(1)).getResult();
        verify(rollbackParameters, never()).getDataArchiveTaskId();
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(context.getResult()).thenReturn(taskEntity);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1);
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
    }

    // You may continue with other edge cases and exception handling scenarios...
}
