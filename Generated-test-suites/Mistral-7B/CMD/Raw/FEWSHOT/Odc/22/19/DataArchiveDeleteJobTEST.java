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

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveClearParameters dataArchiveClearParameters;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> dlmTasks;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123456");

        scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        dlmTasks = new ArrayList<>();
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        // when
        dataArchiveDeleteJob.execute(null);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        // given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.CREATED);

        // when
        dataArchiveDeleteJob.execute(null);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        // given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        DlmTask dlmTask = new DlmTask();
        dlmTasks.add(dlmTask);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        // when
        dataArchiveDeleteJob.execute(null);

        // then
        verify(taskUnit, times(dlmTasks.size())).setId(anyString());
        verify(taskUnit, times(dlmTasks.size())).setJobType(eq(JobType.DELETE));
        verify(taskUnit, times(dlmTasks.size())).setStatus(eq(TaskStatus.PREPARING));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(scheduleTaskEntity.getId()), any(TaskStatus.class));
    }
}