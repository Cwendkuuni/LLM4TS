package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduleTaskRepository", mock(ScheduleTaskRepository.class));
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setResultJson("[]");
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        taskEntity.setParametersJson(parametersJson);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson("[]");
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
        when(dataArchiveRollbackJob.getScheduleTaskRepository().findById(2L)).thenReturn(dataArchiveTaskOption);

        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setId(1L);
        taskUnit.setJobType(JobType.ROLLBACK);
        taskUnit.setStatus(TaskStatus.PREPARING);

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(taskUnit);
        String taskUnitsJson = JsonUtils.toJson(taskUnits);

        when(dataArchiveTask.getResultJson()).thenReturn(taskUnitsJson);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.DONE, taskUnit.getStatus());
        assertEquals(TaskStatus.DONE, dataArchiveTask.getStatus());
    }
}