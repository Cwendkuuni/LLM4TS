package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob() {
            {
                this.scheduleTaskRepository = scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        // Given
        String dataArchiveTaskId = "dataArchiveTaskId";
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);
        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTasks.add(dlmTask);
        String jsonString = JsonUtils.toJsonString(dlmTasks);
        when(scheduleTaskEntity.getResultJson()).thenReturn(jsonString);

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        String dataArchiveTaskId = "dataArchiveTaskId";
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);
        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testGetTaskStatus() {
        // Given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask().setStatus(TaskStatus.PREPARING);
        DlmTask dlmTask2 = new DlmTask().setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        // When
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // Then
        assertEquals(TaskStatus.DONE, taskStatus);
    }
}