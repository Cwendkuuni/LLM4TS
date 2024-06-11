
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext mockContext;
    @Mock
    private ScheduleTaskRepository mockRepository;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(mockRepository);
    }

    @Test
    public void testExecute_WhenDataArchiveTaskNotPresent_ShouldUpdateStatusToFailed() {
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));

        when(mockContext.getResult()).thenReturn(taskEntity);
        when(mockRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(mockContext);

        verify(mockRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_WhenDataArchiveTaskStatusNotDone_ShouldUpdateStatusToFailed() {
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(mockContext.getResult()).thenReturn(taskEntity);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(mockContext);

        verify(mockRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_WhenDataArchiveTaskDone_ShouldUpdateStatusToTaskStatus() {
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());

        when(mockContext.getResult()).thenReturn(taskEntity);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(mockRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(mockContext);

        verify(mockRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.DONE));
        verify(mockRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.PREPARING));
    }

}
