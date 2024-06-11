
package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;


@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    @Mock
    DataArchiveClearParameters dataArchiveClearParameters;

    @InjectMocks
    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    public void testExecute_notFound() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_notDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.STARTING); 

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_success() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn("[{}]");

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(any());
    }
}
