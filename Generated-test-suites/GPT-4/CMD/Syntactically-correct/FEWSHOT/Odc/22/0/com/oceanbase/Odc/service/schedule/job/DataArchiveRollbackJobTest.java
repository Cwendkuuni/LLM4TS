
package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    JobExecutionContext context;
    DataArchiveRollbackJob job;
    ScheduleTaskEntity taskEntity;
    DataArchiveRollbackParameters parameters;
    ScheduleTaskEntity dataArchiveTask;
    List<DlmTask> dlmTasks;
    DlmTask dlmTask;

    @Before
    public void setUp() {
        context = Mockito.mock(JobExecutionContext.class);
        job = new DataArchiveRollbackJob();
        taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        parameters = Mockito.mock(DataArchiveRollbackParameters.class);
        dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        dlmTask = Mockito.mock(DlmTask.class);
        dlmTasks = Arrays.asList(dlmTask);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(dlmTasks));
        when(parameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(dataArchiveTask));
        when(dlmTask.getSourceDatabaseId()).thenReturn(2L);
        when(dlmTask.getTargetDatabaseId()).thenReturn(3L);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        
        job.execute(context);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
        verify(dlmTask, times(1)).setId(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()));
        verify(dlmTask, times(1)).setSourceDatabaseId(3L);
        verify(dlmTask, times(1)).setTargetDatabaseId(2L);
        verify(dlmTask, times(1)).setJobType(JobType.ROLLBACK);
        verify(dlmTask, times(1)).setStatus(TaskStatus.DONE);
    }
}
