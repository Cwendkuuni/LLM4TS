
package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {
    
    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId("taskId_1");

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));

        // test for dataArchiveTask not found
        when(scheduleTaskRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository).updateStatusById(Mockito.anyString(), TaskStatus.FAILED);

        // test for dataArchiveTask not finished
        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.EXECUTING);
        when(scheduleTaskRepository.findById(Mockito.anyString())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository, Mockito.times(2)).updateStatusById(Mockito.anyString(), TaskStatus.FAILED);

        // test for dataArchiveTask finished
        DlmTask dlmTask = new DlmTask();
        dlmTask.setResultJson(JsonUtils.toJson(Arrays.asList(dlmTask)));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository).updateStatusById(Mockito.anyString(), TaskStatus.PREPARING);
    }
}
