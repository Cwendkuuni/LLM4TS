package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
        dataArchiveTaskOption.get().setId(10L);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(new DataArchiveRollbackParameters());
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}))).thenReturn(taskUnits);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(1)).updateStatusById(eq(1L), any(TaskStatus.class));
    }
}