
package com.oceanbase.odc.service.schedule.job;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import java.util.Optional;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Test class for DataArchiveDeleteJob.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext jobExecutionContextMock;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntityMock;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParametersMock;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    // Testing the execute method
    @Test
    public void executeTest() {

        when(jobExecutionContextMock.getResult()).thenReturn(scheduleTaskEntityMock);
        when(scheduleTaskEntityMock.getParametersJson()).thenReturn("json");
        when(scheduleTaskEntityMock.getResultJson()).thenReturn("json");
        when(scheduleTaskEntityMock.getId()).thenReturn(1L);
        when(dataArchiveClearParametersMock.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskEntityMock.getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(jobExecutionContextMock);
        assertEquals("Task status should match", TaskStatus.PREPARING, scheduleTaskEntityMock.getStatus());
    }

    // Testing the execute method when the data does not exist.
    @Test(expected=NotFoundException.class)
    public void executeDataDoesNotExistTest() {

        when(jobExecutionContextMock.getResult()).thenReturn(scheduleTaskEntityMock);
        when(scheduleTaskEntityMock.getParametersJson()).thenReturn("json");
        when(scheduleTaskEntityMock.getResultJson()).thenReturn("json");
        when(scheduleTaskEntityMock.getId()).thenReturn(1L);
        when(dataArchiveClearParametersMock.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskEntityMock.getStatus()).thenReturn(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(jobExecutionContextMock);
    }
}
