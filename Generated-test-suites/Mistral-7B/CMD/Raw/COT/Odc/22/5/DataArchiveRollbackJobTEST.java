package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.JsonUtilsTest;

@Slf4j
public class DataArchiveRollbackJobTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(-1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskRepository scheduleTaskRepository = new ScheduleTaskRepository() {
            @Override
            public Optional<ScheduleTaskEntity> findById(Long id) {
                return Optional.empty();
            }
        };

        JobExecutionContext context = new JobExecutionContext();
        context.setResult(taskEntity);

        try {
            dataArchiveRollbackJob.execute(context);
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_PrepareTasks() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(new ArrayList<>()));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setJobType(JobType.ARCHIVE);
        dlmTask1.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask1);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setSourceDatabaseId(2L);
        dlmTask2.setTargetDatabaseId(1L);
        dlmTask2.setJobType(JobType.ARCHIVE);
        dlmTask2.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask2);

        ScheduleTaskRepository scheduleTaskRepository = new ScheduleTaskRepository() {
            @Override
            public Optional<ScheduleTaskEntity> findById(Long id) {
                return Optional.ofNullable(id.equals(1L) ? dataArchiveTask : null);
            }
        };

        JobExecutionContext context = new JobExecutionContext();
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        List<DlmTask> resultTaskUnits = JsonUtils.fromJson(dataArchiveTask.getResultJson(),
                new TypeReference<List<DlmTask>>() {});

        assertEquals(2, resultTaskUnits.size());
        for (int i = 0; i < resultTaskUnits.size(); i++) {
            DlmTask resultTaskUnit = resultTaskUnits.get(i);
            DlmTask expectedTaskUnit = i == 0 ? dlmTask1 : dlmTask2;
            assertEquals(expectedTaskUnit.getId(), resultTaskUnit.getId());
            assertEquals(expectedTaskUnit.getJobType(), resultTaskUnit.getJobType());
            assertEquals(expectedTaskUnit.getSourceDatabaseId(), resultTaskUnit.getSourceDatabaseId());
            assertEquals(expectedTaskUnit.getTargetDatabaseId(), resultTaskUnit.getTargetDatabaseId());
            assertEquals(expectedTaskUnit.getStatus(), resultTaskUnit.getStatus());
        }

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecute_UpdateTaskStatus() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(new ArrayList<>()));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setJobType(JobType.ARCHIVE);
        dlmTask1.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask1);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setSourceDatabaseId(2L);
        dlmTask2.setTargetDatabaseId(1L);
        dlmTask2.setJobType(JobType.ARCHIVE);
        dlmTask2.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask2);

        ScheduleTaskRepository scheduleTaskRepository = new ScheduleTaskRepository() {
            @Override
            public Optional<ScheduleTaskEntity> findById(Long id) {
                return Optional.ofNullable(id.equals(1L) ? dataArchiveTask : null);
            }

            @Override
            public void updateStatusById(Long id, TaskStatus status) {
                if (dataArchiveTask != null) {
                    dataArchiveTask.setStatus(status);
                }
            }
        };

        JobExecutionContext context = new JobExecutionContext();
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test(expected = AssertionError.class)
    public void testExecute_UnexpectedException() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(new ArrayList<>()));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setJobType(JobType.ARCHIVE);
        dlmTask1.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask1);

        ScheduleTaskRepository scheduleTaskRepository = new ScheduleTaskRepository() {
            @Override
            public Optional<ScheduleTaskEntity> findById(Long id) {
                return Optional.ofNullable(id.equals(1L) ? dataArchiveTask : null);
            }
        };

        JobExecutionContext context = new JobExecutionContext();
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        DlmJobIdUtil dlmJobIdUtil = new DlmJobIdUtil() {
            @Override
            public Long generateHistoryJobId(String jobName, String jobGroup, Long jobId, int sequence) {
                throw new AssertionError("Unexpected exception");
            }
        };

        try {
            dataArchiveRollbackJob.execute(context);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // Expected
        }

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}