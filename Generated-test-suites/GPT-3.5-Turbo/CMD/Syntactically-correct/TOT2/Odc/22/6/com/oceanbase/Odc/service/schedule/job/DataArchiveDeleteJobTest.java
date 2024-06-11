
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_SetFailedStatus() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{}");

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone_SetFailedStatus() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters("dataArchiveTaskId")));

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.FAILED)));

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_PrepareTasksForClear_ExecuteTaskAndUpdateStatus() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters("dataArchiveTaskId")));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Collections.singletonList(new DlmTask())));

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.DONE));
    }
}
