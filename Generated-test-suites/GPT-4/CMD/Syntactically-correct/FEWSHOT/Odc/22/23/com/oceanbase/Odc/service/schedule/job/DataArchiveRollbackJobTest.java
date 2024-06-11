package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @Test
    public void testDataArchiveRollbackJob() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // Mocking
        JobExecutionContext mockJobExecutionContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity mockTaskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        DlmTask task = new DlmTask();

        when(mockJobExecutionContext.getResult()).thenReturn(mockTaskEntity);
        when(mockTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(mockTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(task)));

        dataArchiveRollbackJob.execute(mockJobExecutionContext);

        // Assertion of final TaskStatus
        TaskStatus expectedTaskStatus = task.getStatus() == TaskStatus.PREPARING ? TaskStatus.DONE : TaskStatus.PREPARING;
        assertEquals(expectedTaskStatus, task.getStatus());

        // Assertion of JobType
        JobType expectedJobType = JobType.ROLLBACK;
        assertEquals(expectedJobType, task.getJobType());
        
        // Assertion of SourceDatabaseId and TargetDatabaseId swap
        Long temp = task.getSourceDatabaseId();
        assertEquals(temp, task.getTargetDatabaseId());
    }
}