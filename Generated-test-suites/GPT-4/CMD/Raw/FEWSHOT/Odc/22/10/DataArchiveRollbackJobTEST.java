package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private Optional<ScheduleTaskEntity> dataArchiveTaskOption;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private JobExecutionContext context;

    @Mock
    private DlmTask dlmTask;

    @Test
    public void testExecute() throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);
        when(dataArchiveTaskOption.get()).thenReturn(dataArchiveTask);

        List<DlmTask> taskUnits = Arrays.asList(dlmTask);
        when(dlmTask.getSourceDatabaseId()).thenReturn(1L);
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt())).thenReturn("test");
        when(getTaskStatus(taskUnits)).thenReturn(TaskStatus.DONE);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.DONE));
    }
}