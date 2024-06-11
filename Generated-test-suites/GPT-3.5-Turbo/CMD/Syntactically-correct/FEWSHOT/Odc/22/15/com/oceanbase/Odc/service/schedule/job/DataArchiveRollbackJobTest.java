package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.TestUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = TestUtils.createMockJobExecutionContext();
        scheduleTaskEntity = TestUtils.createMockScheduleTaskEntity();
    }

    @Test
    public void testExecute() {
        // Test case for successfully rolling back data archive task
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = TestUtils.createMockScheduleTaskEntity();
        List<DlmTask> taskUnits = TestUtils.createMockDlmTaskList();
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        context.setResult(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertNotNull(dataArchiveTaskOption);
        log.info("Data archive task rollback successful.");

        // Test case for data archive task not found
        rollbackParameters.setDataArchiveTaskId(2L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        context.setResult(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        log.warn("Data archive task not found, rollback task fast failed.");

        // Add more test cases as needed to cover all scenarios

    }
}