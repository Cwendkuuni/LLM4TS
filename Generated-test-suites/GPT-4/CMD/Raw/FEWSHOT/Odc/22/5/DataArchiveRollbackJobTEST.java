package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Test
    public void testExecute_whenDataArchiveTaskNotFound() {

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveRollbackParameters(2L)));
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        // Verify task status is set to FAILED
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound() {

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveRollbackParameters(2L)));
        when(taskEntity.getJobName()).thenReturn("testJob");
        when(taskEntity.getJobGroup()).thenReturn("testGroup");
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(dataArchiveTask));
                
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(3L);
        dlmTask.setTargetDatabaseId(4L);
        dlmTask.setStatus(TaskStatus.PREPARING);

        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(dlmTask)));

        dataArchiveRollbackJob.execute(context);

        // Verify task unit data is swapped and status is changed
        assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
        assertEquals(TaskStatus.DONE, dlmTask.getStatus());
        assertEquals(Long.valueOf(4L), dlmTask.getSourceDatabaseId());
        assertEquals(Long.valueOf(3L), dlmTask.getTargetDatabaseId());
        
        // Verify task status is updated based on task units status
        assertEquals(taskEntity.getStatus(), dataArchiveRollbackJob.getTaskStatus(Arrays.asList(dlmTask)));
    }
}