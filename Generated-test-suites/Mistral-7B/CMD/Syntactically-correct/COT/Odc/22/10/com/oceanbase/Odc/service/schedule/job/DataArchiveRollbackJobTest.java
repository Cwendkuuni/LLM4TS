package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() throws Exception {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // Prepare mock objects
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        ObjectMapper objectMapper = new ObjectMapper();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String parametersJson = objectMapper.writeValueAsString(rollbackParameters);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        // Test execution
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Verify results
        verifyTaskStatusUpdated(TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // Prepare mock objects
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        ObjectMapper objectMapper = new ObjectMapper();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String parametersJson = objectMapper.writeValueAsString(rollbackParameters);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // Test execution
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Verify results
        verifyTaskStatusUpdated(TaskStatus.FAILED);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_TaskExecutionException() throws Exception {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // Prepare mock objects
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        ObjectMapper objectMapper = new ObjectMapper();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String parametersJson = objectMapper.writeValueAsString(rollbackParameters);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.FAILED);
        taskUnits.add(taskUnit);
        when(dataArchiveRollbackJob.executeTask(taskEntity.getId(), taskUnits))
                .thenThrow(new JobExecutionException("Test Error"));

        // Test execution
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Verify results
        verifyTaskStatusUpdated(TaskStatus.FAILED);
    }

    private void verifyTaskStatusUpdated(TaskStatus expectedTaskStatus) {
        if (expectedTaskStatus != null) {
            assertEquals(expectedTaskStatus, taskEntity.getStatus());
        } else {
            verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), eq(null));
        }
    }
}