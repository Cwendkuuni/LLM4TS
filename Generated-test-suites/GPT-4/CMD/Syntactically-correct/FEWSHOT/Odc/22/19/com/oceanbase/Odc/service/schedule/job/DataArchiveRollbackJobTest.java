package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_NoDataArchiveTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_WithDataArchiveTask() {
        List<DlmTask> expectedTaskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        String taskUnitsJson = "EXPECTED_JSON";

        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getParametersJson()).thenReturn(taskUnitsJson);
        when(taskEntity.getResultJson()).thenReturn(taskUnitsJson);
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(JsonUtils.fromJson(taskUnitsJson, new TypeReference<List<DlmTask>>() {})).thenReturn(expectedTaskUnits);
        when(JsonUtils.fromJson(taskUnitsJson, DataArchiveRollbackParameters.class)).thenReturn(rollbackParameters);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}