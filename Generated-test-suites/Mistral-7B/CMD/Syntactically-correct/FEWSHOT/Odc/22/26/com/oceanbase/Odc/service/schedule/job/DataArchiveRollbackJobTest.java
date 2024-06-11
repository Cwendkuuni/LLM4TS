package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockJobExecutionContext;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class DataArchiveRollbackJobTest {

    private static final String JOB_NAME = "testJob";
    private static final String JOB_GROUP = "testGroup";
    private static final String TASK_ID = "1";
    private static final String DATA_ARCHIVE_TASK_ID = "2";
    private static final ObjectMapper mapper = new ObjectMapper();
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private StdSchedulerFactory schedulerFactory;

    @Before
    public void setup() {
        scheduleTaskRepository = new ScheduleTaskRepository() {
            @Override
            public Optional<ScheduleTaskEntity> findById(String id) {
                if (id.equals(taskEntity.getId())) {
                    return Optional.of(taskEntity);
                }
                return Optional.empty();
            }

            @Override
            public void updateStatusById(String id, TaskStatus status) {
                // no-op
            }
        };
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(TASK_ID);
        taskEntity.setJobName(JOB_NAME);
        taskEntity.setJobGroup(JOB_GROUP);
        taskEntity.setParametersJson(mapper.writeValueAsString(new DataArchiveRollbackParameters(DATA_ARCHIVE_TASK_ID)));

        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        schedulerFactory = new StdSchedulerFactory();
    }

    @After
    public void teardown() {
        if (dataArchiveRollbackJob != null) {
            dataArchiveRollbackJob = null;
        }
        if (schedulerFactory != null) {
            try {
                schedulerFactory.shutdown();
            } catch (SchedulerException e) {
                // no-op
            }
            schedulerFactory = null;
        }
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        taskEntity.setId(TASK_ID);
        taskEntity.setJobName(JOB_NAME);
        taskEntity.setJobGroup(JOB_GROUP);
        taskEntity.setParametersJson(mapper.writeValueAsString(new DataArchiveRollbackParameters(DATA_ARCHIVE_TASK_ID + "notExist")));

        MockJobExecutionContext context = new MockJobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters(DATA_ARCHIVE_TASK_ID);
        taskEntity.setId(TASK_ID);
        taskEntity.setJobName(JOB_NAME);
        taskEntity.setJobGroup(JOB_GROUP);
        taskEntity.setParametersJson(mapper.writeValueAsString(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(DATA_ARCHIVE_TASK_ID);
        dataArchiveTask.setResultJson(mapper.writeValueAsString(createDlmTasks(1)));

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        MockJobExecutionContext context = new MockJobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
        verifyTasksExecuted(dataArchiveTask.getResultJson());
    }

    private String createDlmTasks(int size) {
        List<DlmTask> dlmTasks = Collections.nCopies(size, new DlmTask());
        return mapper.writeValueAsString(dlmTasks);
    }

    private void verifyTasksExecuted(String json) {
        List<DlmTask> dlmTasks = mapper.readValue(json, new TypeReference<List<DlmTask>>() {});

        for (int i = 0; i < dlmTasks.size(); i++) {
            DlmTask dlmTask = dlmTasks.get(i);
            assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
            assertEquals(TaskStatus.DONE, dlmTask.getStatus());
            assertEquals(dlmTasks.get(dlmTasks.size() - 1 - i).getId(), dlmTask.getId());
        }
    }
}