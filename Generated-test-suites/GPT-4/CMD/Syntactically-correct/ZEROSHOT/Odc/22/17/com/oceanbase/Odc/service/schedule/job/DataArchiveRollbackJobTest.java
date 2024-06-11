
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.fasterxml.jackson.core.type.TypeReference;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity, dataArchiveTask;

    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Mock
    private List<DlmTask> taskUnits;

    @Mock
    private DlmTask taskUnit;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("someJson");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("someJson");
        when(taskUnit.getSourceDatabaseId()).thenReturn(1L);
        when(taskUnit.getTargetDatabaseId()).thenReturn(1L);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        taskUnits = Arrays.asList(taskUnit);

        dataArchiveRollbackJob.execute(context);

        verify(taskUnit, times(1)).setSourceDatabaseId(anyLong());
        verify(taskUnit, times(1)).setTargetDatabaseId(anyLong());
        verify(taskUnit, times(1)).setJobType(any(JobType.class));
        verify(taskUnit, times(1)).setStatus(any(TaskStatus.class));
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
        verify(taskEntity, times(1)).getId();
    }

    @Test
    public void testExecuteDataArchiveTaskNotPresent() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("someJson");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
}
