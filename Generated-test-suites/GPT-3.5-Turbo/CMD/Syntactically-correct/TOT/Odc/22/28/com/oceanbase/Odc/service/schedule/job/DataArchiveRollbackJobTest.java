
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask())));
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}
