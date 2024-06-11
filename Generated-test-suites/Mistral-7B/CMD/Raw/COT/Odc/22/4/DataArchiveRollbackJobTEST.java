package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        // Initialize mocks and objects here if needed
    }

    @org.junit.jupiter.api.Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        // When & Then
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(createJobExecutionContext(rollbackParameters)));
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @org.junit.jupiter.api.Test
    public void testExecute_DataArchiveTaskFound() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // When
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.execute(createJobExecutionContext(rollbackParameters));

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.DONE));
    }

    private JobExecutionContext createJobExecutionContext(DataArchiveRollbackParameters rollbackParameters) {
        return mock(JobExecutionContext.class);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        // Clean up mocks and objects here if needed
    }

}