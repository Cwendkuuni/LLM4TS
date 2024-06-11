
package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class DataArchiveRollbackJobTest {

    @MockBean
    private JobExecutionContext context;

    @MockBean
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @MockBean
    private ScheduleTaskEntity taskEntity;

    @MockBean
    private ScheduleTaskEntity dataArchiveTask;

    @MockBean
    private DataArchiveRollbackParameters rollbackParameters;

    @MockBean
    private DlmTask taskUnit;

    @Test
    public void executeTest() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));

        Mockito.when(scheduleTaskRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(dataArchiveTask));

        Mockito.when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(taskUnit));
        Mockito.when(taskUnit.getSourceDatabaseId()).thenReturn(1L);
        Mockito.when(dataArchiveTask.getResultJson())
                .thenReturn(JsonUtils.toJson(Arrays.asList(taskUnit)));

        dataArchiveRollbackJob.execute(context);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyString(), any(TaskStatus.class));
        assertEquals(taskUnit.getJobType(), JobType.ROLLBACK);
    }
}
