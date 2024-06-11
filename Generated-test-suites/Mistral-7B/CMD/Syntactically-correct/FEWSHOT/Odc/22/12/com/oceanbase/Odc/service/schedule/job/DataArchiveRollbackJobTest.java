package com.oceanbase.odc.service.schedule.job;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private DlmTask taskUnit;
    private ScheduleTaskEntity dataArchiveTask;
    private ScheduleTaskEntity taskEntity;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        taskUnit = new DlmTask();
        dataArchiveTask = new ScheduleTaskEntity();
        taskEntity = new ScheduleTaskEntity();
        taskUnits = new ArrayList<>();
        taskUnits.add(taskUnit);
    }

    @Test
    public void testExecute_NullContext() {
        assertThrows(NullPointerException.class, () -> dataArchiveRollbackJob.execute(null));
    }

    @Test
    public void testExecute_NullTaskEntity() {
        taskEntity = null;
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        assertThrows(NullPointerException.class, () -> dataArchiveRollbackJob.execute(new JobExecutionContext() {}));
    }

    @Test
    public void testExecute_EmptyTaskUnits() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenAnswer(i -> null);

        dataArchiveRollbackJob.execute(new JobExecutionContext() {});

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_NonExistentTask() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(new JobExecutionContext() {}));

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_Success() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenAnswer(i -> null);

        dataArchiveRollbackJob.execute(new JobExecutionContext() {});

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }
}