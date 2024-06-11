package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity, dataArchiveTask;
    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setUp() throws Exception {
       MockitoAnnotations.initMocks(this);
       dataArchiveRollbackJob = new DataArchiveRollbackJob();
       when(context.getResult()).thenReturn(taskEntity);
       when(taskEntity.getParametersJson()).thenReturn("{}");
    }

    @Test
    public void whenDataArchiveTaskNotPresent_thenUpdateTaskFailed() {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void whenDataArchiveTaskPresent_thenProceedAsExpected() {
        List<DlmTask> taskUnits = new ArrayList<>();

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("[]");
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), getTaskStatus(taskUnits));
    }
}