package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollBackJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    private DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

    @Test
    public void testExecute() {
        // Arrange
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        // Act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assert
        verify(jobExecutionContext, times(1)).getResult();
    }
}
