
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext context;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("jobname");
        taskEntity.setJobGroup("jobgroup");
        taskEntity.setResult(TaskStatus.SUCCESS.getDescription());

        DataArchiveRollbackParameters dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveRollbackParameters.setDataArchiveTaskId(1L);

        when(context.getResult()).thenReturn(taskEntity);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(123L);
        dlmTask.setJobType(JobType.PREFERRED);
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

        dataArchiveRollbackJob.execute(context);

        assertNotNull(dataArchiveRollbackJob.getJobThread());
        assertNotNull(dlmTask.getTargetDatabaseId());
    }
}
