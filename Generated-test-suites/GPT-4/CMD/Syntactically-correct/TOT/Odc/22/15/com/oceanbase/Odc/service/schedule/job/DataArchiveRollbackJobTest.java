package com.oceanbase.odc.service.schedule.job;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity mockTaskEntity;
    private JobExecutionContext mockContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        mockContext = Mockito.mock(JobExecutionContext.class);
        mockTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        Mockito.when(mockContext.getResult()).thenReturn(mockTaskEntity);
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        Mockito.when(mockTaskEntity.getId()).thenReturn(1L);
        Mockito.when(mockTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": 1}");
        
        dataArchiveRollbackJob.execute(mockContext);

        Mockito.verify(mockTaskEntity).updateStatusById(1L, TaskStatus.FAILED);
    }

    // add more test cases here, for example test when dataArchiveTask is found, when taskUnits are null, when taskUnits are not null, exception cases, etc.
}