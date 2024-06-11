
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = Mockito.mock(JobExecutionContext.class);
        taskEntity = new ScheduleTaskEntity();
    }

    @Test(expected = JobExecutionException.class)
    public void testExecuteWithException() throws JobExecutionException {
        when(context.getResult()).thenReturn(null);
        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("1");

        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setStatus(TaskStatus.PREPARING);

        List<DlmTask> taskUnits = Arrays.asList(dlmTask);

        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), any());
        assertNotNull(dlmTask.getSourceDatabaseId());
        assertNotNull(dlmTask.getTargetDatabaseId());
        assertNotNull(dlmTask.getStatus());
    }
}
