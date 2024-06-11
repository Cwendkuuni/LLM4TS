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
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private List<DlmTask> dlmTasks;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dlmTasks = new ArrayList<>();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_Success() {
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class)))
                .thenReturn(dataArchiveClearParameters);
        when(JsonUtils.fromJson(anyString(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);
        doNothing().when(dataArchiveDeleteJob).executeTask(anyLong(), anyList());

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), isA(TaskStatus.class));
    }
}