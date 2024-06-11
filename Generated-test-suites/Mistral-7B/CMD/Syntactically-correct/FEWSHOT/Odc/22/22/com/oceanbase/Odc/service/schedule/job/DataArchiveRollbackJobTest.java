package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
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
import com.oceanbase.tools.migrator.common.utils.TaskStatusUtils;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123");

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(new ScheduleTaskEntity());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);

        List<DlmTask> dlmTasks = List.of(new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getParametersJson()).thenReturn("{}");
        when(JsonUtils.fromJson(dataArchiveTask.getParametersJson(), DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(dataArchiveTask);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.anyLong(), eq(TaskStatus.DONE));

        DlmTask dlmTask = dlmTasks.get(0);
        assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
        assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_TaskExecutionFailed() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);

        List<DlmTask> dlmTasks = List.of(new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getParametersJson()).thenReturn("{}");
        when(JsonUtils.fromJson(dataArchiveTask.getParametersJson(), DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(dataArchiveTask);

        doThrow(new RuntimeException("Task execution failed."))
                .when(dataArchiveRollbackJob)
                .executeTask(ArgumentMatchers.anyLong(), ArgumentMatchers.anyList());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.anyLong(), eq(TaskStatus.FAILED));
    }

    private static <T> T eq(T value) {
        return ArgumentMatchers.eq(value);
    }
}