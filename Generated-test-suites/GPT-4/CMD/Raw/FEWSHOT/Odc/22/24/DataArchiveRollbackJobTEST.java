
package com.oceanbase.odc.service.schedule.job;

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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DlmTask taskUnit;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        taskEntity = new ScheduleTaskEntity();
        rollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveTask = new ScheduleTaskEntity();
        taskUnit = new DlmTask();

        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
    }

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.PREPARING, taskUnit.getStatus());
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), taskUnit.getStatus());
    }

    @Test
    public void testExecute_WithTaskNotFound() {
        when(context.getResult()).thenReturn(taskEntity);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
}
