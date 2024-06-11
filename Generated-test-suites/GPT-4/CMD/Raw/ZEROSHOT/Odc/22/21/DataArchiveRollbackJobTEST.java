
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

public class DataArchiveRollbackJobTest {
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private DataArchiveRollbackParameters rollbackParameters;
    private List<DlmTask> taskUnits;
    private DlmTask taskUnit;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = Mockito.mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);
        rollbackParameters = mock(DataArchiveRollbackParameters.class);
        taskUnit = mock(DlmTask.class);
        taskUnits = Arrays.asList(taskUnit);
    }

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("somejson");
        when(JsonUtils.fromJson("somejson", DataArchiveRollbackParameters.class)).thenReturn(rollbackParameters);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("1234");
        //In these tests we assume that the data archive task is always present.
        when(scheduleTaskRepository.findById("1234")).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("json");
        when(JsonUtils.fromJson("json", new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        when(taskUnit.getSourceDatabaseId()).thenReturn(Long.valueOf(1));
        when(taskUnit.getTargetDatabaseId()).thenReturn(Long.valueOf(2));
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).findById("1234");
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), any());
    }

    @Test
    public void testExecute_WhenDataArchiveTaskNotPresent() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("somejson");
        when(JsonUtils.fromJson("somejson", DataArchiveRollbackParameters.class)).thenReturn(rollbackParameters);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("1234");
        when(scheduleTaskRepository.findById("1234")).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }
}
