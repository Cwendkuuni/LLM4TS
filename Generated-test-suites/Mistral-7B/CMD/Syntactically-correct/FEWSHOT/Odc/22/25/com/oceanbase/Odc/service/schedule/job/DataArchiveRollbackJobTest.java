package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @After
    public void teardown() {
        // Do nothing
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws Exception {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(taskId);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        String parametersJson = JsonUtils.toJsonString(rollbackParameters);
        scheduleTaskEntity.setParametersJson(parametersJson);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName(jobName);
        dataArchiveTask.setJobGroup(jobGroup);
        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(any(Long.class), any(TaskStatus.class))).thenReturn(true);

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.DONE, getTaskStatus(dlmTasks));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(taskId);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        String parametersJson = JsonUtils.toJsonString(rollbackParameters);
        scheduleTaskEntity.setParametersJson(parametersJson);

        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        // when, then
        dataArchiveRollbackJob.execute(context);
        assertEquals(TaskStatus.FAILED, getTaskStatus(null));
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_ExceptionThrown() throws Exception {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(taskId);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        String parametersJson = JsonUtils.toJsonString(rollbackParameters);
        scheduleTaskEntity.setParametersJson(parametersJson);

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());

        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(any(Long.class), any(TaskStatus.class))).thenThrow(new RuntimeException("Test Exception"));

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        // when, then
        dataArchiveRollbackJob.execute(context);
    }

    private TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
        return dlmTasks != null ? dlmTasks.get(0).getStatus() : null;
    }
}