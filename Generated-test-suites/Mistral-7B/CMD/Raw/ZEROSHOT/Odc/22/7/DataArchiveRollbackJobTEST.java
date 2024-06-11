import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetail;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for DataArchiveRollbackJob.
 */
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob job;

    private ObjectMapper objectMapper;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private JobThread jobThread;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        jobThread = mock(JobThread.class);
        taskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("non-existing-id");

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        JobExecutionContext context = new JobExecutionContext();
        context.setJobDetail(new JobDetail());
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(taskEntity);

        try {
            job.execute(context);
            assertFalse("Expected the method to throw an exception when data archive task not found.", true);
        } catch (JobExecutionException e) {
            // Expected exception
        }

        assertEquals("Expected the rollback task status to be FAILED.", TaskStatus.FAILED,
                scheduleTaskRepository.getLastStatus(taskEntity.getId()));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-id");
        dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":\"existing-id\"}");

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.getTaskUnits(dataArchiveTask.getId()))
                .thenReturn(taskUnits);

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), anyBoolean()))
                .thenAnswer(invocation -> {
                    if (invocation.getArgument(0) == TaskStatus.DONE) {
                        scheduleTaskRepository.updateStatusById(dataArchiveTask.getId(), TaskStatus.ROLLBACKED);
                    }
                    return true;
                });

        JobExecutionContext context = new JobExecutionContext();
        context.setJobDetail(new JobDetail());
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(taskEntity);

        job.execute(context);

        assertEquals("Expected the rollback task status to be ROLLBACKED.", TaskStatus.ROLLED_BACK,
                scheduleTaskRepository.getLastStatus(dataArchiveTask.getId()));
    }

    /**
     * Helper method to verify if all DLM tasks are prepared for rollback and has correct rollback status.
     *
     * @param taskUnits the list of DLM tasks prepared for rollback
     */
    private void verifyTasksPreparedForRollback(List<DlmTask> taskUnits) {
        for (DlmTask taskUnit : taskUnits) {
            assertEquals("Expected the DLM task status to be PREPARING.", TaskStatus.PREPARING, taskUnit.getStatus());
            assertEquals("Expected the DLM task source database ID to be the target database ID.",
                    taskUnit.getTargetDatabaseId(), taskUnit.getSourceDatabaseId());
            assertEquals("Expected the DLM task job type to be ROLLBACK.", JobType.ROLLBACK, taskUnit.getJobType());
            assertNotNull("Expected the DLM task id to be set.", taskUnit.getId());
        }
    }

    @Test
    public void testExecute_DataArchiveTaskPreparingTasks() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-id");
        dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":\"existing-id\"}");

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnit.setSourceDatabaseId(2L);
        taskUnit.setTargetDatabaseId(1L);
        taskUnit.setJobType(JobType.MIGRATE);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(scheduleTaskRepository.getTaskUnits(dataArchiveTask.getId()))
                .thenReturn(taskUnits);

        JobExecutionContext context = new JobExecutionContext();
        context.setJobDetail(new JobDetail());
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(taskEntity);

        job.execute(context);

        verifyTasksPreparedForRollback(taskUnits);

        assertEquals("Expected the rollback task status to be ROLLBACKING.", TaskStatus.ROLLBACKING,
                scheduleTaskRepository.getLastStatus(dataArchiveTask.getId()));
    }

    @Test
    public void testExecute_DataArchiveTaskDoneTasks() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-id");
        dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":\"existing-id\"}");

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.DONE);
        taskUnit.setSourceDatabaseId(2L);
        taskUnit.setTargetDatabaseId(1L);
        taskUnit.setJobType(JobType.MIGRATE);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(scheduleTaskRepository.getTaskUnits(dataArchiveTask.getId()))
                .thenReturn(taskUnits);

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), anyBoolean()))
                .thenAnswer(invocation -> {
                    if (invocation.getArgument(0) == TaskStatus.DONE) {
                        scheduleTaskRepository.updateStatusById(dataArchiveTask.getId(), TaskStatus.ROLLBACKED);
                    }
                    return true;
                });

        JobExecutionContext context = new JobExecutionContext();
        context.setJobDetail(new JobDetail());
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(taskEntity);

        job.execute(context);

        verifyTasksPreparedForRollback(taskUnits);

        assertEquals("Expected the rollback task status to be ROLLED_BACK.", TaskStatus.ROLLED_BACK,
                scheduleTaskRepository.getLastStatus(dataArchiveTask.getId()));
    }

    @Test
    public void testExecute_DataArchiveTaskFailedTasks() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-id");
        dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":\"existing-id\"}");

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.FAILED);
        taskUnit.setSourceDatabaseId(2L);
        taskUnit.setTargetDatabaseId(1L);
        taskUnit.setJobType(JobType.MIGRATE);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(scheduleTaskRepository.getTaskUnits(dataArchiveTask.getId()))
                .thenReturn(taskUnits);

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), anyBoolean()))
                .thenAnswer(invocation -> {
                    if (invocation.getArgument(0) == TaskStatus.FAILED) {
                        scheduleTaskRepository.updateStatusById(dataArchiveTask.getId(), TaskStatus.FAILED);
                    }
                    return true;
                });

        JobExecutionContext context = new JobExecutionContext();
        context.setJobDetail(new JobDetail());
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(taskEntity);

        job.execute(context);

        verifyTasksPreparedForRollback(taskUnits);

        assertNull("Expected the rollback task status to be NULL when the rollback failed.",
                scheduleTaskRepository.getLastStatus(dataArchiveTask.getId()));
    }
}