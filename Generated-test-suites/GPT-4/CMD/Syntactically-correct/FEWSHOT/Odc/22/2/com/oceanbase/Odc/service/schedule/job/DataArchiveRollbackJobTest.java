
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    @Mock
    DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        String taskId = "10";
        
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":\"10\"}");
        when(scheduleTaskRepository.findById("10").orElse(null)).thenReturn(Optional.of(dataArchiveTask));

        // Assume we have DlmTask
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(100L);
        taskUnit.setTargetDatabaseId(200L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.empty());
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);

        verify(taskEntity, times(1)).setId(eq(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(), taskEntity.getId(), taskUnits.size())));
        verify(taskEntity, times(1)).setSourceDatabaseId(eq(taskUnit.getTargetDatabaseId()));
        verify(taskEntity, times(1)).setTargetDatabaseId(eq(taskUnit.getSourceDatabaseId()));
        verify(taskEntity, times(1)).setJobType(eq(JobType.ROLLBACK));
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), any(TaskStatus.class));
    }
}
