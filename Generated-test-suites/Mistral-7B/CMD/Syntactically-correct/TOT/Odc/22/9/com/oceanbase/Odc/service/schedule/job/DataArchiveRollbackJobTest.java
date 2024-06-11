import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.JsonUtils;

public class DataArchiveRollbackJobTest {

    private Scheduler scheduler;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private static final String JOB_NAME = "testJobName";
    private static final String JOB_GROUP = "testJobGroup";
    private static final String TASK_ID = "testTaskId";
    private static final String DATA_ARCHIVE_TASK_ID = "testDataArchiveTaskId";

    @Before
    public void setup() {
        scheduler = new StdSchedulerFactory().getScheduler();
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_SingleDataArchiveTask_Success() throws Exception {
        // Given
        MockitoJobDetail jobDetail = new MockitoJobDetail(JOB_NAME, JOB_GROUP);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(TASK_ID);
        scheduleTaskEntity.setJobName(JOB_NAME);
        scheduleTaskEntity.setJobGroup(JOB_GROUP);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(DATA_ARCHIVE_TASK_ID)));
        jobDetail.setJobDataMap(new JobExecutionContext().getJobDetail().getJobDataMap());
        jobDetail.setJobClass(DataArchiveRollbackJob.class);

        Trigger trigger = Mockito.mock(Trigger.class);
        Mockito.when(trigger.getKey()).thenReturn(JobExecutionContext.KEY);
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

        Mockito.doAnswer(invocation -> {
            DataArchiveRollbackParameters dataArchiveRollbackParameters = JsonUtils.fromJson(invocation.getArgument(0),
                    DataArchiveRollbackParameters.class);
            Optional<ScheduleTaskEntity> dataArchiveTaskOption = Mockito.mock(Optional.class);
            dataArchiveTaskOption.get().setId(DATA_ARCHIVE_TASK_ID);
            dataArchiveTaskOption.get().setResultJson(JsonUtils.toJsonString(List.of(createDlmTask())));
            Mockito.when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                    .thenReturn(dataArchiveTaskOption.get());
            return null;
        }).when(dataArchiveRollbackJob).execute(Mockito.any(JobExecutionContext.class));

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(TASK_ID, TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_Failure() throws Exception {
        // Given
        MockitoJobDetail jobDetail = new MockitoJobDetail(JOB_NAME, JOB_GROUP);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(TASK_ID);
        scheduleTaskEntity.setJobName(JOB_NAME);
        scheduleTaskEntity.setJobGroup(JOB_GROUP);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(DATA_ARCHIVE_TASK_ID)));
        jobDetail.setJobDataMap(new JobExecutionContext().getJobDetail().getJobDataMap());
        jobDetail.setJobClass(DataArchiveRollbackJob.class);

        Trigger trigger = Mockito.mock(Trigger.class);
        Mockito.when(trigger.getKey()).thenReturn(JobExecutionContext.KEY);
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

        Mockito.doAnswer(invocation -> {
            DataArchiveRollbackParameters dataArchiveRollbackParameters = JsonUtils.fromJson(invocation.getArgument(0),
                    DataArchiveRollbackParameters.class);
            Optional<ScheduleTaskEntity> dataArchiveTaskOption = Mockito.mock(Optional.class);
            Mockito.when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                    .thenReturn(dataArchiveTaskOption);
            return null;
        }).when(dataArchiveRollbackJob).execute(Mockito.any(JobExecutionContext.class));

        Mockito.doThrow(new Exception("task not found"))
                .when(scheduleTaskRepository)
                .findById(DATA_ARCHIVE_TASK_ID);

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(TASK_ID, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTasksEmpty_Failure() throws Exception {
        // Given
        MockitoJobDetail jobDetail = new MockitoJobDetail(JOB_NAME, JOB_GROUP);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(TASK_ID);
        scheduleTaskEntity.setJobName(JOB_NAME);
        scheduleTaskEntity.setJobGroup(JOB_GROUP);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(DATA_ARCHIVE_TASK_ID)));
        jobDetail.setJobDataMap(new JobExecutionContext().getJobDetail().getJobDataMap());
        jobDetail.setJobClass(DataArchiveRollbackJob.class);

        Trigger trigger = Mockito.mock(Trigger.class);
        Mockito.when(trigger.getKey()).thenReturn(JobExecutionContext.KEY);
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

        Mockito.doAnswer(invocation -> {
            DataArchiveRollbackParameters dataArchiveRollbackParameters = JsonUtils.fromJson(invocation.getArgument(0),
                    DataArchiveRollbackParameters.class);
            Optional<ScheduleTaskEntity> dataArchiveTaskOption = Mockito.mock(Optional.class);
            dataArchiveTaskOption.get().setId(DATA_ARCHIVE_TASK_ID);
            dataArchiveTaskOption.get().setResultJson(JsonUtils.toJsonString(List.of()));
            Mockito.when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                    .thenReturn(dataArchiveTaskOption.get());
            return null;
        }).when(dataArchiveRollbackJob).execute(Mockito.any(JobExecutionContext.class));

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(TASK_ID, TaskStatus.FAILED);
    }

    private DlmTask createDlmTask() {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setJobType(JobType.ROLLBACK);
        return dlmTask;
    }

    @After
    public void tearDown() throws Exception {
        scheduler.shutdown();
    }
}