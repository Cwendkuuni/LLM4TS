
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_withNullTaskEntity() {
        when(context.getResult()).thenReturn(null);
        dataArchiveRollbackJob.execute(context);
        // Verify log error and further action.
    }

    @Test
    public void testExecute_withInvalidParameterJson() {
        // Mock taskEntity and context
        // Pass a non-JSON string
        // Verify log error and further action
    }

    @Test
    public void testExecute_withDataArchiveTaskOptionNotPresent() {
        // Mock taskEntity, context, and dataArchiveTaskOption
        // Assert not present
        // Verify log error and further action
    }

    @Test
    public void testExecute_withInvalidResultJson() {
        // Mock taskEntity, dataArchiveTaskOption, and dataArchiveTask
        // Pass a non-JSON string
        // Verify log error and further action
    }
}
