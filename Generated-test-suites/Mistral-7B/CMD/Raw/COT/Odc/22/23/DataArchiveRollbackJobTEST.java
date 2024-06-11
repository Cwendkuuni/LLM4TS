package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJobTest;
import com.oceanbase.odc.service.schedule.job.scheduleTaskRepository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveRollbackJobTest extends AbstractDlmJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_ValidDataArchiveTask_Success() throws JobExecutionException {
        // given
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testJobName", "testJobGroup", null);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String json = objectMapper.writeValueAsString(rollbackParameters);
        taskEntity.setParametersJson(json);

        List<DlmTask> taskUnits = createDlmTasks(2);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        verifyTasksExecuted(taskUnits);
        verifyTasksStatusUpdated(taskStatus.DONE);
    }

    @Test
    public void testExecute_InvalidDataArchiveTask_Failed() throws JobExecutionException {
        // given
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testJobName", "testJobGroup", null);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(-1L);
        String json = objectMapper.writeValueAsString(rollbackParameters);
        taskEntity.setParametersJson(json);

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        verifyNoTasksExecuted();
        verifyTaskStatusUpdated(taskStatus.FAILED);
    }

    @Test
    public void testExecute_NullDataArchiveTask_Failed() throws JobExecutionException {
        // given
        ScheduleTaskEntity taskEntity = null;

        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(taskEntity));

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        verifyNoTasksExecuted();
        verifyTaskStatusUpdated(taskStatus.FAILED);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_NullTaskUnits_Exception() throws JobExecutionException {
        // given
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testJobName", "testJobGroup", null);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String json = objectMapper.writeValueAsString(rollbackParameters);
        taskEntity.setParametersJson(json);

        List<DlmTask> taskUnits = null;

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        verifyNoTasksExecuted();
        verifyTaskStatusUpdated(taskStatus.FAILED);
    }

    @Test
    public void testExecute_EmptyTaskUnits_Failed() throws JobExecutionException {
        // given
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testJobName", "testJobGroup", null);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String json = objectMapper.writeValueAsString(rollbackParameters);
        taskEntity.setParametersJson(json);

        List<DlmTask> taskUnits = new ArrayList<>();

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        verifyNoTasksExecuted();
        verifyTaskStatusUpdated(taskStatus.FAILED);
    }

    private ScheduleTaskEntity createScheduleTaskEntity(long id, String jobName, String jobGroup, String resultJson) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(id);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        scheduleTaskEntity.setResultJson(resultJson);
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        return scheduleTaskEntity;
    }

    private List<DlmTask> createDlmTasks(int size) {
        List<DlmTask> dlmTasks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            DlmTask dlmTask = new DlmTask();
            dlmTask.setId(i);
            dlmTask.setSourceDatabaseId(i);
            dlmTask.setTargetDatabaseId(i);
            dlmTasks.add(dlmTask);
        }
        return dlmTasks;
    }

    private void verifyTasksExecuted(List<DlmTask> taskUnits) {
        for (DlmTask dlmTask : taskUnits) {
            verify(dataArchiveRollbackJob, times(1)).executeTask(eq(1L), eq(taskUnits));
        }
    }

    private void verifyTasksStatusUpdated(TaskStatus expectedStatus) {
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(expectedStatus));
    }

    private void verifyNoTasksExecuted() {
        verify(dataArchiveRollbackJob, never()).executeTask(anyLong(), anyList());
    }

    private void verifyTaskStatusUpdated(TaskStatus expectedStatus) {
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(expectedStatus));
    }
}