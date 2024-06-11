package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity resultEntity;
    private DataArchiveClearParameters parameters;

    @Before
    public void setUp() {
       dataArchiveDeleteJob = new DataArchiveDeleteJob();
       context = mock(JobExecutionContext.class);
       resultEntity = mock(ScheduleTaskEntity.class);
       parameters = new DataArchiveClearParameters();
       when(context.getResult()).thenReturn(resultEntity);
       when(resultEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
    }

    @Test
    public void testDataArchiveTaskNotFound() {
       parameters.setDataArchiveTaskId(1);
       when(dataArchiveDeleteJob.scheduleTaskRepository.findById(1)).thenReturn(Optional.empty());
       dataArchiveDeleteJob.execute(context);
       verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testDataArchiveTaskNotDone() {
       parameters.setDataArchiveTaskId(2);
       ScheduleTaskEntity dataArchiveTaskEntity = mock(ScheduleTaskEntity.class);
       when(dataArchiveTaskEntity.getStatus()).thenReturn(TaskStatus.PREPARE);
       when(dataArchiveDeleteJob.scheduleTaskRepository.findById(2)).thenReturn(Optional.of(dataArchiveTaskEntity));
       dataArchiveDeleteJob.execute(context);
       verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithValidTaskUnits() {
        parameters.setDataArchiveTaskId(3);
        ScheduleTaskEntity dataArchiveTaskEntity = mock(ScheduleTaskEntity.class);
        when(dataArchiveTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        String jsonTasks = JsonUtils.toJson(
            Arrays.asList(mock(DlmTask.class), mock(DlmTask.class))
        );
        when(dataArchiveTaskEntity.getResultJson()).thenReturn(jsonTasks);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(3))
            .thenReturn(Optional.of(dataArchiveTaskEntity));
        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyInt(), any());
    }
}