package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob() {
            {
                this.scheduleTaskRepository = scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        doReturn(Optional.empty())
                .when(scheduleTaskRepository)
                .findById(rollbackParameters.getDataArchiveTaskId());

        dataArchiveRollbackJob.execute(new JobExecutionContext() {});

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName("DataArchiveRollbackJob");
        dataArchiveTask.setJobGroup("default");
        dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":1,\"dataArchiveRollbackParameters\":{\"dataSource\":\"ds1\",\"dataArchiveRollbackParameters\":[{\"sourceDatabaseId\": 1,\"targetDatabaseId\": 2,\"jobType\": \"ROLLBACK\"}],\"startTime\":\"2023-06-26 20:05:00\"}}");

        doReturn(Optional.of(dataArchiveTask))
                .when(scheduleTaskRepository)
                .findById(rollbackParameters.getDataArchiveTaskId());

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setId(1L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

        doReturn(taskUnits)
                .when(dataArchiveRollbackJob)
                .executeTask(anyLong(), anyList());

        TaskStatus expectedTaskStatus = TaskStatus.DONE;

        dataArchiveRollbackJob.execute(new JobExecutionContext() {});

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(1L), eq(expectedTaskStatus));
    }
}