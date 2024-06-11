package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = Mockito.mock(JobExecutionContext.class);
        taskEntity = Mockito.mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        dataArchiveDeleteJob.execute(context); // since actual repository is not used, this will fail all conditions in execute method
        Mockito.verify(taskEntity, Mockito.atLeastOnce()).getId(); // checks if method getId() was called
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWithInvalidJson() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{\"invalidProp\":1}");
        dataArchiveDeleteJob.execute(context); // this should throw RuntimeException because of invalid json
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWithNullContext() {
        dataArchiveDeleteJob.execute(null); // this will raise NullPointerException
    }
}
