
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
    }

    @Test
    public void testExecuteWithNoDataArchiveTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L)).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.getScheduleTaskRepository(), times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);
        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.getScheduleTaskRepository(), times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithDataArchiveTaskDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask());
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.getScheduleTaskRepository(), times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}
