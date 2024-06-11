package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParametersBuilder;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Slf4j
public class DataArchiveDeleteJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper jsonUtils;

    @Before
    public void setUp() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        jsonUtils = Mockito.spy(new ObjectMapper());
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L)
                .build();

        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        dataArchiveDeleteJob.execute(context);

        Mockito.verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyDone() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L)
                .build();

        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        dataArchiveDeleteJob.execute(context);

        Mockito.verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testExecute_PrepareTasksForClear() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L)
                .build();

        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = Mockito.mock(DlmTask.class);
        dlmTask.setId(1L);
        dlmTasks.add(dlmTask);

        Mockito.when(jsonUtils.fromJson(dataArchiveTask.getResultJson(), List.class))
                .thenReturn(dlmTasks);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        dataArchiveDeleteJob.execute(context);

        Mockito.verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.PREPARING));
    }

    @Test
    public void testExecute_UpdateTaskStatus() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(1L)
                .build();

        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = Mockito.mock(DlmTask.class);
        dlmTask.setId(1L);
        dlmTasks.add(dlmTask);

        Mockito.when(jsonUtils.fromJson(dataArchiveTask.getResultJson(), List.class))
                .thenReturn(dlmTasks);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        dataArchiveDeleteJob.execute(context);

        TaskStatus taskStatus = TaskStatus.SUCCESS;
        Mockito.when(dataArchiveDeleteJob.getTaskStatus(dlmTasks))
                .thenReturn(taskStatus);

        Mockito.verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(taskStatus));
    }
}