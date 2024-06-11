package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void clean() {
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenFastFailed() {
        // given
        String dataArchiveTaskId = "1";
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());

        // when
        dataArchiveRollbackJob.execute(null);

        // then
        assertEquals(TaskStatus.FAILED, getLastStatus(scheduleTaskEntity));
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_thenExecuteTaskAndUpdateStatus() {
        // given
        String dataArchiveTaskId = "1";
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> dlmTasks = Arrays.asList(
                new DlmTask(),
                new DlmTask()
        );

        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(dlmTasks));

        // when
        dataArchiveRollbackJob.execute(null);

        // then
        verifyTasksExecuted(dlmTasks);
        assertEquals(TaskStatus.DONE, getLastStatus(scheduleTaskEntity));
    }

    private void verifyTasksExecuted(List<DlmTask> dlmTasks) {
        for (DlmTask dlmTask : dlmTasks) {
            assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
            assertEquals(TaskStatus.DONE, dlmTask.getStatus());
        }
    }

    private TaskStatus getLastStatus(ScheduleTaskEntity scheduleTaskEntity) {
        return JsonUtils.fromJson(scheduleTaskEntity.getParametersJson(), DataArchiveRollbackParameters.class)
                .getStatus();
    }
}