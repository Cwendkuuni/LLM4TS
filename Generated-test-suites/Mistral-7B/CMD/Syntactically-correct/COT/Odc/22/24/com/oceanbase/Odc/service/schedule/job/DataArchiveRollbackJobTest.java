package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJobTest;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest extends AbstractDlmJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute_NoDataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("nonExistentTaskId");
        scheduleTaskEntity.setId("testTaskId");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        expectThrows(JobExecutionException.class, () -> {
            dataArchiveRollbackJob.execute(new JobExecutionContext() {});
        });

        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.findById("testTaskId").map(ScheduleTaskEntity::getStatus).orElse(TaskStatus.RUNNING));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existingTaskId");
        scheduleTaskEntity.setId("testTaskId");
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setJobType(JobType.BACKUP);
        taskUnit.setStatus(TaskStatus.RUNNING);
        taskUnits.add(taskUnit);

        expectNoThrowing(() -> {
            dataArchiveRollbackJob.execute(new JobExecutionContext() {
                @Override
                public ScheduleTaskEntity getMergedJobData() {
                    return scheduleTaskEntity;
                }

                @Override
                public Object getMappedJobData() {
                    return scheduleTaskEntity;
                }
            });

            JsonNode resultJson = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), ObjectMapper.class).get("tasks");
            List<JsonNode> tasks = resultJson.asList();

            for (JsonNode task : tasks) {
                assertEquals(taskUnit.getId(), task.get("id").longValue());
                assertEquals(DlmJobIdUtil.generateHistoryJobId("testJobName", "testJobGroup", "testTaskId", tasks.size()), task.get("id").longValue());
                assertEquals(JobType.ROLLBACK, task.get("jobType").asText());
                assertEquals(taskUnit.getStatus() == TaskStatus.PREPARING ? TaskStatus.DONE : TaskStatus.PREPARING, task.get("status").asText());
            }
        });

        assertEquals(TaskStatus.DONE, scheduleTaskRepository.findById("testTaskId").map(ScheduleTaskEntity::getStatus).orElse(TaskStatus.RUNNING));
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_JsonParsingException() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("{invalidJson}");
        scheduleTaskEntity.setId("testTaskId");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        dataArchiveRollbackJob.execute(new JobExecutionContext() {});
    }
}