package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Mock
    private JobRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        scheduleTaskEntity = new ScheduleTaskEntity();
        dataArchiveTask = new ScheduleTaskEntity();
        taskUnits = new ArrayList<>();

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(null);

        verifyExecutedTaskStatus(TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_UpdateStatus() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        dataArchiveRollbackJob.execute(null);

        verifyExecutedTaskStatus(TaskStatus.FAILED);
        verifyTaskEntityStatus(TaskStatus.FAILED);
    }

    @Test
    public void testExecute_Success() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        taskUnits.add(new DlmTask());
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJsonString(taskUnits));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(null);

        verifyExecutedTaskStatus(TaskStatus.DONE);
        verifyTaskEntityStatus(TaskStatus.DONE);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_NullContext() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        dataArchiveRollbackJob.execute(null);
    }

    private void verifyExecutedTaskStatus(TaskStatus expectedStatus) {
        JsonNode actualResult = JsonUtils.parseJsonStringToJsonNode(dataArchiveRollbackJob.getResultJson());
        JsonNode actualTaskStatusNode = actualResult.get("tasksStatus");
        TaskStatus actualTaskStatus = TaskStatus.valueOf(actualTaskStatusNode.asText());
        assertEquals(expectedStatus, actualTaskStatus);
    }

    private void verifyTaskEntityStatus(TaskStatus expectedStatus) {
        assertEquals(expectedStatus, scheduleTaskEntity.getStatus());
    }
}