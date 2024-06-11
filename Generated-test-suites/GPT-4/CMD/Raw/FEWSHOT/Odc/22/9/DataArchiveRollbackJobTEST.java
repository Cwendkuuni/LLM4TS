
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import lombok.extern.slf4j.Slf4j;     

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Test
    public void testExecute() {
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        DataArchiveRollbackParameters dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveRollbackParameters));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setJobType(JobType.DATA_ARCHIVE);
        dlmTask.setStatus(TaskStatus.PREPARING);
        String dlmTaskJson = JsonUtils.toJson(Arrays.asList(dlmTask));

        when(scheduleTaskEntity.getResultJson()).thenReturn(dlmTaskJson);

        ScheduleTaskEntity taskDataArchive = mock(ScheduleTaskEntity.class);
        Optional<ScheduleTaskEntity> taskDataArchiveOption = Optional.of(taskDataArchive);

        when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId())).thenReturn(taskDataArchiveOption);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
    }
}
