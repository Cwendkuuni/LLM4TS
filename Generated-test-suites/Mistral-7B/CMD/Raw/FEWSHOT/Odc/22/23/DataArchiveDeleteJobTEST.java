package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob() {
            {
                this.scheduleTaskRepository = scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // when
        dataArchiveDeleteJob.execute(null);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setStatus(TaskStatus.CREATING);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        // when
        dataArchiveDeleteJob.execute(null);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        // when
        dataArchiveDeleteJob.execute(null);

        // then
        for (DlmTask dlmTask : dlmTasks) {
            verify(scheduleTaskRepository, times(1)).save(any(DlmTask.class));
            verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.PREPARING));
        }
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), any(TaskStatus.class));
    }
}