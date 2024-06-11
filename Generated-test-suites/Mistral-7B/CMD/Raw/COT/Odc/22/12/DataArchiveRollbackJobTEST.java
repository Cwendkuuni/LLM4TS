package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackJob.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackJob.DataArchiveRollbackJobRepository;
import com.oceanbase.tools.migrator.common.enums.JobThreadStatus;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private DataArchiveRollbackJobRepository dataArchiveRollbackJobRepository;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJobRepository = mock(DataArchiveRollbackJobRepository.class);
        dataArchiveRollbackJob.setDataArchiveRollbackJobRepository(dataArchiveRollbackJobRepository);

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        this.jobExecutionContext = mock(JobExecutionContext.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123");
        scheduleTaskEntity.setId("1");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        try {
            dataArchiveRollbackJob.execute(jobExecutionContext);
            assertNull("Expected rollback job to fail since data archive task not found", dataArchiveRollbackJob.getJobThread());
            assertEquals("Expected rollback job status to be FAILED after task not found", TaskStatus.FAILED,
                    dataArchiveRollbackJob.getStatus());
        } catch (JobExecutionException e) {
            log.error("Caught expected exception: {}", e.getMessage());
        }
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("1");
        dataArchiveTask.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));
        scheduleTaskEntity = dataArchiveTask;
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(new DlmTask());
        when(dataArchiveRollbackJob.executeTask(Long.parseLong("1"), dlmTasks)).thenReturn(true);

        try {
            dataArchiveRollbackJob.execute(jobExecutionContext);

            JobThread jobThread = dataArchiveRollbackJob.getJobThread();
            assertEquals("Expected rollback job thread to be non-null after execution", JobThread.class, jobThread.getClass());
            assertEquals("Expected rollback job status to be DONE after execution", TaskStatus.DONE, dataArchiveRollbackJob.getStatus());
            assertEquals("Expected rollback job thread status to be RUNNING", JobThreadStatus.RUNNING, jobThread.getStatus());
        } catch (JobExecutionException e) {
            log.error("Caught unexpected exception: {}", e.getMessage());
        }
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(new DlmTask());
        dlmTasks.add(new DlmTask());
        dlmTasks.add(new DlmTask());

        assertEquals("Expected all tasks to be DONE when status is DONE", TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
        assertEquals("Expected all tasks to be PREPARING when status is PREPARING", TaskStatus.PREPARING, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
    }
}