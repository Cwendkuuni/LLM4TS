package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class DataArchiveRollbackJobTest {

    @Mock
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private DlmTask dlmTask;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dlmTask = new DlmTask();
    }

    @Test
    public void testExecute() {

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1);

        when(scheduleTaskEntity.getParametersJson()).thenReturn(
                "{\"dataArchiveTaskId\":1}"
        );

        when(scheduleTaskEntity.getId()).thenReturn(1);
        when(scheduleTaskEntity.getJobName()).thenReturn("jobName");
        when(scheduleTaskEntity.getJobGroup()).thenReturn("jobGroup");
        when(scheduleTaskEntity.getResultJson()).thenReturn(
                "[{\"sourceDatabaseId\":1, \"targetDatabaseId\":2, \"status\": \"PREPARING\"}]"
        );

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);
        
        verify(scheduleTaskEntity, times(1)).getId();
        verify(scheduleTaskEntity, times(1)).getResultJson();
        verify(scheduleTaskEntity, times(1)).getParametersJson();
        verify(jobExecutionContext, times(1)).getResult();
    }
}
