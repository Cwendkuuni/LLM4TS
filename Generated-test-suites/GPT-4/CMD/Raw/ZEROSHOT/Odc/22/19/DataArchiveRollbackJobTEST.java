
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private final JobExecutionContext context = mock(JobExecutionContext.class);
    private final ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
    private final DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
    }

    @Test
    public void testExecuteDataArchiveTaskNotPresent() {
        rollbackParameters.setDataArchiveTaskId(12345L);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteDataArchiveTaskPresent() {
        rollbackParameters.setDataArchiveTaskId(12345L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(10L);
        dlmTask.setTargetDatabaseId(20L);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(Arrays.asList(dlmTask));
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), any(TaskStatus.class));
        verify(dlmTask, times(1)).setId(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(),
                taskEntity.getId(), 1));
        verify(dlmTask, times(1)).setJobType(JobType.ROLLBACK);
        verify(dlmTask, times(1)).setSourceDatabaseId(20L);
        verify(dlmTask, times(1)).setTargetDatabaseId(10L);
        verify(dlmTask, times(1)).setStatus(TaskStatus.PREPARING);
        verify(executorService, times(1)).executeTask(eq(taskEntity.getId()), anyList());
    }
}
