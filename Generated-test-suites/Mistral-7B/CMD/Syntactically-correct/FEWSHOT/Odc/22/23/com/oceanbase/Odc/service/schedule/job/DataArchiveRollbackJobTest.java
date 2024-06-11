package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = getDataArchiveRollbackParameters();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class), taskEntity);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = getDataArchiveRollbackParameters();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setJobName("testJobName");
        dataArchiveTask.setJobGroup("testJobGroup");
        dataArchiveTask.setResultJson(objectMapper.writeValueAsString(getDlmTasks()));

        doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class), taskEntity);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    private List<DlmTask> getDlmTasks() {
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setJobType(JobType.BACKUP);
        dlmTask1.setStatus(TaskStatus.DONE);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setSourceDatabaseId(2L);
        dlmTask2.setTargetDatabaseId(1L);
        dlmTask2.setJobType(JobType.ROLLBACK);
        dlmTask2.setStatus(TaskStatus.PREPARING);

        return new ArrayList<DlmTask>() {{
            add(dlmTask1);
            add(dlmTask2);
        }};
    }

    private DataArchiveRollbackParameters getDataArchiveRollbackParameters() {
        return new DataArchiveRollbackParameters();
    }
}