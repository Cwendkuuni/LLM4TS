package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters.DataArchiveClearParametersBuilder;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters.DataArchiveTaskId;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.test.AbstractDlmJobTest;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest extends AbstractDlmJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void init() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    }

    @Test
    public void testExecuteWithInvalidDataArchiveTaskId() throws JobExecutionException {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersBuilder.aDataArchiveClearParameters()
                .withDataArchiveTaskId(new DataArchiveTaskId("invalidId"))
                .build();
        JobExecutionContext context = mock(JobExecutionContext.class);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId().getValue()))
                .thenReturn(Optional.empty());

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // when then
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithAlreadyFinishedDataArchiveTask() throws JobExecutionException {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersBuilder.aDataArchiveClearParameters()
                .withDataArchiveTaskId(new DataArchiveTaskId("1"))
                .build();
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getId()).thenReturn(1L);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId().getValue()))
                .thenReturn(Optional.of(dataArchiveTask));

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // when then
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, never())
                .updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithUnfinishedDataArchiveTask() throws JobExecutionException {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersBuilder.aDataArchiveClearParameters()
                .withDataArchiveTaskId(new DataArchiveTaskId("1"))
                .build();
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getId()).thenReturn(1L);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.FAILED);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId().getValue()))
                .thenReturn(Optional.of(dataArchiveTask));

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // when then
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithValidDataArchiveTask() throws JobExecutionException {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersBuilder.aDataArchiveClearParameters()
                .withDataArchiveTaskId(new DataArchiveTaskId("1"))
                .build();
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getId()).thenReturn(1L);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("1");
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("2");

        List<DlmTask> dlmTasks = Arrays.asList(dlmTask1, dlmTask2);
        String jsonString = JsonUtils.toJson(dlmTasks);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId().getValue()))
                .thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn(jsonString);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // when then
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.PREPARING));
        verify(scheduleTaskRepository, never())
                .updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }
}