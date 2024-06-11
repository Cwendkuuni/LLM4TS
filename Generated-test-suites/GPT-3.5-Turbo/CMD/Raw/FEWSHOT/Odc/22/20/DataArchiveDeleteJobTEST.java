package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    public void testExecute() {
        // Test scenario when dataArchiveTask is not present
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));

        // Test scenario when dataArchiveTask status is not DONE
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.CREATED);
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(2)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));

        // Test scenario with valid dataArchiveTask
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson(JsonUtils.toJson(new DlmTask()));
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(jobExecutionContext);
        Mockito.verify(scheduleTaskRepository, Mockito.times(3)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.PREPARING));
    }
}