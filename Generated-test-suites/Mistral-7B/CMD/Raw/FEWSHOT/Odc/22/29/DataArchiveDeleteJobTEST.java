package com.oceanbase.odc.service.schedule.job;

import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.JobType.DELETE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockObjectTestCase;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.JsonUtilsTest;

@Slf4j
public class DataArchiveDeleteJobTest extends MockObjectTestCase {

    private static final String DATA_ARCHIVE_TASK_ID = "1";
    private static final String SCHEDULE_TASK_ID = "2";
    private static final String[] DATA_ARCHIVE_TASK_IDS = {"1", "2", "3"};
    private static final String DATA_ARCHIVE_TASK_NAME = "dataArchiveTaskName";
    private static final String DATA_ARCHIVE_TASK_GROUP = "dataArchiveTaskGroup";
    private static final String JOB_NAME = "jobName";
    private static final String JOB_GROUP = "jobGroup";
    private static final int NUM_TASK_UNITS = 3;
    private static final String SCHEDULE_TASK_JSON = "{\"id\":\"" + SCHEDULE_TASK_ID + "\",\"jobName\":\"" + JOB_NAME + "\",\"jobGroup\":\""
            + JOB_GROUP + "\",\"parametersJson\":\"" + JsonUtilsTest.toJsonString(new DataArchiveClearParameters(DATA_ARCHIVE_TASK_ID)) + "\"}";
    private static final String TASK_ENTITY_JSON = "{\"id\":\"" + SCHEDULE_TASK_ID + "\",\"jobName\":\"" + JOB_NAME + "\",\"jobGroup\":\""
            + JOB_GROUP + "\",\"parametersJson\":\"" + SCHEDULE_TASK_JSON + "\",\"status\":\"EXECUTING\"}";
    private static final String RESULT_JSON = "{\"id\":\"" + SCHEDULE_TASK_ID + "\",\"jobName\":\"" + JOB_NAME + "\",\"jobGroup\":\""
            + JOB_GROUP + "\",\"resultJson\":[{ \"id\":\"1\", \"taskType\":\"DELETE\", \"status\":\"DONE\"},{"
            + "\"id\":\"2\", \"taskType\":\"DELETE\", \"status\":\"DONE\"},{" + "\"id\":\"3\", \"taskType\":\"DELETE\", \"status\":\"DONE\"}]";

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ObjectMapper objectMapper;
    private ScheduleTaskRepository scheduleTaskRepository;
    private StdSchedulerFactory schedulerFactory;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        objectMapper = new ObjectMapper();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        schedulerFactory = mock(StdSchedulerFactory.class);
    }

    @Test
    public void testExecute_DataArchiveTaskFoundAndFinished() {
        // Given
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(new ScheduleTaskEntity(SCHEDULE_TASK_ID, JOB_NAME, JOB_GROUP, TASK_ENTITY_JSON));

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.updateStatusById(SCHEDULE_TASK_ID, TaskStatus.FAILED)).thenReturn(true);
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.updateStatusById(SCHEDULE_TASK_ID, TaskStatus.DONE)).thenReturn(true);

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        assertNotNull(dataArchiveDeleteJob.jobThread);
        assertEquals("EXECUTING", dataArchiveDeleteJob.getTaskStatus(Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask())).name());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(new ScheduleTaskEntity(SCHEDULE_TASK_ID, JOB_NAME, JOB_GROUP, TASK_ENTITY_JSON));

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.empty());

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        log.warn("Data archive task not found,rollback task fast failed.");
        assertNull(dataArchiveDeleteJob.jobThread);
        assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask())).name());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        // Given
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(new ScheduleTaskEntity(SCHEDULE_TASK_ID, JOB_NAME, JOB_GROUP, TASK_ENTITY_JSON));

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.updateStatusById(SCHEDULE_TASK_ID, TaskStatus.FAILED)).thenReturn(true);
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{\"status\":\"RUNNING\"}")));

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        log.warn("Data archive task do not finish,data archive task id = {}", dataArchiveDeleteJob.jobThread);
        assertNull(dataArchiveDeleteJob.jobThread);
        assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask())).name());
    }

    @Test
    public void testExecute_TaskUnitsPreparedAndExecuted() {
        // Given
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(new ScheduleTaskEntity(SCHEDULE_TASK_ID, JOB_NAME, JOB_GROUP, TASK_ENTITY_JSON));

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.updateStatusById(SCHEDULE_TASK_ID, TaskStatus.DONE)).thenReturn(true);

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.updateStatusById(SCHEDULE_TASK_ID, TaskStatus.DONE)).thenReturn(true);

        List<DlmTask> taskUnits = JsonUtils.fromJson(Arrays.asList(new String[]{"{}", "{}", "{}"}),
                new TypeReference<List<DlmTask>>() {});
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{\"resultJson\":[" + JsonUtils.toJsonString(taskUnits) + "]}")));

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        assertEquals(TaskStatus.DONE, dataArchiveDeleteJob.getTaskStatus(Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask())).name());
    }

    @Test
    public void testExecute_TaskUnitsNotPrepared() {
        // Given
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(new ScheduleTaskEntity(SCHEDULE_TASK_ID, JOB_NAME, JOB_GROUP, TASK_ENTITY_JSON));

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, TASK_ENTITY_JSON)));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity(DATA_ARCHIVE_TASK_ID,
                DATA_ARCHIVE_TASK_NAME, DATA_ARCHIVE_TASK_GROUP, "{}")));
        when(scheduleTaskRepository.updateStatusById(SCHEDULE_TASK_ID, TaskStatus.FAILED)).thenReturn(true);

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        log.warn("Task units not prepared,rollback task fast failed.");
        assertNull(dataArchiveDeleteJob.jobThread);
        assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask())).name());
    }
}