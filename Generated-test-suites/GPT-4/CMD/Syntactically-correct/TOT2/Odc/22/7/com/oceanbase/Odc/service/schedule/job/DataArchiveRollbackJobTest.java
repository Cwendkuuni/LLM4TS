package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob = mock(DataArchiveRollbackJob.class);
    private JobExecutionContext context = mock(JobExecutionContext.class);

    @Test
    public void testExecute() {
        dataArchiveRollbackJob.execute(context);
    }

    @Test(expected = NullPointerException.class)
    public void testExecuteNullContext() {
        dataArchiveRollbackJob.execute(null);
    }

    @Test
    public void testExecuteContextWithNullResults() {
        when(context.getResults()).thenReturn(null);
        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testExecuteContextWithNullDataArchiveTaskId() {
        when(taskEntity.getParametersJson()).thenReturn(null);
        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testExecuteContextWithInvalidDataArchiveTaskId() {
        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(context);
    }
}