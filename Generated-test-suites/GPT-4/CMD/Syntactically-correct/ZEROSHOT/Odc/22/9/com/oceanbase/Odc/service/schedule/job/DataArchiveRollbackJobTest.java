package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void init() {
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
    }

    @Test
    public void testExecute() {
        // Mocking related methods
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(dlmTask);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(1);

        // Test method
        dataArchiveRollbackJob.execute(context);

        // Verify methods were called
        verify(scheduleTaskRepository).findById(1L);
        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
        verify(dlmTask).setJobType(JobType.ROLLBACK);
        verify(dlmTask).setStatus(TaskStatus.PREPARING);
    }
}
