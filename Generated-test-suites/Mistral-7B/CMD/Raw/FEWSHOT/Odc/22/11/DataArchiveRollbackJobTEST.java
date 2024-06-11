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
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters.DataArchiveRollbackParametersBuilder;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters.RollbackParametersBuilder;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParametersBuilder()
                .withDataArchiveTaskId(1L)
                .build();

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // when
        dataArchiveRollbackJob.execute(null);

        // then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParametersBuilder()
                .withDataArchiveTaskId(1L)
                .build();

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson("{}");

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        // when
        dataArchiveRollbackJob.execute(null);

        // then
        for (DlmTask dlmTask : dlmTasks) {
            verify(dlmTask, times(1)).setId(anyLong());
            verify(dlmTask, times(1)).setSourceDatabaseId(eq(dlmTask.getTargetDatabaseId()));
            verify(dlmTask, times(1)).setTargetDatabaseId(notNullValue());
            verify(dlmTask, times(1)).setJobType(eq(JobType.ROLLBACK));
            verify(dlmTask, times(1)).setStatus(eq(TaskStatus.DONE));
        }

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(dataArchiveTask.getId()), notNullValue());
    }
}