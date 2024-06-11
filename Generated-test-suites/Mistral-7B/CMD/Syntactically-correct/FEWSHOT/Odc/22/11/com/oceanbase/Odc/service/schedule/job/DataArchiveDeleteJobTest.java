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
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParametersBuilder;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
    private List<DlmTask> dlmTasks;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskEntity = new ScheduleTaskEntity();
        dlmTasks = new ArrayList<>();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L).build();
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L).build();
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTasks.add(dlmTask);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(),
                TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(),
                TaskStatus.RUNNING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(),
                TaskStatus.COMPLETED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L).build();
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.FAILED);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTasks.add(dlmTask);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TasksExecutionFailed() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L).build();
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        doThrow(RuntimeException.class).when(dataArchiveDeleteJob)
                .executeTask(dataArchiveClearParameters.getDataArchiveTaskId(), dlmTasks);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TasksExecutionSuccess() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L).build();
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTasks.add(dlmTask);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(),
                TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(),
                TaskStatus.RUNNING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(),
                TaskStatus.COMPLETED);
        verify(dataArchiveDeleteJob, times(1)).executeTask(dataArchiveClearParameters.getDataArchiveTaskId(), dlmTasks);
    }
}