package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_taskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_taskAlreadyFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_taskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_success() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTasks.add(dlmTask);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.SUCCESS));
    }
}