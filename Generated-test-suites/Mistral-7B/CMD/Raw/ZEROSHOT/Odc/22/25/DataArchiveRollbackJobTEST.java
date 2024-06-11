package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenStatusUpdatedToFailed() {
        // given
        String dataArchiveTaskId = "123";
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("1");
        scheduleTaskEntity.setParametersJson("{}");

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());

        // when
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById("1", TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_thenStatusUpdatedToDone() {
        // given
        String dataArchiveTaskId = "123";
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("1");
        scheduleTaskEntity.setParametersJson("{}");

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setJobType(JobType.ROLLBACK);
        dlmTask1.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask1);

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        doReturn(dlmTasks).when(dataArchiveRollbackJob).executeTask("1", dlmTasks);

        // when
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById("1", TaskStatus.DONE);
    }

    @Test
    public void testGetTaskStatus_whenAllTasksDone_thenStatusIsDone() {
        // given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        // when
        TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // then
        assertEquals(TaskStatus.DONE, actual);
    }

    @Test
    public void testGetTaskStatus_whenOneTaskFailed_thenStatusIsFailed() {
        // given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setStatus(TaskStatus.FAILED);
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        // when
        TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // then
        assertEquals(TaskStatus.FAILED, actual);
    }
}